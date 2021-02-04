package com.cg.healthcaresystemrest.controller;


import java.io.ByteArrayInputStream;
import java.io.IOException;
/*
 * Author: Jayesh Gaur
 * Description: Controller class for all functionalities
 * Created on: October 14, 2019
 */
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cg.healthcaresystemrest.dto.Appointment;
import com.cg.healthcaresystemrest.dto.AppointmentRequest;
import com.cg.healthcaresystemrest.dto.DiagnosticCenter;
import com.cg.healthcaresystemrest.dto.Test;
import com.cg.healthcaresystemrest.dto.User;
import com.cg.healthcaresystemrest.exceldownload.ExcelGenerator;
import com.cg.healthcaresystemrest.exception.ValidationException;
import com.cg.healthcaresystemrest.service.UserServiceImpl;


@CrossOrigin(origins="http://localhost:4200")
@RestController
public class HCSController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	HttpSession session;
	
	private static final Logger logger = LoggerFactory.getLogger(HCSController.class);
	
	
	/*
	 * Author: 		Jayesh Gaur
	 * Description: /adminpage and /userpage -> used for debugging JWT Authentication
	 * Created on: October 14, 2019
	 */
	@GetMapping(value = "/adminpage")
	public String adminPage() {
		logger.info("Returning admin page..");
		return "Admin Page..";
	}
	
	@GetMapping(value = "/userpage")
	public String userPage() {
		logger.info("Returning user page..");
		return "User Page..";
	}
	
	/*
	 * Author: 			Jayesh Gaur 
	 * Description: 	Returns a list of all centers
	 * Created on: 		October 9, 2019 
	 * Input: 			NULL
	 * Output: 			A list of all center objects
	 */ 
	@GetMapping(value="/getCenters")
	public ResponseEntity<List<DiagnosticCenter>> getCenters(){
		logger.info("Getting list of all centers in the system..");
		List<DiagnosticCenter> centerList = userService.getCenterList();
		if(centerList.size() == 0) {
			logger.info("No centers present in the system. Returning NO_CONTENT");
			return new ResponseEntity<List<DiagnosticCenter>>(HttpStatus.NO_CONTENT);
		}else {
			logger.info("Returning center list..");
			return new ResponseEntity<List<DiagnosticCenter>>(centerList,HttpStatus.OK);
		}
		}
	
	/*
	 * Author: 			Jayesh Gaur 
	 * Description: 	Returns a list of all tests in the center corresponding to the center Id
	 * Created on: 		October 9, 2019 
	 * Input: 			Center Id of the center you want to retrieve tests of
	 * Output: 			A list of all test objects in the center object corresponding to the
	 * 						center Id entered
	 */ 
	@GetMapping(value="/getTests")
	public ResponseEntity<List<Test>> getTests(@RequestParam("centerId") String stringCenterId){
		BigInteger centerId=null;
		List<Test> testList = null;
		try {
			logger.info("Validating center id entered as parameter..");
			centerId = userService.validateCenterId(stringCenterId, userService.getCenterList());
			logger.info("Center Id validated... Getting test List");
			testList = userService.getListOfTests(centerId);
			if (testList.size() > 0) {
				logger.info("Returning test list in the center..");
				return new ResponseEntity<List<Test>>(testList, HttpStatus.OK);
			} else {
				logger.info("Test list is empty.");
				return new ResponseEntity<List<Test>>(HttpStatus.NO_CONTENT);
			}
		} catch (ValidationException exception) {
			logger.error("Caught Validation Exception in /getTests Controller... ");
			return new ResponseEntity<List<Test>>(HttpStatus.BAD_REQUEST);
		}	
	}
	
	/*
	 * Author: 			Jayesh Gaur 
	 * Description: 	Creates an appointment for the user. A
	 * Created on: 		October 9, 2019 
	 * Input: 			Center Id, Test Id and DateAndTime in the form of String 
	 * Output: 			Appointment object of the newly created appointment
	 */ 
	
	@PostMapping(value="/addAppointment")
	public ResponseEntity<?> addAppointment(@RequestBody AppointmentRequest appointmentRequest) {
		BigInteger centerId = null;
		BigInteger testId = null;
		BigInteger userId = null;
		LocalDateTime dateAndTime = null;
		try{
			//Getting the center object
			logger.info("Validating center Id entered for appointment..");
			centerId = userService.validateCenterId(appointmentRequest.getCenterId(), userService.getCenterList());
			logger.info("Getting the center corresponding to the center Id");
			DiagnosticCenter center = userService.findCenter(centerId);
			
			//Getting the test object
			logger.info("Validating test Id entered for appointment..");
			testId = userService.validateTestId(appointmentRequest.getTestId(), userService.getListOfTests(centerId));
			logger.info("Getting the test corresponding to the test Id");
			Test test = userService.findTest(testId);
			
			//getting the user object
			logger.info("Validating user Id entered for appointment");
			userId = userService.validateUserId(appointmentRequest.getUserId());
			logger.info("Getting the user corresponding to the user Id");
			User user = userService.findUser(userId);
			
			//Validating date and time
			logger.info("Validating date and time..");
			dateAndTime = userService.validateDateTime(appointmentRequest.getDateAndTime());
			
			logger.info("All inputs valid.. creating appointment");
			Appointment appointment = new Appointment();
			appointment.setCenter(center);
			appointment.setTest(test);
			appointment.setUser(user);
			appointment.setDateTime(dateAndTime);
			logger.info("Appointment created.. returning appointment object");
			appointment = userService.addAppointment(appointment);
			logger.info("New Appointment Added. AUDIT TRAIL=> Appointment ID: "+appointment.getAppointmentId()+" Created on: "+appointment.getCreationDate()+" Created by: "+appointment.getCreatedBy());
			return new ResponseEntity<Appointment>(appointment,HttpStatus.OK);
		}catch(ValidationException exception) {
			logger.error("Caught validation exception in /addAppointment Controller");
			return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	/*
	 * Author: 			Jayesh Gaur
	 * Description: 	Returns a list of all appointments of the current user
	 * Input: 			User Id to identify the user
	 * Output: 			List of Appointments
	 * Created on:		October 14, 2019
	 */
	@GetMapping(value="/viewAppointments")
	public ResponseEntity<?> viewAppointments(@RequestParam("userId") String stringUserId){
		BigInteger userId = null;
		try {
			logger.info("Validating user Id entered for appointment");
			userId = userService.validateUserId(stringUserId);
			logger.info("Getting appointments of the user corresponding to the user id..");
			List<Appointment> appointmentList = userService.getAppointmentList(userId);
			if(appointmentList.size() > 0) {
				logger.info("Returning appointment list to the user..");
				return new ResponseEntity<List<Appointment>>(appointmentList, HttpStatus.OK);
			}else {
				logger.info("0 appointments in the system from this user.. returning nothing");
				return new ResponseEntity<String>("You have no appointments", HttpStatus.NO_CONTENT);
			}
			
		}catch(ValidationException exception) {
			logger.error("Caught validation exception in /viewAppointments Controller");
			return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	/*
	 * Author: 			Jayesh Gaur
	 * Description: 	Returns a list of all appointments of the center
	 * Input: 			center id
	 * Output: 			List of Appointments
	 * Created on:		October 14, 2019
	 */
	@GetMapping(value="/pendingAppointments")
	public ResponseEntity<?> pendingAppointments(@RequestParam("centerId") String stringCenterId){
		BigInteger centerId = null;
		try {
			logger.info("Validating center Id entered for appointment..");
			centerId = userService.validateCenterId(stringCenterId, userService.getCenterList());
			logger.info("Getting appointments of the user corresponding to the user id..");
			List<Appointment> appointmentList = userService.getCenterAppointmentList(centerId);
			if(appointmentList.size() > 0) {
				logger.info("Returning appointment list to the admin..");
				return new ResponseEntity<List<Appointment>>(appointmentList, HttpStatus.OK);
			}else {
				logger.info("0 appointments in the system from this center.. returning nothing");
				return new ResponseEntity<String>("No Appointments in this center", HttpStatus.NO_CONTENT);
			}
		}catch(ValidationException exception) {
			logger.error("Caught validation exception in /pendingAppointments Controller");
			return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	/*
	 * Author: 		Jayesh Gaur
	 * Description: Approves the appointment status from pending to approved
	 * Input: 		Appointment Id of the appointment to be approved
	 * Output: 		Status of the operation: true or error
	 * Created on: 	October 14, 2019
	 */
	@PostMapping(value="/approveAppointment")
	public ResponseEntity<?> approveAppointments(@RequestParam("appointmentId") String stringAppointmentId){
		BigInteger appointmentId = null;
		try {
			logger.info("Validating appointment Id selected for approval");
			appointmentId = userService.validateAppointmentId(stringAppointmentId, userService.getAppointments());
			logger.info("Appointment Id Validated... approving appointment.");
			if(userService.approveAppointment(appointmentId)) {
				
				return new ResponseEntity<Boolean>(true,HttpStatus.OK);
			}
			return null;
		}catch(ValidationException exception) {
			logger.error("Caught validation exception in /approveAppointment Controller");
			return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	/*
	 *Author: 			Jayesh Gaur
	 *Description:		Controller method to map download excel request of the user.
	 *Created on: 		October 22, 2019
	 *Input: 			User Id
	 *Output: 			Excel file of the user
	 */
	@GetMapping("/download")
	public ResponseEntity<InputStreamResource> downloadAppointments(@RequestParam("userId") BigInteger userId) throws IOException {
		List<Appointment> appointmentList = userService.getAppointmentList(userId);

		ByteArrayInputStream in = ExcelGenerator.customersToExcel(appointmentList);
		// return IOUtils.toByteArray(in);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}
	
	/*
	 * Author: 			Kushal
	 * Description: 	Reads an excel file having names of tests inside rows and adds it into the center selected in parameter
	 * Created on: 		October 22, 2019
	 * Input: 			Excel file having test list and center id
	 */
	@PostMapping("/uploadtest")
//	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> uploadTest(@RequestParam("file") MultipartFile reapExcelDataFile, @RequestParam("centerId") BigInteger centerId) throws IOException {
System.out.println("testUPLOAD");
XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
   XSSFSheet worksheet = workbook.getSheetAt(0);
   System.out.println(worksheet.getLastRowNum());
   for(int i=0;i<=worksheet.getLastRowNum() ;i++) {
       Test tempTest = new Test ();

       XSSFRow row = worksheet.getRow(i);
       if(row != null) {
        tempTest .setTestName(row.getCell(0).getStringCellValue());
       userService.addTest(centerId,tempTest);}
       workbook.close();
       
           
   }
   return new ResponseEntity<String>("Added successfully!", HttpStatus.OK);
}
	/*
	 * Author: 			Jayesh Gaur
	 * Description: 	Returns user Id of the user associated with the useremail in parameter
	 * Created on: 		October 23, 2019
	 * Input: 			User Email
	 * Output: 			User id
	 */
	@GetMapping("/finduser")
	public ResponseEntity<?> findUser(@RequestParam("userEmail") String userEmail){
		try {
			logger.info("Fetching user object linked with user Email..");
			User user = userService.findUser(userEmail);
			return new ResponseEntity<User>(user,HttpStatus.OK);
		} catch (ValidationException exception) {
			logger.info("ValidationException caught in find user controller..");
			return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	/*
	 * Author: 			Jayesh Gaur
	 * Description: 	Returns role of the user associated with the useremail in parameter
	 * Created on: 		October 23, 2019
	 * Input: 			User Email
	 * Output: 			User id
	 */
	@GetMapping("/finduserrole")
	public ResponseEntity<?> findUserRole(@RequestParam("userEmail") String userEmail){
		try {
			logger.info("Fetching user object linked with user Email..");
			User user = userService.findUser(userEmail);
			return new ResponseEntity<String>(user.getUserRole(),HttpStatus.OK);
		} catch (ValidationException exception) {
			logger.info("ValidationException caught in find user controller..");
			return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}


	/*
	 * Author: 			Jayesh Gaur
	 * Description: 	Maps the reject appointment request and takes care of rejecting appointments
	 * Created on: 		October 24, 2019
	 * Input: 			Appointment Id
	 * Output:			Boolean status of the delete operation
	 */
	@PostMapping("/rejectappointment")
	public ResponseEntity<?> rejectAppointment(@RequestParam("appointmentId") BigInteger appointmentId)
	{
		
			if(userService.rejectAppointment(appointmentId)) {
				return new ResponseEntity<String>("Appointment Rejected!", HttpStatus.OK);
			}
			else {
				return new ResponseEntity<String>("Please try again",HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
	}
	


	/*
	* Author: Nidhi
	* Description: Add Test to the given center
	* Created: October 14, 2019
	* Input: BigInteger centerId,Test test
	* Output: ResponseEntity<Test>
	*
	*/
	@PostMapping("/addTest")
	public ResponseEntity<?> addTest(@RequestParam("centerId")String id,@ModelAttribute Test test) throws ValidationException
	{
	try {
	logger.info("Validating center Id entered for add Test..");
	BigInteger centerId=userService.validateCenterId(id,userService.getCenterList());
	Test newTest=userService.addTest(centerId, test);
	logger.info("adding test to the given centerId");
	if(newTest==null)
	{
	return new ResponseEntity<String>("Data not added",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	else
	{
	logger.info("Test added! AUDIT TRAIL=> Created on: "+newTest.getCreationDate()+" Created by: "+newTest.getCreatedBy());
	return new ResponseEntity<Test>(newTest,HttpStatus.OK);
	}}
	catch(ValidationException exception) {
	logger.error("Caught Validation Exception in /addTests Controller... ");
	return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
	}
	}

	/*
	* Author: Nidhi
	* Description: Remove Test to the given center
	* Created: October 14, 2019
	* Input: BigInteger centerId,BigInteger testId
	* Output: ResponseEntity<Test>
	*
	*/

	@DeleteMapping("/removeTest")
	public ResponseEntity<?> deleteTest(@RequestParam("centerId")String stringCenterId,@RequestParam("testId")String stringTestId) throws Exception  {
	boolean remove=false;
	BigInteger centerId=null;
	BigInteger testId=null;
	try {

	centerId=userService.validateCenterId(stringCenterId,userService.getCenterList());
	testId=userService.validateTestId(stringTestId, userService.getListOfTests(centerId));
	   remove=userService.removeTest(centerId, testId);
	    logger.info("removing test from the given centerId");

	//System.out.println(exception.getMessage());

	if(remove==true)
	return new ResponseEntity<String>("test deleted",HttpStatus.OK);
	else if(remove==false)
	return new ResponseEntity<String>("Data not deleted",HttpStatus.OK);
	else
	return new ResponseEntity<String>("Data not added",HttpStatus.INTERNAL_SERVER_ERROR);

	  }
	catch(ValidationException exception) {
	logger.error("Caught Validation Exception in /removeTest Controller... ");
	return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
	} }
	
	/*
	* Author: Kushal
	* Description: Add Center
	* Created: October 14, 2019
	* Input: DiagnosticCenter center
	* Output: ResponseEntity<DiagnosticCenter>
	*
	*/


	@PostMapping("/addCenter")
	public ResponseEntity<?> addTest(@RequestBody DiagnosticCenter center)
	{

		
	DiagnosticCenter newCenter=userService.addCenter(center);

	if(newCenter==null)
	{
	return new ResponseEntity<String>("Center not added",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	else
	{
	logger.info("Center added. Audit Details: CenterID: "+newCenter.getCenterId()+" Created on: "+newCenter.getCreationDate()+". Created by: "+newCenter.getCreatedBy());
	return new ResponseEntity<DiagnosticCenter>(newCenter,HttpStatus.OK);
	}
	}
	/*
	* Author: Kushal
	* Description: Remove center
	* Created: October 14, 2019
	* Input: BigInteger centerId
	* Output: ResponseEntity<DiagnosticCenter>
	*
	*/

	@DeleteMapping("/removeCenter")
	public ResponseEntity<?> deleteCenter(@RequestParam("centerId")String stringCenterId) throws Exception  {
	boolean remove=false;
	BigInteger centerId=null;
	try {

		centerId=userService.validateCenterId(stringCenterId,userService.getCenterList());
	    remove=userService.removeCenter(centerId);


	//System.out.println(exception.getMessage());

	if(remove==true) {
	return new ResponseEntity<String>("Center deleted successfully",HttpStatus.OK);

	}else 
	return new ResponseEntity<String>(" Center not deleted",HttpStatus.OK);
	
	}catch(ValidationException exception) {
		logger.error("Caught Validation Exception in /deleteCenter Controller... ");
		return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
		} 
}}
