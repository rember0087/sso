package com.claridy.sso.service.moduleName.dao.hibernateimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.sso.service.moduleName.dao.ISSODAO;
import com.claridy.sso.service.moduleName.domain.SSORecord;
import com.claridy.sso.service.moduleName.domain.SSOUser;
import com.claridy.sso.service.moduleName.domain.WebEmployee;
import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;

@Repository
public class SSODAO extends BaseDAO implements ISSODAO {
	@Autowired
	public SSODAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	public SSORecord findSSORecordByGuukey(String guukey)
			throws DataAccessException {
		Criteria criteria = this.getSession().createCriteria(SSORecord.class);
		criteria.add(Restrictions.eq("guukey", guukey));
		return (SSORecord) criteria.uniqueResult();
	}

	public SSORecord findSSORecordByGuukeyAndtimeKey(String guukey,
			String timeKey) throws DataAccessException {
		Criteria criteria = this.getSession().createCriteria(SSORecord.class);
		criteria.add(Restrictions.eq("guukey", guukey));
		criteria.add(Restrictions.eq("timekey", timeKey));
		return (SSORecord) criteria.uniqueResult();
	}

	public SSOUser findSSOUserByUserid(String userid)
			throws DataAccessException {
		Criteria criteria = this.getSession().createCriteria(SSOUser.class);
		criteria.add(Restrictions.eq("userid", userid));
		return (SSOUser) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<SSORecord> getTimeOutSSORecord(Date dateBefore)
			throws DataAccessException {
		List<SSORecord> retSSORecordList = new ArrayList<SSORecord>();
		Criteria criteria = this.getSession().createCriteria(SSORecord.class);
		criteria.add(Restrictions.le("timeouttime", dateBefore));
		retSSORecordList = criteria.list();
		return retSSORecordList;
	}

	public WebEmployee getWebEmployeeByUserIDAndPWD(String user, String pwd)
			throws DataAccessException {
		Criteria criteria = this.getSession().createCriteria(WebEmployee.class);
		criteria.add(Restrictions.eq("employeeid", user));
		criteria.add(Restrictions.eq("pwd", pwd));
		return (WebEmployee) criteria.uniqueResult();
	}

}
