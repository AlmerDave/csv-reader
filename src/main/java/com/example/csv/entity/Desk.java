package com.example.csv.entity;

import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="MOSE_DESK")
public class Desk {
	
	@Id
	@Column(name="desk_code")
	private String deskCode;
	
	@Column(name="desk_desc")
	private String deskDesc;
	
	@Column(name="department")
	private String department;
	
	@Column(name="status")
	private String status;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name = "DATE_CREATED")
	private Date dateCreated;

	@Column(name = "DATE_MODIFIED")
	private Date dateModified;
	
	@Version
	@Column(name= "version")
    private Long version;
	
	public Desk() {
		super();
	}

	public Desk(String deskCode, String deskDesc, String department, String status, String createdBy, Date dateCreated,
			Date dateModified, Long version) {
		super();
		this.deskCode = deskCode;
		this.deskDesc = deskDesc;
		this.department = department;
		this.status = status;
		this.createdBy = "OMNIA";
		this.dateCreated = new Date();
		this.dateModified = new Date();
	}

	public String getDeskCode() {
		return deskCode;
	}

	public void setDeskCode(String deskCode) {
		this.deskCode = deskCode;
	}

	public String getDeskDesc() {
		return deskDesc;
	}

	public void setDeskDesc(String deskDesc) {
		this.deskDesc = deskDesc;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDateCreated() {
		return Optional.ofNullable(this.dateCreated).map(Date::getTime).map(Date::new).orElse(null);
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = Optional.ofNullable(dateCreated).map(Date::getTime).map(Date::new).orElse(null);
	}

	public Date getDateModified() {
		return Optional.ofNullable(this.dateModified).map(Date::getTime).map(Date::new).orElse(null);
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = Optional.ofNullable(dateModified).map(Date::getTime).map(Date::new).orElse(null);
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}



}
