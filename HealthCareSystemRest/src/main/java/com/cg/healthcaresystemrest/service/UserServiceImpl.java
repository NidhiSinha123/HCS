package com.cg.healthcaresystemrest.service;


/*
 * Author: Jayesh Gaur
 * Description: Service Class
 * Created on: October 9, 2019
 */
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cg.healthcaresystemrest.dto.Appointment;
import com.cg.healthcaresystemrest.dto.DiagnosticCenter;
import com.cg.healthcaresystemrest.dto.Test;
import com.cg.healthcaresystemrest.dto.User;
import com.cg.healthcaresystemrest.exception.ExistingCredentialException;
import com.cg.healthcaresystemrest.exception.UserErrorMessage;
import com.cg.healthcaresystemrest.exception.ValidationException;
import com.cg.healthcaresystemrest.repository.AppointmentRepository;
import com.cg.healthcaresystemrest.repository.CenterRepository;
import com.cg.healthcaresystemrest.repository.TestRepository;
import com.cg.healthcaresystemrest.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestRepository testRepository;

	@Autowired
	private CenterRepository centerRepository;
	
	@Autowired
    private JavaMailSender javaMailSender;


	@Autowired
	private AppointmentRepository appointmentRepository;
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	public DiagnosticCenter addCenter(DiagnosticCenter center) {
		return centerRepository.save(center);
	}

	public boolean removeCenter(BigInteger centerId) throws ValidationException {
		Optional<DiagnosticCenter> center = centerRepository.findById(centerId);
		if (!center.isPresent()) {
			throw new ValidationException(UserErrorMessage.userErrorInvalidCenterId);
		}
		DiagnosticCenter diagnosticCenter = center.get();
		diagnosticCenter.setDeleted(true);
		logger.info("Center Deleted. Audit Details: CenterID: "+diagnosticCenter.getCenterId()+" Modified on: "+diagnosticCenter.getLastModifiedDate()+". Modified by: "+diagnosticCenter.getLastModifiedBy());
		return true;
	}

	/*
	 * Author : Nidhi Description : This service method is adding test to the list
	 * of tests of center and saving the test using the save method of Test
	 * Repository. Created Date : 9th October,2019 Input: Test Return Type : Test
	 * 
	 */

	public Test addTest(BigInteger centerId, Test test) {
		DiagnosticCenter center = centerRepository.findById(centerId).get();
		center.getListOfTests().add(test);
		return testRepository.save(test);
	}

	/*
	 * Author : Nidhi Description : This service method is removing test from the
	 * list of tests of center and removing the test by setting isDeleted attribute
	 * of Test to true. Created Date : 9th October,2019 Input: BigInteger
	 * removeCenterId, BigInteger removeTestId Return Type : boolean
	 * 
	 */
	public boolean removeTest(BigInteger removeCenterId, BigInteger removeTestId) throws ValidationException {
		DiagnosticCenter center = centerRepository.findById(removeCenterId).get();
		if (center == null) {
			throw new ValidationException("center is not present");
		}
		Test test = testRepository.findById(removeTestId).get();
		if (test == null) {
			throw new ValidationException("test not found");
		}
		test.setDeleted(true);
		center.getListOfTests().remove(test);
		logger.info("Test Deleted. AUDIT TRAIL=> TestID: "+test.getTestId()+" Modified by: "+test.getLastModifiedBy()+"Modified on: "+test.getLastModifiedDate());
		// testrepository.deleteById(removeTestId);
		return true;
	}

	/*
	 * Author: Nidhi Description: Returns the list of appointments which are not
	 * approved in the center corresponding to the center id received in input
	 * Created on: October 9, 2019
	 */
	@Override
	public List<Appointment> getCenterAppointmentList(BigInteger centerId) throws ValidationException {
		Optional<DiagnosticCenter> center = centerRepository.findById(centerId);
		if (center.isPresent()) {
			return appointmentRepository.findByCenterAndAppointmentStatus(center.get(), 0);
		} else {
			throw new ValidationException(UserErrorMessage.userErrorInvalidCenterId);
		}
	}

	/*
	 * Author: Nidhi Description: Returns the list of appointment corresponding to
	 * the user Id received in input Created on: October 11, 2019
	 */

	@Override
	public List<Appointment> getAppointmentList(BigInteger userId) {
		User user = userRepository.findById(userId).get();
		List<Appointment> appointmentList = appointmentRepository.findByUser(user);
		return appointmentList;
	}
	/*
	 * Author: Nidhi Description: Returns the test object corresponding to the test
	 * Id received in input Created on: October 9, 2019
	 */

	@Override
	public Test findTest(BigInteger testId) {
		return testRepository.findById(testId).get();
	}

	/*
	 * Author: Nidhi Description: Returns the list of test corresponding to the
	 * center Id received in input Created on: October 9, 2019
	 */
	@Override
	public List<Test> getListOfTests(BigInteger centerId) {
		DiagnosticCenter center = centerRepository.findById(centerId).get();
		List<Test> testList = center.getListOfTests();
		return testList;
	}

	/*
	 * Author: Jayesh Gaur Description: Service method for registration. Calls the
	 * Repository save method and returns the automatically generated new user Id.
	 * Exceptions thrown: If unique values like email or phone number already exist
	 * in database, exception is thrown and user is notified. Created on: October 9,
	 * 2019
	 */
	public BigInteger register(User user) throws ExistingCredentialException {
		logger.info("Checking if the email is already registered..");
		// Validating unique database columns
		Optional<User> checkUserCredentials = userRepository.findByUserEmail(user.getUserEmail());
		if (checkUserCredentials.isPresent()) {
			logger.error("An existing account with this email found... throwing ExistingCredentialException");
			throw new ExistingCredentialException(UserErrorMessage.userErrorDuplicateEmail);
		} else {
			checkUserCredentials = null;
			logger.info("Email is unique. Checking if the phone number is already registered..");
			checkUserCredentials = userRepository.findByContactNo(user.getContactNo());
			if (checkUserCredentials.isPresent()) {
				logger.error("An existing account with this contact found... throwing ExistingCredentialException");
				throw new ExistingCredentialException(UserErrorMessage.userErrorDuplicatePhoneNumber);
			}
		}
		// save if email and phone numbers are unique
		logger.info("Phone number is also unique. Registering the user..");
		return userRepository.save(user).getUserId();
	}

	public List<DiagnosticCenter> getCenterList() {
		return centerRepository.getCenterList();
	}

	/*
	 * Author: Jayesh Gaur Description: TO BE DONE (NOT VALIDATED) Created on:
	 * October 9, 2019
	 */
	public BigInteger userLogin(String email, String password) {
		// check if a user exists whose credentials match, if it does, get the user
		// object
		User user = userRepository.findByUserEmailAndUserPassword(email, password);
		// reference for bigInteger user Id to return to controller
		BigInteger userId = null;

		// Check if any user object has been returned after matching credentials
		if (null != user) {
			// get his user Id
			userId = user.getUserId();
		}
		// return user Id
		return userId;
	}

	/*
	 * Author: Jayesh Gaur Description: Checks if the center Id in parameter 1 is
	 * present in the list of all centers present in parameter 2 Input: String
	 * Center Id and List<DiagnosticCenter> Output: throws ValidationException if
	 * invalid center Id Returns BigInteger value of the center ID if centerID is
	 * valid Created on: October 9, 2019
	 */
	public BigInteger validateCenterId(String centerId, List<DiagnosticCenter> centerList) throws ValidationException {
		
		if (centerId.matches("^[0-9]+")) {
			
			for (Iterator<DiagnosticCenter> iterator = centerList.iterator(); iterator.hasNext();) {
				DiagnosticCenter diagnosticCenter = iterator.next();
				if (diagnosticCenter.getCenterId().compareTo(new BigInteger(centerId)) == 0) {
					logger.info("Center Id is proper... returning BigInteger value of the entered Id..");
					return new BigInteger(centerId);
				}
			}
		}
		logger.error("Invalid Center Id... throwing ValidationException in UserService.validateCenterId");
		throw new ValidationException(UserErrorMessage.userErrorInvalidCenterId);
	}

	/*
	 * Author: Jayesh Gaur Description: Checks if the test Id in parameter 1 is
	 * present in the list of all tests present in parameter 2 Input: String Test Id
	 * and List<Test> testList Output: throws ValidationException if invalid test Id
	 * Returns BigInteger value of the test ID if the testId is valid Created on:
	 * October 9, 2019
	 */
	public BigInteger validateTestId(String testId, List<Test> testList) throws ValidationException {
		
		if (testId.matches("^[0-9]+")) {
			Iterator<Test> testIterator = testList.iterator();
			while (testIterator.hasNext()) {
				Test test = testIterator.next();
				if (test.getTestId().compareTo(new BigInteger(testId)) == 0) {
					logger.info("Test Id is proper... returning BigInteger value of the entered Id..");
					return test.getTestId();
				}
			}
		}
		logger.error("Invalid Test Id... throwing ValidationException in UserService.validateTestId");
		throw new ValidationException(UserErrorMessage.userErrorInvalidTestId);
	}

	/*
	 * Author: Jayesh Gaur Description: validates the date and time input given by
	 * the user Created on: October 9, 2019
	 */
	public LocalDateTime validateDateTime(String dateString) throws ValidationException {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
		LocalDateTime userDateTime = null;
		LocalDate userDate = null;
		LocalTime userTime = null;
		LocalTime closeTime = LocalTime.parse("20:00", timeFormat);
		LocalTime openTime = LocalTime.parse("10:00", timeFormat);
		int userYear=0;
		int currentYear=0;
		try {
			logger.info("Parsing string data into LocalDateTime");
			userDateTime = LocalDateTime.parse(dateString, dateFormat);
			userYear = userDateTime.getYear();
			userDate = userDateTime.toLocalDate();
			currentYear = LocalDate.now().getYear();
			userTime = userDateTime.toLocalTime();
		} catch (Exception exception) {
			logger.error("Caught ParseException... wrong format of date and time. Throwing ValidationException in ");
			throw new ValidationException(UserErrorMessage.userErrorInvalidDateFormat);
		}
		LocalDate currentDate = LocalDate.now();
		if(userYear > currentYear+1) {
			throw new ValidationException("You can book an appointment only a year in advance.");
		}
		if (userDate.isBefore(currentDate)) {
			logger.error("User has entered a date in the past.. throwing ValidationException");
			throw new ValidationException(UserErrorMessage.userErrorPastDate);
		} else if (userTime.isAfter(closeTime) || userTime.isBefore(openTime)) {
			logger.error("The entered time is not within working hours... throwing ValidationException");
			throw new ValidationException(UserErrorMessage.userErrorNonWorkingHours);
		} else if (null == (userDateTime = LocalDateTime.parse(dateString, dateFormat))) {
			throw new ValidationException(UserErrorMessage.userErrorInvalidDateFormat);
		}
		logger.info("Correct date and time entered by user... returning the same in LocalDateTime format");
		return userDateTime;
	}

	/*
	 * Author: Jayesh Gaur Description: Validates the appointment Id selected by
	 * admin to approve Created on: October 9, 2019 Input/Output: Returns BigInteger
	 * value of the appointmentId if correct, else throws ValidationException
	 */
	public BigInteger validateAppointmentId(String appointmentId, List<Appointment> listOfAppointment)
			throws ValidationException {
		if (appointmentId.matches("^[0-9]+")) {
			Appointment appointment = null;
			Iterator<Appointment> appointmentListIterator = listOfAppointment.iterator();
			while (appointmentListIterator.hasNext()) {
				appointment = appointmentListIterator.next();
				if ((appointment.getAppointmentId().compareTo(new BigInteger(appointmentId)) == 0)
						&& (appointment.getAppointmentStatus() == 0)) {
					logger.info("Correct appointment id... returning BigInteger value of the appointment Id");
					return new BigInteger(appointmentId);
				}
			}
		}
		logger.error("Invalid Appointment Id.... throwing ValidationException");
		throw new ValidationException(UserErrorMessage.userErrorInvalidAppointmentId);
	}
	
	public BigInteger validateUserId(String userId) throws ValidationException {
		if (userId.matches("^[0-9]+")) {
			List<User> listOfUser = userRepository.findAll();
			Iterator<User> userIterator = listOfUser.iterator();
			while (userIterator.hasNext()) {
				User user = userIterator.next();
				if (user.getUserId().compareTo(new BigInteger(userId)) == 0) {
					return new BigInteger(userId);
				}

			}
		}
		throw new ValidationException(UserErrorMessage.userErrorInvalidUserId);
	}

	/*
	 * Author: Jayesh Gaur Description: Calls appointmentRepository to persist the
	 * new appointment object Created on: October 9, 2019
	 */
	public Appointment addAppointment(Appointment appointment) {
		logger.info("Calling repository save method to save the appointment in service layer..");
		return appointmentRepository.save(appointment);
	}

	/*
	 * Author: Jayesh Gaur Description: Accepts the appointment Id to be approved
	 * and marks it in the database Returns true on successful completion Created
	 * on: October 9, 2019
	 */
	@Override
	public boolean approveAppointment(BigInteger appointmentId) throws ValidationException {
		logger.info("Getting the appointment object corresponding to the appointment Id...");
		Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
		if (appointment.isPresent()) {
			logger.info("Appointment object found.. setting status to 1 (Approved)");
			Appointment appointmentObject= appointment.get();
			appointmentObject.setAppointmentStatus(1);
			logger.info("Appointment status UPDATED. AUDIT TRAIL=> Appointment Id: "+appointmentObject.getAppointmentId()+" Modified on: "+appointmentObject.getLastModifiedDate()+" Modified by: "+appointmentObject.getLastModifiedBy());
			logger.info("Sending confirmation email of approval to user..");
			sendEmail(appointmentObject);
		} else {
			logger.info("Manipulated appointment id... throwing ValidationException in service approve appointment");
			throw new ValidationException(UserErrorMessage.userErrorInvalidAppointmentId);
		}
		return true;
	}

	/*
	 * Author: Jayesh Gaur Description: Returns the center object corresponding to
	 * the center Id received in input Created on: October 9, 2019
	 */
	@Override
	public DiagnosticCenter findCenter(BigInteger centerId) {
		logger.info("Returning center object of the center id..");
		return centerRepository.findById(centerId).get();
	}

	/*
	 * Author: Jayesh Gaur Description: Returns the user object corresponding to the
	 * user Id received in input Created on: October 9, 2019
	 */
	@Override
	public User findUser(BigInteger userId) throws ValidationException {
	Optional<User> user = userRepository.findById(userId);
	if(user.isPresent()) {
		return user.get();
	}
	throw new ValidationException(UserErrorMessage.userErrorInvalidUserId);
	}
	
	/*
	 * Author: Jayesh Gaur Description: Returns the user object corresponding to the
	 * user Id received in input Created on: October 9, 2019
	 */
	public User findUser(String userEmail) throws ValidationException {
		Optional<User> user =  userRepository.findByUserEmail(userEmail);
		if(user.isPresent()) {
			return user.get();
		}
		throw new ValidationException(UserErrorMessage.userErrorInvalidEmailId);
	}

	public List<Appointment> getAppointments() {
		return appointmentRepository.findAll();
	}

	/*
	 * Author: Jayesh Gaur
	 * Description: sends mail to the user associated with the appointment in the parameter
	 * Created on: October 21, 2019
	 * Input: Appointment object
	 * Output: void
	 */
	void sendEmail(Appointment appointment) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(appointment.getUser().getUserEmail());
        msg.setSubject("Update on your appointment. ID: "+appointment.getAppointmentId());
        msg.setText("Hello, your appointment request has been approved! Please be on time.");
        javaMailSender.send(msg);
    }
	
	
	/*
	 * Author: JayeshGaur
	 * Description: returns user role of the user associated with the email in parameter
	 * Created on: October 23, 2019
	 * Input: User Email
	 * Output: user role
	 */
	public String findUserRole(String userEmail) {
		Optional<User> user = userRepository.findByUserEmail(userEmail);
		if(user.isPresent()) {
			return user.get().getUserRole();
		}
		else {
			return "ROLE_Customer";
		}
	}
	
	/*
	 * Author:  		Jayesh Gaur
	 * Description: 	Service function to call repository to change appointment status
	 * Created on: 		October 24, 2019
	 * Input: 			Appointment Id
	 * Output:			Boolean status of the operation
	 */
	public boolean rejectAppointment(BigInteger appointmentId) {
		Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
		if(appointment.isPresent()) {
			Appointment appointmentObject = appointment.get();
			appointmentObject.setAppointmentStatus(2);
			
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(appointmentObject.getUser().getUserEmail());
			msg.setSubject("Update on your appointment ID: "+appointmentObject.getAppointmentId());
			msg.setText("Sorry but your appointment application was rejected. Please select another date and time, and for more information, contact us by reverting on this email.");
			javaMailSender.send(msg);
			
			logger.info("Appointment status UPDATED(Rejected). AUDIT TRAIL=> Appointment Id: "+appointmentObject.getAppointmentId()+" Modified on: "+appointmentObject.getLastModifiedDate()+" Modified by: "+appointmentObject.getLastModifiedBy());
			return true;
		}
		else {
			return false;
		}
	}
}
