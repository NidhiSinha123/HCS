package com.cg.healthcaresystemrest.dto;
/*
 * Author: 			Jayesh Gaur
 * Description: 	DTO class for User Entity
 * Created on: 		October 12, 2019
 */
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hcs_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private BigInteger userId;

	// @JsonIgnore
//	@Size(min = 8, message = "Passwould length should be greater than or equal to 8 characters")
//	@Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})", message = "Password must have an uppercase, a lower case, a number and a special character")
//	@Column(name = "user_password")
	private String userPassword;

	@Pattern(regexp = "^[A-Z].*", message = "Name must start with a capital letter")
	@Column(name = "user_name")
	private String userName;

	@NotNull(message = "Phone number cannot be empty")
	@Min(value = 1000000000, message = "Should not be less than 10 digits")
	@Digits(integer = 10, message = "Phone number cannot be more than 10 characters", fraction = 0)
	@Column(name = "user_contact_no")
	private BigInteger contactNo;

//	@Transient //Because default value is Customer, which will be set by the database automatically, so we don't need to map this.
	@Column(name = "user_role")
	private String userRole;

	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Enter a valid email id! Example: abc@gmail.com")
	@Column(name = "user_email")
	private String userEmail;

	@NotNull(message = "Age cannot be empty")
	@Min(value = 15)
	@Column(name = "user_age")
	private Integer age;

	@Column(name = "user_gender")
	private String gender;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Appointment> userAppointmentList = new ArrayList<Appointment>();

	@CreatedBy
	protected String createdBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	protected Date creationDate;

	@LastModifiedBy
	protected String lastModifiedBy;

	@LastModifiedDate
	protected String lastModifiedDate;

	public User() {

	}

	public User(String userPassword, String userName, BigInteger contactNo, String userEmail, Integer age,
			String gender) {
		super();
		this.userPassword = userPassword;
		this.userName = userName;
		this.contactNo = contactNo;
		this.userEmail = userEmail;
		this.age = age;
		this.gender = gender;
		this.userRole = "ROLE_Customer";
	}

	// UserId
	public BigInteger getUserId() {
		return userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}

	// UserPassword
	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	// User Name
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	// ContactNumber
	public BigInteger getContactNo() {
		return contactNo;
	}

	public void setContactNo(BigInteger contactNo) {
		this.contactNo = contactNo;
	}

	// UserRole
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = "ROLE_Customer";
	}

	// User Email
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	// User Age
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	// User Gender
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((contactNo == null) ? 0 : contactNo.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userPassword == null) ? 0 : userPassword.hashCode());
		result = prime * result + ((userRole == null) ? 0 : userRole.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (contactNo == null) {
			if (other.contactNo != null)
				return false;
		} else if (!contactNo.equals(other.contactNo))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userPassword == null) {
			if (other.userPassword != null)
				return false;
		} else if (!userPassword.equals(other.userPassword))
			return false;
		if (userRole == null) {
			if (other.userRole != null)
				return false;
		} else if (!userRole.equals(other.userRole))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userPassword=" + userPassword + ", userName=" + userName + ", contactNo="
				+ contactNo + ", userRole=" + userRole + ", userEmail=" + userEmail + ", age=" + age + ", gender="
				+ gender + "]";
	}

	public List<Appointment> getUserAppointmentList() {
		return userAppointmentList;
	}

	public void setUserAppointmentList(List<Appointment> userAppointmentList) {
		this.userAppointmentList = userAppointmentList;
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

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
