package com.claridy.sso.web.webService;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;

import javax.xml.ws.handler.MessageContext;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.HorizonLoginService;

import com.claridy.common.util.SystemVariable;
import com.claridy.sso.service.moduleName.domain.SSOLoginResult;
import com.claridy.sso.service.moduleName.domain.SSOResult;
import com.claridy.sso.service.moduleName.facade.SSOService;

/**
 * 
 * @author RemberSu
 * @version 1.0
 * 
 */
// http://localhost:8080/SSO/services/SSOAPI?wsdl
// http://221.226.25.91:8080/SSO/services/SSOAPI?wsdl
@WebService(endpointInterface = "com.claridy.sso.web.webService.SSOWebService", serviceName = "SSOWebService")
public class SSOWebServiceImpl implements SSOWebService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Resource(name = "org.apache.cxf.jaxws.context.WebServiceContextImpl")
	private WebServiceContext context;
	private SSOService sSOService = (SSOService) SpringBeanFactoryUtils
			.getBean("SSOService");
	
	

	public SSOLoginResult getLoginResult(String userId, String userKey) {
		SSOLoginResult retSSOLoginResult = new SSOLoginResult();
		retSSOLoginResult.setStatus(SystemVariable.FAIL);
		if (userId != null) {
			userId = userId.trim();
		}
		// 获取用户IP
		String userIdAddress = getClientIpCxf();

		logger.debug("Connection request form: " + userId + ",password:"
				+ userKey + ",userip [" + userIdAddress + "] ");
		try {
			if ((userId == null) || (userId.equals("")) || (userKey == null)
					|| (userKey.equals(""))) {
				retSSOLoginResult.setMsg("账号或密码不能为空");
				this.logger.error("No User or password, cannot authenticate.");
			} else {
				retSSOLoginResult = sSOService.getLoginResult(userId, userKey,
						userIdAddress);
			}
		} catch (Exception e) {
			retSSOLoginResult.setMsg("SSO认证异常");
		}
		return retSSOLoginResult;
	}

	public SSOResult getSSOResult(String guuKey) {
		SSOResult retSSOResult = new SSOResult();
		retSSOResult.setStatus(SystemVariable.FAIL);
		if (guuKey != null) {
			guuKey = guuKey.trim();
			retSSOResult = sSOService.getSSOResult(guuKey, getClientIpCxf());
		} else {
			retSSOResult.setMsg("输入参数不能为空");
		}
		return retSSOResult;
	}

	public void logout(String guuKey) {
		if (guuKey != null) {
			guuKey = guuKey.trim();
			sSOService.logout(guuKey, getClientIpCxf());
		}
	}

	public String continueSSO(String guuKey, String timeKey) {
		String retStr = "";
		if (guuKey == null || "".equals(guuKey) || timeKey == null
				|| "".equals(timeKey)) {
			retStr = "输入参数不能为空";
		} else {
			guuKey = guuKey.trim();
			timeKey = timeKey.trim();
			retStr = sSOService.continueSSO(guuKey, timeKey, getClientIpCxf());
		}
		return retStr;
	}

	public String getClientIpCxf() {
		MessageContext ctx = context.getMessageContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(AbstractHTTPDestination.HTTP_REQUEST);
		String ip = request.getRemoteAddr();
		return ip;
	}
	
	public SSOLoginResult getHorizonLoginResult(String userId, String userKey,String userType) {
		SSOLoginResult retSSOLoginResult = new SSOLoginResult();
		retSSOLoginResult.setStatus(SystemVariable.FAIL);
		if (userId != null) {
			userId = userId.trim();
		}
		// 获取用户IP
		String userIdAddress = getClientIpCxf();

		logger.debug("Connection request form: " + userId + ",password:"
				+ userKey + ",userip [" + userIdAddress + "] ");
		try {
			if ((userId == null) || (userId.equals("")) || (userKey == null)
					|| (userKey.equals(""))) {
				retSSOLoginResult.setMsg("账号或密码不能为空");
				this.logger.error("No User or password, cannot authenticate.");
			} else {
				retSSOLoginResult = sSOService.getHorizonLoginResult(userId, userKey,userType,
						userIdAddress);
			}
		} catch (Exception e) {
			retSSOLoginResult.setMsg("SSO认证异常");
			this.logger.error("SSO Exception",e);
		}
		return retSSOLoginResult;
	}

}
