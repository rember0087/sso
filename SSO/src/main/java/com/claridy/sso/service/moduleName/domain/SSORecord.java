package com.claridy.sso.service.moduleName.domain;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "ssorecord")
public class SSORecord implements Serializable {
	private static final long serialVersionUID = -6780563748492662219L;
	private String guukey;
	private String timekey;
	private String userid;
	private String username;
	private String userpwd;
	private Date logintime;
	private Date timeouttime;

	/**
	 * @return the guukey
	 */
	@Id
	public String getGuukey() {
		return guukey;
	}

	/**
	 * @param guukey
	 *            the guukey to set
	 */
	public void setGuukey(String guukey) {
		this.guukey = guukey;
	}

	/**
	 * @return the timekey
	 */
	public String getTimekey() {
		return timekey;
	}

	/**
	 * @param timekey
	 *            the timekey to set
	 */
	public void setTimekey(String timekey) {
		this.timekey = timekey;
	}

	/**
	 * @return the userid
	 */
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
	 * @return the logintime
	 */
	public Date getLogintime() {
		return logintime;
	}

	/**
	 * @param logintime the logintime to set
	 */
	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}

	/**
	 * @return the timeouttime
	 */
	public Date getTimeouttime() {
		return timeouttime;
	}

	/**
	 * @param timeouttime the timeouttime to set
	 */
	public void setTimeouttime(Date timeouttime) {
		this.timeouttime = timeouttime;
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
