package com.claridy.common.mechanism.dao.hibernateimpl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.domain.Sys_Pagination;

/**
 * 
 * @author Chapel Lee
 */
public class BaseDAO extends HibernateDaoSupport {

	/**
	 * <p>
	 * Log instance for this class.
	 * </p>
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Get PO by id
	 * 
	 * @param clazz
	 * @param id
	 * @param lock
	 * @return Object
	 * @throws DataAccessException
	 */
	public Object get(Class<?> clazz, Serializable id, boolean lock)
			throws DataAccessException {

		try {
			if (lock) {
				// SELECT ... FOR UPDATE
				return getHibernateTemplate().load(clazz, id,
						LockMode.PESSIMISTIC_WRITE);
			} else {
				return getHibernateTemplate().load(clazz, id);
			}
		} catch (Exception e) {
			throw new DataAccessException("Get entity failed:", e);
		}

	}

	// ********************************************************************* //

	public List<?> findByHQL(String strHQL) {

		Session session = this.getSession();
		Query query = session.createQuery(strHQL);
		return query.list();
	}
	
	public void updateByHQL(String strHQL) {

		Session session = this.getSession();
		Query query = null;
		if(strHQL.toLowerCase().indexOf("insert into ") != -1) {
			query = session.createSQLQuery(strHQL);
		} else {
			query = session.createQuery(strHQL);
		}
		query.executeUpdate();
		
	}
	
	// ********************************************************************* //

	public Sys_Pagination findByHQL(String strHQL, int currentPage, int pageSize) {
		Sys_Pagination Sys_Pagination = new Sys_Pagination();
		
		Session session = this.getSession();
		
		Query query = session.createQuery(strHQL);		
		
		currentPage++;
		if(pageSize <= 0) {
			pageSize = 10;
		}
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);		
		
		//查詢資料
		List<?> list = query.list();
		Sys_Pagination.setAaData(list);
				
		//查詢筆數
		int rowCount = this.getRowCount(strHQL);
		Sys_Pagination.setRowCount(rowCount);
		Sys_Pagination.setiTotalRecords(rowCount);
		Sys_Pagination.setiTotalDisplayRecords(rowCount);
		
		//查詢頁數	
		if (rowCount == 0) {
			Sys_Pagination.setPageCount(1);
		}else {
			Sys_Pagination.setPageCount(rowCount % pageSize == 0 ? rowCount / pageSize : rowCount / pageSize + 1);
		}
		
		return Sys_Pagination;
	}

	// ********************************************************************* //

	public List<?> findByCriteria(DetachedCriteria detachedCriteria) {

		Criteria criteria = detachedCriteria.getExecutableCriteria(this
				.getSession());
		
		return criteria.list();
	}

	// ********************************************************************* //

	public Sys_Pagination findByCriteria(DetachedCriteria detachedCriteria, int currentPage, int pageSize) {
		Sys_Pagination Sys_Pagination = new Sys_Pagination();
		
		Criteria criteria = detachedCriteria.getExecutableCriteria(this
				.getSession());

		currentPage++;
		if(pageSize <= 0) {
			pageSize = 10;
		}
		criteria.setFirstResult((currentPage - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		
		//查詢資料
		List<?> list = criteria.list();
		Sys_Pagination.setAaData(list);
				
		//查詢筆數
		criteria.setFirstResult(0);
		int rowCount = this.getRowCount(detachedCriteria);
		Sys_Pagination.setRowCount(rowCount);
		Sys_Pagination.setiTotalRecords(rowCount);
		Sys_Pagination.setiTotalDisplayRecords(rowCount);
		
		//查詢頁數		
		if (rowCount == 0) {
			Sys_Pagination.setPageCount(1);
		}else {
			Sys_Pagination.setPageCount(rowCount % pageSize == 0 ? rowCount / pageSize : rowCount / pageSize + 1);			
		}

		return Sys_Pagination;
	}
	
	// ********************************************************************* //

	public int getRowCount(String strHQL) throws DataAccessException {

		if(strHQL.toLowerCase().startsWith("select ")) {
			strHQL = "select count(*) ".concat( strHQL.substring(strHQL.toLowerCase().indexOf(" from")) );
		} else {
			strHQL = "select count(*) ".concat(strHQL);
		}
		
		Session session = this.getSession();
		Query query = session.createQuery(strHQL);
		
		return Integer.parseInt( query.list().get(0).toString() );
	}
	
	// ********************************************************************* //

	public int getRowCount(DetachedCriteria detachedCriteria) throws DataAccessException {
		Criteria criteria = detachedCriteria.getExecutableCriteria(this
				.getSession());

		criteria.setProjection(Projections.rowCount());

		return ((Long)criteria.list().get(0)).intValue();
	}

	// ********************************************************************* //

	/**
	 * @param obj
	 * @throws DataAccessException
	 * 
	 */
	public void create(Object obj) throws DataAccessException {

		try {

			getHibernateTemplate().save(obj);
		} catch (Exception e) {
			throw new DataAccessException("Create entity failed:", e);
		}
	}

	// ********************************************************************* //

	/**
	 * @param obj
	 * @throws DataAccessException
	 */
	public void update(Object obj) throws DataAccessException {

		try {

			getHibernateTemplate().update(obj);
		} catch (Exception e) {
			throw new DataAccessException("Update entity failed:", e);
		}
	}

	// ********************************************************************* //

	/**
	 * @param obj
	 * @throws DataAccessException
	 */
	public void saveOrUpdate(Object obj) throws DataAccessException {

		try {

			getHibernateTemplate().saveOrUpdate(obj);
		} catch (Exception e) {
			throw new DataAccessException("SaveOrUpdate entity failed:", e);
		}
	}
	
	/**
	 * @param obj
	 * @throws DataAccessException
	 */
	public void merge(Object obj) throws DataAccessException {

		try {

			getHibernateTemplate().merge(obj);
		} catch (Exception e) {
			throw new DataAccessException("SaveOrUpdate entity failed:", e);
		}
	}

	// ********************************************************************* //

	/**
	 * @param obj
	 * @throws DataAccessException
	 */
	public void delete(Object obj) throws DataAccessException {

		try {
			getHibernateTemplate().delete(obj);
		} catch (Exception e) {
			throw new DataAccessException("Delete entity failed:", e);
		}
	}

	// ********************************************************************* //

	/**
	 * @param clazz
	 * @param id
	 * @throws DataAccessException
	 */
	public void delete(Class<?> clazz, Serializable id)
			throws DataAccessException {

		try {

			getHibernateTemplate().delete(
					getHibernateTemplate().load(clazz, id));
		} catch (Exception e) {
			throw new DataAccessException("Delete entity failed ", e);
		}
	}

	// ********************************************************************* //
	/**
     * 
     */
	public int getTotalRecoredRsize(Query query) {

		ScrollableResults sr = query.scroll(ScrollMode.SCROLL_SENSITIVE);
		sr.last();
		return sr.getRowNumber() + 1;
	}

	// ********************************************************************* //

	/**
	 * @param object
	 * @param properties
	 * @throws DataAccessException
	 */
	public void initialize(Object object, String[] properties)
			throws DataAccessException {

		try {
			for (int i = 0; i < properties.length; i++) {
				Object property = PropertyUtils.getSimpleProperty(object,
						properties[i]);
				Hibernate.initialize(property);
			}
		} catch (Exception e) {
			throw new DataAccessException("", e);
		}

	}

	public int calculateFirstRowNum(int pageNo, int pageSize) {
		if (log.isDebugEnabled()) {
			log.debug("input para: pageNo=" + pageNo + ", pageSize=" + pageSize);
		}
		if (pageNo < 1) {
			log.error("pageNo[" + pageNo
					+ "] must be integer, using 1 as the default pageNo.");
			pageNo = 1;
		}
		int first = 0;
		switch (pageNo) {
		case 1:
			first = 0;
			break;
		default:
			first = pageSize * (pageNo - 1);
			break;
		}
		if (log.isDebugEnabled()) {
			log.debug("calculate first row num=" + first);
		}

		return first;
	}

}
