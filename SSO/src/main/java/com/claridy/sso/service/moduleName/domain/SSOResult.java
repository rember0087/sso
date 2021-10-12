package com.claridy.sso.service.moduleName.domain;

import java.io.Serializable;

/**
 * 
 * @author RemberSu
 * @version 1.0
 * 
 */
public class SSOResult implements Serializable {
	private static final long serialVersionUID = -8037076462070010013L;
	private String status;
	private String userid;
	private String username;
	private String userpwd;
	private String timeKey;
	private String msg;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the timeKey
	 */
	public String getTimeKey() {
		return timeKey;
	}

	/**
	 * @param timeKey
	 *            the timeKey to set
	 */
	public void setTimeKey(String timeKey) {
		this.timeKey = timeKey;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
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
