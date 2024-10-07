package com.example.csv.entity;

import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="MOSE_BRANCH")
public class Branch {

	@Id
	@Column(name="branch_code")
	private String branchCode;
	
	@Column(name="branch_name")
	private String branchName;
	
	@Column(name="desk_code")
	private String deskCode;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name = "DATE_CREATED")
	private Date dateCreated;

	@Column(name = "DATE_MODIFIED")
	private Date dateModified;
	
	@Version
	@Column(name= "version")
    private Long version;

	public Branch() {
		super();
	}
	
	public Branch(String branchCode, String branchName, String deskCode, String createdBy) {
		super();
		this.branchCode = branchCode;
		this.branchName = branchName;
		this.deskCode = deskCode;
		this.createdBy = "OMNIA";
		this.dateCreated = new Date();
		this.dateModified = new Date();
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getDeskCode() {
		return deskCode;
	}

	public void setDeskCode(String deskCode) {
		this.deskCode = deskCode;
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
	
	
	
}
