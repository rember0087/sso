package com.claridy.common.mechanism.dao.hibernateimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.IOnlineCounterDAO;
import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;

@Repository
public class OnlineCounterDAO extends BaseDAO implements IOnlineCounterDAO {

	@Autowired
	public OnlineCounterDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	private static long online = 0;

	public static long getOnline() {

		return online;
	}

	public static void raise() {
		online++;

	}

	public static void reduce() {
		online--;
	}

}
