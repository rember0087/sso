package com.claridy.common.mechanism.domain;

import java.io.Serializable;

import org.springframework.stereotype.Component;
@Component
public class Sys_User implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 認證是否成功；成功為ok，失敗為failure */
	private String loginStatus;
	/** 讀者帳號 */
	private String userID;
	/** 讀者姓名 */
	private String cardNumber;
	/** 讀者卡號 */
	private String userName;
	/** 讀者手機 */
	private String phoneNumber;
	/** 讀者Email */
	private String eMail;
	/** 讀者生日（格式：YYYY/MM/DD） */
	private String borthDay;
	/** 讀者密碼（此密碼為傳入值） */
	private String passWord;
	/** 讀者身份類別 */
	private String identity;
	/** 讀者身份類別 */
	private String loginMsg;

	/**
	 * @return the loginStatus
	 */
	public String getLoginStatus() {
		return loginStatus;
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @return the eMail
	 */
	public String geteMail() {
		return eMail;
	}

	/**
	 * @return the borthDay
	 */
	public String getBorthDay() {
		return borthDay;
	}

	/**
	 * @return the passWord
	 */
	public String getPassWord() {
		return passWord;
	}

	/**
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * @param loginStatus
	 *            the loginStatus to set
	 */
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @param cardNumber
	 *            the cardNumber to set
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @param eMail
	 *            the eMail to set
	 */
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	/**
	 * @param borthDay
	 *            the borthDay to set
	 */
	public void setBorthDay(String borthDay) {
		this.borthDay = borthDay;
	}

	/**
	 * @param passWord
	 *            the passWord to set
	 */
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/**
	 * @param identity
	 *            the identity to set
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * @return the loginMsg
	 */
	public String getLoginMsg() {
		return loginMsg;
	}

	/**
	 * @param loginMsg
	 *            the loginMsg to set
	 */
	public void setLoginMsg(String loginMsg) {
		this.loginMsg = loginMsg;
	}

}
