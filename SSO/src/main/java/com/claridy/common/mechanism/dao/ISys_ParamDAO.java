package com.claridy.common.mechanism.dao;

import java.util.List;
import java.util.Map;

import com.claridy.common.mechanism.domain.Sys_Param;

public interface ISys_ParamDAO extends IBaseDAO
{
	public List<Sys_Param> findAll() throws DataAccessException;
	
	public Sys_Param find(String func_no,String parent) throws DataAccessException;
	
	public List<Sys_Param> findByCategory(String category) throws DataAccessException;
	
	/**
	 * @param category
	 * @return Map<func_value ,func_name>
	 * @throws DataAccessException
	 */
	public Map<String ,String> getMapByCategory(String category) throws DataAccessException;
	
}
