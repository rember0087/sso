package com.claridy.common.mechanism.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_PARAM")
public class Sys_Param implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * This attribute maps to the column FUNC_NO in the SYS_PARAM table.
	 */
	@Id
	protected String func_no;

	/** 
	 * This attribute maps to the column FUNC_NAME in the SYS_PARAM table.
	 */
	protected String func_name;

	/** 
	 * This attribute maps to the column PARENT in the SYS_PARAM table.
	 */
	@Id
	protected String parent;

	/** 
	 * This attribute maps to the column FUNC_VALUE in the SYS_PARAM table.
	 */
	protected String func_value;

	/** 
	 * This attribute maps to the column CATEGORY in the SYS_PARAM table.
	 */
	protected String category;

	/** 
	 * This attribute maps to the column CREATOR in the SYS_PARAM table.
	 */
	protected String creator;

	/** 
	 * This attribute maps to the column CREATED_DATE in the SYS_PARAM table.
	 */
	protected Date created_date;

	/** 
	 * This attribute maps to the column LAST_UPDATE in the SYS_PARAM table.
	 */
	protected String last_update;

	/** 
	 * This attribute maps to the column LAST_UPDATE_DATE in the SYS_PARAM table.
	 */
	protected Date last_update_date;

	/** 
	 * This attribute maps to the column CATEGORY2 in the SYS_PARAM table.
	 */
	protected String category2;

	/** 
	 * This attribute maps to the column CATEGORY3 in the SYS_PARAM table.
	 */
	protected String category3;

	/** 
	 * This attribute maps to the column REMARK in the SYS_PARAM table.
	 */
	protected String remark;

	/** 
	 * This attribute maps to the column LOCALE in the SYS_PARAM table.
	 */
	protected String locale;

	/** 
	 * This attribute maps to the column SEQ in the SYS_PARAM table.
	 */
	protected String seq;

	public String getFunc_no() {
		return func_no;
	}

	public void setFunc_no(String func_no) {
		this.func_no = func_no;
	}

	public String getFunc_name() {
		return func_name;
	}

	public void setFunc_name(String func_name) {
		this.func_name = func_name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getFunc_value() {
		return func_value;
	}

	public void setFunc_value(String func_value) {
		this.func_value = func_value;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public String getLast_update() {
		return last_update;
	}

	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}

	public Date getLast_update_date() {
		return last_update_date;
	}

	public void setLast_update_date(Date last_update_date) {
		this.last_update_date = last_update_date;
	}

	public String getCategory2() {
		return category2;
	}

	public void setCategory2(String category2) {
		this.category2 = category2;
	}

	public String getCategory3() {
		return category3;
	}

	public void setCategory3(String category3) {
		this.category3 = category3;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	
	
	
}
