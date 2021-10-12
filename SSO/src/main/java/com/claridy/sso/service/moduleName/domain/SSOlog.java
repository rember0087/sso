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
@Table(name = "ssolog")
public class SSOlog implements Serializable {
	private static final long serialVersionUID = 2987029211516987120L;
	private String uuid;
	private String guukey;
	private String userid;
	private String userip;
	private String useractive;
	private Date activetime;

	@Id
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getGuukey() {
		return guukey;
	}

	public void setGuukey(String guukey) {
		this.guukey = guukey;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserip() {
		return userip;
	}

	public void setUserip(String userip) {
		this.userip = userip;
	}

	public String getUseractive() {
		return useractive;
	}

	public void setUseractive(String useractive) {
		this.useractive = useractive;
	}

	public Date getActivetime() {
		return activetime;
	}

	public void setActivetime(Date activetime) {
		this.activetime = activetime;
	}

}
