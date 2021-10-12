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
@Table(name = "ssouser")
public class SSOUser implements Serializable {
	private static final long serialVersionUID = -7666974011398088548L;
	private String userid;
	private String usersex;
	private String username;
	private String userpwd;
	private String libcode;
	private String libname;

	/**
	 * @return the userid
	 */
	@Id
	public String getUserid() {
		return userid;
	}

	/**
	 * @param userid
	 *            the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return the usersex
	 */
	public String getUsersex() {
		return usersex;
	}

	/**
	 * @param usersex
	 *            the usersex to set
	 */
	public void setUsersex(String usersex) {
		this.usersex = usersex;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the libcode
	 */
	public String getLibcode() {
		return libcode;
	}

	/**
	 * @param libcode
	 *            the libcode to set
	 */
	public void setLibcode(String libcode) {
		this.libcode = libcode;
	}

	/**
	 * @return the libname
	 */
	public String getLibname() {
		return libname;
	}

	/**
	 * @param libname
	 *            the libname to set
	 */
	public void setLibname(String libname) {
		this.libname = libname;
	}

	/**
	 * @return the userpwd
	 */
	public String getUserpwd() {
		return userpwd;
	}

	/**
	 * @param userpwd the userpwd to set
	 */
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

}
