package com.claridy.sso.service.moduleName.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author RemberSu
 * @version 1.0
 * 
 */
@Entity
@Table(name = "WEBEMPLOYEE")
public class WebEmployee implements Serializable {

	private static final long serialVersionUID = -6656362943962019537L;
	private String uuid;
	private String employeeid;
	private String employeename;
	private String pwd;

	/**
	 * @return the uuid
	 */
	@Id
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid
	 *            the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the employeeid
	 */
	public String getEmployeeid() {
		return employeeid;
	}

	/**
	 * @param employeeid
	 *            the employeeid to set
	 */
	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}

	/**
	 * @return the employeename
	 */
	public String getEmployeename() {
		return employeename;
	}

	/**
	 * @param employeename
	 *            the employeename to set
	 */
	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @param pwd
	 *            the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
