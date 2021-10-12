package com.claridy.timer;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.claridy.sso.service.moduleName.facade.SSOService;

@Component
public class SSOAutoLogoutTimer implements Serializable {

	private static final long serialVersionUID = -4023051688425016877L;
	@Autowired
	private SSOService sSOService;

	//执行自动登出
	public void executeAutoLogout() {
		sSOService.autoLogoutTimer();
	}

	/**
	 * 定时计算。每天凌晨 01:00 执行一次
	 * 
	 * @Scheduled(cron = "0 0 1 * * *") public void show() {
	 *                 System.out.println("Annotation：is show run"); }
	 */

	/**
	 * 心跳更新。启动时执行一次，之后每隔2秒执行一次
	 * 
	 * @Scheduled(fixedRate = 1000 * 2) public void print() {
	 *                      System.out.println("Annotation：print run"); }
	 */
}
