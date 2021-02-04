package com.cg.healthcaresystemrest.dto;

public class AppointmentRequest {
	private String centerId;
	private String testId;
	private String userId;
	private String dateAndTime;
	
	public AppointmentRequest() {
	}

	public AppointmentRequest(String centerId, String testId, String userId, String dateAndTime) {
		super();
		this.centerId = centerId;
		this.testId = testId;
		this.userId = userId;
		this.dateAndTime = dateAndTime;
	}	
	

	public String getCenterId() {
		return centerId;
	}

	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
}
