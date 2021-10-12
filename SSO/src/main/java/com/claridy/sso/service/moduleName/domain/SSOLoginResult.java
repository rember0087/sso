package com.claridy.sso.service.moduleName.domain;

import java.io.Serializable;

/**
 * 
 * @author RemberSu
 * @version 1.1
 * 
 */
public class SSOLoginResult implements Serializable {
	private static final long serialVersionUID = -840053921595872334L;
	private String status;
	private String guuKey;
	private String timeKey;
	private String userId;
	private String userName;
	private String userPwd;
	private String msg;

	public String getGuuKey() {
		return guuKey;
	}

	public void setGuuKey(String guuKey) {
		this.guuKey = guuKey;
	}

	public String getTimeKey() {
		return timeKey;
	}

	public void setTimeKey(String timeKey) {
		this.timeKey = timeKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the userPwd
	 */
	public String getUserPwd() {
		return userPwd;
	}

	/**
	 * @param userPwd the userPwd to set
	 */
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

}
