package com.claridy.sso.service.moduleName.dao;

import java.util.Date;
import java.util.List;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.sso.service.moduleName.domain.SSORecord;
import com.claridy.sso.service.moduleName.domain.SSOUser;
import com.claridy.sso.service.moduleName.domain.WebEmployee;

public interface ISSODAO extends IBaseDAO {
	public SSORecord findSSORecordByGuukey(String guukey)
			throws DataAccessException;

	public SSORecord findSSORecordByGuukeyAndtimeKey(String guukey,
			String timeKey) throws DataAccessException;
	
	public List<SSORecord> getTimeOutSSORecord(Date dateBefore)
			throws DataAccessException;
	
	public SSOUser findSSOUserByUserid(String userid) throws DataAccessException;
	
	public WebEmployee getWebEmployeeByUserIDAndPWD(String user, String pwd)
			throws DataAccessException ;
}
