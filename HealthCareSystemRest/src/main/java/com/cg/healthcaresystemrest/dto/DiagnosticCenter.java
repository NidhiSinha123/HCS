
package com.cg.healthcaresystemrest.dto;
/*
 * Author: 			Jayesh Gaur
 * Description: 	DTO class for DiagnosticCenter Entity
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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hcs_center")
public class DiagnosticCenter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "center_id")
	private BigInteger centerId;

	@NotBlank(message="centerName cannot be empty")
	@Size(max = 50, message = "Center name cannot be this big. Max 50 characters allowed.")
	@Column(name = "center_name")
	private String centerName;

	@NotNull(message = "Phone number cannot be empty")
	@Min(value = 1000000000, message = "Should not be less than 10 digits")
	@Digits(integer = 10, message = "Phone number cannot be more than 10 characters", fraction = 0)
	@Column(name = "center_contact_no")
	private BigInteger centerContactNo;

	@NotBlank(message="Center address cannot be blank")
	@Column(name = "center_address")
	private String centerAddress;
	
	@JsonIgnore
	@OneToMany
	@JoinColumn(name="center_id_fk")
	private List<Test> listOfTests = new ArrayList<Test>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "center")
	List<Appointment> userAppointmentList = new ArrayList<Appointment>();
	
	@Column(name="is_deleted")
	private boolean isDeleted;
	
	
	@CreatedBy
	protected String createdBy;
	
	@CreatedDate	
	@Temporal(TemporalType.TIMESTAMP)
	protected Date creationDate;
	
	@LastModifiedBy
	protected String lastModifiedBy;
	
	@LastModifiedDate
	protected String lastModifiedDate;
	

	public DiagnosticCenter() {
	}

	public DiagnosticCenter(String centerName, String centerAddress, BigInteger centerContactNo) {
		super();
		this.centerName = centerName;
		this.centerContactNo = centerContactNo;
		this.centerAddress = centerAddress;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public BigInteger getCenterId() {
		return centerId;
	}

	public void setCenterId(BigInteger centerId) {
		this.centerId = centerId;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public BigInteger getCenterContactNo() {
		return centerContactNo;
	}

	public void setCenterContactNo(BigInteger centerContactNo) {
		this.centerContactNo = centerContactNo;
	}

	public String getCenterAddress() {
		return centerAddress;
	}

	public void setCenterAddress(String centerAddress) {
		this.centerAddress = centerAddress;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((centerAddress == null) ? 0 : centerAddress.hashCode());
		result = prime * result + ((centerContactNo == null) ? 0 : centerContactNo.hashCode());
		result = prime * result + ((centerId == null) ? 0 : centerId.hashCode());
		result = prime * result + ((centerName == null) ? 0 : centerName.hashCode());
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
		DiagnosticCenter other = (DiagnosticCenter) obj;
		if (centerAddress == null) {
			if (other.centerAddress != null)
				return false;
		} else if (!centerAddress.equals(other.centerAddress))
			return false;
		if (centerContactNo == null) {
			if (other.centerContactNo != null)
				return false;
		} else if (!centerContactNo.equals(other.centerContactNo))
			return false;
		if (centerId == null) {
			if (other.centerId != null)
				return false;
		} else if (!centerId.equals(other.centerId))
			return false;
		if (centerName == null) {
			if (other.centerName != null)
				return false;
		} else if (!centerName.equals(other.centerName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DiagnosticCenter [centerId=" + centerId + ", centerName=" + centerName + ", centerContactNo="
				+ centerContactNo + ", centerAddress=" + centerAddress + "]";
	}

	public List<Test> getListOfTests() {
		return listOfTests;
	}

	public void setListOfTests(List<Test> listOfTests) {
		this.listOfTests = listOfTests;
	}

	public List<Appointment> getUserAppointmentList() {
		return userAppointmentList;
	}

	public void setUserAppointmentList(List<Appointment> userAppointmentList) {
		this.userAppointmentList = userAppointmentList;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}