/**
 * SSO对应的WebService的接口
 */
package com.claridy.sso.web.webService;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.claridy.sso.service.moduleName.domain.SSOLoginResult;
import com.claridy.sso.service.moduleName.domain.SSOResult;

/**
 * 
 * @author RemberSu
 * @version 1.0
 * 
 */
@WebService
public interface SSOWebService {

	/**
	 * 登录认证
	 * 
	 * @param userId
	 * @param userKey
	 * @return <SSOLoginResult>
	 */
	public SSOLoginResult getLoginResult(
			@WebParam(name = "userId") String userId,
			@WebParam(name = "userKey") String userKey);

	/**
	 * SSO
	 * 
	 * @param guuKey
	 * @return <SSOResult>
	 */
	public SSOResult getSSOResult(@WebParam(name = "guuKey") String guuKey);

	/**
	 * 登出
	 */
	public void logout(@WebParam(name = "guuKey") String guuKey);

	/**
	 * 续约SSO，防止SSO超时不同系统登出
	 * 
	 * @param guuKey
	 * @param timeKey
	 * @return String 续约成功返回ok，失败返回fail
	 */
	public String continueSSO(@WebParam(name = "guuKey") String guuKey,
			@WebParam(name = "timeKey") String timeKey);

	/**
	 * 登录认证
	 * 
	 * @param userId
	 * @param userKey
	 * @return <SSOLoginResult>
	 */
	public SSOLoginResult getHorizonLoginResult(
			@WebParam(name = "userId") String userId,
			@WebParam(name = "userKey") String userKey,
			@WebParam(name = "userType") String userType);
}
