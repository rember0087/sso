package com.claridy.common.mechanism.facase;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.claridy.common.mechanism.dao.hibernateimpl.OnlineCounterDAO;


public class OnlineCounterListener implements HttpSessionListener {

	@Autowired
	private OnlineCounterDAO onlinecounterdao;
	
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		onlinecounterdao.raise();
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		onlinecounterdao.reduce();
	}

}
