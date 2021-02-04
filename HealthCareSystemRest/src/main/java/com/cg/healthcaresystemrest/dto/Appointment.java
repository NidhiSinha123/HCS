package com.cg.healthcaresystemrest.dto;
/*
 * Author: 			Jayesh Gaur
 * Description: 	DTO class for Appointment Entity
 * Created on: 		October 12, 2019
 */
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="hcs_appointment")
public class Appointment {

	@Id @GeneratedValue
	@Column(name="appointment_id")
	private BigInteger appointmentId;

	@ManyToOne
	@JoinColumn(name="center_id_fk")
	private DiagnosticCenter center;  	
	
	@ManyToOne
	@JoinColumn(name="test_id_fk")
	private Test test;
	
	@ManyToOne
	@JoinColumn(name="user_id_fk")
	private User user;
	
	@Column(name="appointment_status")
	private int appointmentStatus;
	
	//@DateTimeFormat(pattern="MM/dd/yyyy hh:mm a")
	@Column(name="appointment_date_time")
	private LocalDateTime dateTime;
	
	@CreatedBy
	protected String createdBy;
	
	@CreatedDate	
	@Temporal(TemporalType.TIMESTAMP)
	protected Date creationDate;
	
	@LastModifiedBy
	protected String lastModifiedBy;
	
	@LastModifiedDate
	protected String lastModifiedDate;
	
	public Appointment() {

	}
	
	public Appointment(BigInteger appointmentId, DiagnosticCenter center, Test test, User user, int appointmentStatus,
			LocalDateTime dateTime) {
		super();
		this.appointmentId = appointmentId;
		this.center = center;
		this.test = test;
		this.user = user;
		this.appointmentStatus = appointmentStatus;
		this.dateTime = dateTime;
	}

	public DiagnosticCenter getCenter() {
		return center;
	}

	public void setCenter(DiagnosticCenter center) {
		this.center = center;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(int appointmentstatus) {
		this.appointmentStatus = appointmentstatus;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public BigInteger getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(BigInteger appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	
}
