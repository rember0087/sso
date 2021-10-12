package com.claridy.common.mechanism.dao.hibernateimpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.claridy.common.mechanism.dao.ISys_ParamDAO;
import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.domain.Sys_Param;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class Sys_ParamDAO extends BaseDAO implements ISys_ParamDAO
{ 
	@Autowired  
	public Sys_ParamDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<Sys_Param> findAll() throws DataAccessException{
		return (List<Sys_Param>) super.findByCriteria( DetachedCriteria.forClass(Sys_Param.class));
	} 
	
	public Sys_Param find(String func_no,String parent) throws DataAccessException{
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" FROM " + Sys_Param.class.getSimpleName());
		sbHql.append(" WHERE func_no = ? and parent = ? ");

		Query query = this.getSession().createQuery(sbHql.toString());
		query.setParameter(0, func_no);
		query.setParameter(1, parent);

		return (Sys_Param) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Sys_Param> findByCategory(String category) throws DataAccessException{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Sys_Param.class);
		detachedCriteria.add(Restrictions.eq("category", category));
		detachedCriteria.add(Restrictions.ne("parent", "0"));
		detachedCriteria.addOrder(Order.asc("seq"));
		return (List<Sys_Param>) super.findByCriteria(detachedCriteria);
	}
	
	public Map<String ,String> getMapByCategory(String category) throws DataAccessException{
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<Sys_Param> list = this.findByCategory(category);
		for (Sys_Param Sys_Param : list) {
			map.put(Sys_Param.getFunc_value(), Sys_Param.getFunc_name());
		}
		return map;
	}
}
