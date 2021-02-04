package com.cg.healthcaresystemrest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.List;

/*
 * Author: 		Jayesh Gaur
 * Description: Test Cases for Rest Controller functions
 * Created on: 	October 15, 2019
 */

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.cg.healthcaresystemrest.dto.Appointment;
import com.cg.healthcaresystemrest.dto.DiagnosticCenter;
import com.cg.healthcaresystemrest.dto.Test;
import com.cg.healthcaresystemrest.dto.User;
import com.cg.healthcaresystemrest.exception.ExistingCredentialException;
import com.cg.healthcaresystemrest.exception.ValidationException;
import com.cg.healthcaresystemrest.repository.AppointmentRepository;
import com.cg.healthcaresystemrest.repository.CenterRepository;
import com.cg.healthcaresystemrest.repository.TestRepository;
import com.cg.healthcaresystemrest.repository.UserRepository;
import com.cg.healthcaresystemrest.service.UserService;
import com.cg.healthcaresystemrest.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HealthCareSystemRestApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CenterRepository centerrepository;
	
	@Autowired
	AppointmentRepository appointmentrepository;
	
	@Autowired
	TestRepository testrepository;
	
	@Autowired
	UserRepository userrepository;
	
	
	DiagnosticCenter center=new DiagnosticCenter();
	Test test=new Test();
	Appointment appointment=new Appointment();
	User user=new User();
	
	/*
	 * Author:  	Jayesh Gaur
	 * Description: Test case to check register controller.
	 * Input: 		User object properties
	 * Output: 		User object
	 * Created on: 	October 15, 2019
	 */
	
	@org.junit.Test
	public void registerUser() {
		User user = restTemplate.getForObject("/register", User.class);
		assertThat(user);
	}
	
	
	/*
	 * Author:  	Jayesh Gaur
	 * Description: Test case to check add appointment controller.
	 * Input: 		Appointment object properties
	 * Output: 		Appointment object
	 * Created on: 	October 15, 2019
	 */
	@org.junit.Test
	public void addAppointment() {
		Appointment appointment = restTemplate.getForObject("/addAppointment", Appointment.class);
		assertThat(appointment);
	}
	
	
	@org.junit.Test
	public void deleteCenter()
	{
		DiagnosticCenter center=restTemplate.getForObject("/removeCenter", DiagnosticCenter.class);
		assertThat(center);
	}
	
	@org.junit.Test
	public void addCenter()
	{
		DiagnosticCenter center=restTemplate.getForObject("/addCenter",DiagnosticCenter.class);
		assertThat(center);
	}
	
	@org.junit.Test
	public void addTest()
	{
		Test test=restTemplate.getForObject("/addTest",Test.class);
		assertThat(test);
	}
	
	@org.junit.Test
	public void deleteTest()
	{
		Test test=restTemplate.getForObject("/removeTest",Test.class);
		assertThat(test);
	}
	
	
	@org.junit.Test
	public void testAddCenter()
	{
		center.setDeleted(false);
		//center.setCenterId(BigInteger.valueOf(500));
		center.setCenterName("center1");
		center.setCenterAddress("powai");
		center.setCenterContactNo(BigInteger.valueOf(1010101010));
		assertEquals(center, userService.addCenter(center));
	}
	
	@org.junit.Test
	public void testAddTest()
	{
		//test.setTestId(BigInteger.valueOf(400));
		test.setDeleted(false);
		test.setTestName("abc");
		assertEquals(test,userService.addTest(BigInteger.valueOf(38), test));
	}
	
	@org.junit.Test
	public void testRemoveCenter() throws ValidationException
	{
		assertEquals(true, userService.removeCenter(BigInteger.valueOf(38)));
	}
	
	@org.junit.Test
	public void testRemoveTest() throws ValidationException
	{
		assertEquals(true, userService.removeTest(BigInteger.valueOf(38),BigInteger.valueOf(65)));
	}
	

	
	@org.junit.Test
	public void testAddAppointment()
	{
		appointment.setAppointmentStatus(1);
	     assertEquals(appointment,userService.addAppointment(appointment));
	}
	
	@org.junit.Test
	public void testGetAppointmentList()
	{
		User user=new User();
		user.setUserId(BigInteger.valueOf(43));
	    assertEquals(appointmentrepository.findByUser(user),userService.getAppointmentList(BigInteger.valueOf(43)));
	}
	
	@org.junit.Test
	public void testFindTest()
	{
		test.setTestId(BigInteger.valueOf(80));
		test.setTestName("abc");
		assertEquals(test,userService.findTest(BigInteger.valueOf(80)));
		
	}
	
	@org.junit.Test
	public void testFindCenter()
	{
		center.setCenterId(BigInteger.valueOf(61));
		center.setCenterName("center1");
		center.setCenterContactNo(BigInteger.valueOf(1010101010));
		center.setCenterAddress("powai");
		assertEquals(center,userService.findCenter(BigInteger.valueOf(61)));
	}
	
	
	@org.junit.Test
	public void testUserLogin()
	{
		assertEquals(BigInteger.valueOf(110),userService.userLogin("kaha@gmail.com","Karish@123"));
	}
	
	
	@org.junit.Test
	public void testFindUserRole()
	{
		assertEquals("ROLE_Customer",userService.findUserRole("kaha@gmail.com"));
	}
	@org.junit.Test
	public void testRejectAppointment()
	{
		assertEquals(true,userService.rejectAppointment(BigInteger.valueOf(53)));
	}
	
	@org.junit.Test
	public void testGetAppointments()
	{
		assertEquals(appointmentrepository.findAll().size(),userService.getAppointments().size());
	}
	
}
