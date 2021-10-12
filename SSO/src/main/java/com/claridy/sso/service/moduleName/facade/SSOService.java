package com.claridy.sso.service.moduleName.facade;

import java.io.IOException;
import java.net.URL;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.dom4j.io.SAXReader;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.claridy.common.util.AESGenerator;
import com.claridy.common.util.DateUtil;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.UUIDGenerator;
import com.claridy.sso.service.moduleName.dao.ISSODAO;
import com.claridy.sso.service.moduleName.domain.SSOLoginResult;
import com.claridy.sso.service.moduleName.domain.SSORecord;
import com.claridy.sso.service.moduleName.domain.SSOResult;
import com.claridy.sso.service.moduleName.domain.SSOUser;
import com.claridy.sso.service.moduleName.domain.SSOlog;
import com.claridy.sso.service.moduleName.domain.WebEmployee;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

@Service
public class SSOService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	public SystemProperties systemProperties;
	@Autowired
	public ISSODAO ssoDAO;

	/**
	 * 登陆认证
	 * 
	 * @param user
	 * @param password
	 * @param userIp
	 * @return <SSOLoginResult>
	 */
	public SSOLoginResult getLoginResult(String user, String password,
			String userIp) {
		log.debug("Start getLoginResult UserID:[" + user + "]" + "IP:["
				+ userIp + "]");
		SSOLoginResult retSSOLoginResult = new SSOLoginResult();
		retSSOLoginResult.setStatus(SystemVariable.FAIL);
		URL loginURL;
		try {
			String loginHost = "";
			loginHost = systemProperties.loginHost;
			if (null != loginHost && null != systemProperties.loginID
					&& null != systemProperties.loginPwd) {
				if (loginHost.contains("?")) {
					loginHost = loginHost + "&" + systemProperties.loginID
							+ "=" + user + "&" + systemProperties.loginPwd
							+ "=" + password;
				} else {
					loginHost = loginHost + "?" + systemProperties.loginID
							+ "=" + user + "&" + systemProperties.loginPwd
							+ "=" + password;
				}

				// log.info("WHAuth Begin......");
				loginURL = new URL(loginHost);
				log.debug("认证URL:" + loginHost.toString());
				String AESPwd = AESGenerator.getencrypt(
						systemProperties.AESKey, password);
				// 解析登陆API返回值
				retSSOLoginResult = parsingContent(loginURL, AESPwd);

				// 成功后记录日志
				if (null != retSSOLoginResult
						&& SystemVariable.OK.equals(retSSOLoginResult
								.getStatus())) {
					// 记录Log
					SSOlog tmpSSOlog = new SSOlog();
					tmpSSOlog.setActivetime(DateUtil.now());
					tmpSSOlog.setGuukey(retSSOLoginResult.getGuuKey());
					tmpSSOlog.setUseractive(SystemVariable.ACTIVE_LOGIN);
					tmpSSOlog.setUserid(retSSOLoginResult.getUserId());
					tmpSSOlog.setUserip(userIp);
					tmpSSOlog.setUuid(UUIDGenerator.getUUID());
					ssoDAO.create(tmpSSOlog);
				}
			} else {
				retSSOLoginResult.setMsg("InterLib认证连接配置出错，请联系系统管理员");
			}
		} catch (MalformedURLException e) {
			retSSOLoginResult.setMsg("InterLib认证API连线异常");
			log.error("InterLib认证API连线异常", e);
		} catch (Exception e) {
			retSSOLoginResult.setMsg("SSO认证异常");
			log.error("SSO认证异常", e);
		}
		return retSSOLoginResult;
	}

	/**
	 * 解析api返回信息
	 * 
	 * @param url
	 * @return SSOLoginResult
	 * @throws IOException
	 */
	public SSOLoginResult parsingContent(URL url, String AESPwd)
			throws IOException {
		SSOLoginResult retSSOLoginResult = new SSOLoginResult();
		retSSOLoginResult.setStatus(SystemVariable.FAIL);
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(url);
			Element patronData = doc.getRootElement();
			String authResult = patronData.elementText("state");

			if (null != authResult && !"".equals(authResult)) {
				// y(認證通過)
				if ("1".equals(authResult)) {
					boolean isNotErr = true;
					Element hoData = patronData.element("readerinfo");
					// 讀者證號
					String tmpReaderCode = hoData.elementText("rdid");
					// 讀者姓名
					String tmpNAME = hoData.elementText("rdname");
					// 讀者姓名
					String tmpSex = hoData.elementText("rdsex");
					// 單位名稱
					String tmpLibName = hoData.elementText("rdlibcode");
					// 單位代碼
					String tmpLibCode = hoData.elementText("rdlibname");
					// 没有错误则向tmpAuthResult写入正确的认证信息
					if (isNotErr) {
						// 設置返回值
						String guukey = UUIDGenerator.getUUID();
						String timeKey = UUIDGenerator.getUUID();
						retSSOLoginResult.setStatus("ok");
						retSSOLoginResult.setUserId(tmpReaderCode);
						retSSOLoginResult.setUserName(tmpNAME);
						retSSOLoginResult.setMsg("登陆成功");
						retSSOLoginResult.setGuuKey(guukey);
						retSSOLoginResult.setTimeKey(timeKey);
						retSSOLoginResult.setUserPwd(AESPwd);
						// 操作記錄存入数据库
						SSORecord tmpSSORecord = new SSORecord();
						tmpSSORecord.setGuukey(guukey);
						tmpSSORecord.setTimekey(timeKey);
						tmpSSORecord.setLogintime(DateUtil.now());
						Integer timeoutTime = systemProperties.onelineLoginTimeout;
						tmpSSORecord.setTimeouttime(DateUtil
								.getTimeOutTime(timeoutTime));
						tmpSSORecord.setUserid(tmpReaderCode);
						tmpSSORecord.setUsername(tmpNAME);
						tmpSSORecord.setUserpwd(AESPwd);
						ssoDAO.create(tmpSSORecord);
						// 記錄用户信息
						SSOUser tmpSSOUser = ssoDAO
								.findSSOUserByUserid(tmpReaderCode);
						if (null == tmpSSOUser) {
							tmpSSOUser = new SSOUser();
							tmpSSOUser.setUserid(tmpReaderCode);
							tmpSSOUser.setUsername(tmpNAME);
							tmpSSOUser.setUsersex(tmpSex);
							tmpSSOUser.setLibcode(tmpLibCode);
							tmpSSOUser.setLibname(tmpLibName);
							tmpSSOUser.setUserpwd(AESPwd);
							ssoDAO.create(tmpSSOUser);
						} else {
							tmpSSOUser.setUsername(tmpNAME);
							tmpSSOUser.setUsersex(tmpSex);
							tmpSSOUser.setLibcode(tmpLibCode);
							tmpSSOUser.setLibname(tmpLibName);
							tmpSSOUser.setUserpwd(AESPwd);
							ssoDAO.update(tmpSSOUser);
						}
					}
				} else {// 登陸失敗
					retSSOLoginResult.setMsg("账号或密码不正确");
				}

			} else {// Result為空，為獲取到正確的數據
				retSSOLoginResult.setMsg("Auth Result is null");
			}

		} catch (DocumentException e) {
			retSSOLoginResult.setMsg("Auth XML throw DocumentException");
		}
		return retSSOLoginResult;
	}

	@Transactional
	public void logout(String guukey, String userIp) {
		log.debug("Start logout:[" + guukey + "]" + "IP:[" + userIp + "]");
		SSORecord tmpSSORecord = new SSORecord();
		tmpSSORecord = ssoDAO.findSSORecordByGuukey(guukey);
		if (null != tmpSSORecord) {
			// 刪除登陆信息
			ssoDAO.delete(tmpSSORecord);
			// 记录Log
			SSOlog tmpSSOlog = new SSOlog();
			tmpSSOlog.setActivetime(DateUtil.now());
			tmpSSOlog.setGuukey(tmpSSORecord.getGuukey());
			tmpSSOlog.setUseractive(SystemVariable.ACTIVE_LOGOUT);
			tmpSSOlog.setUserid(tmpSSORecord.getUserid());
			tmpSSOlog.setUserip(userIp);
			tmpSSOlog.setUuid(UUIDGenerator.getUUID());
			ssoDAO.create(tmpSSOlog);
		}
	}

	public SSOResult getSSOResult(String guukey, String userIp) {
		log.debug("Start getSSOResult:[" + guukey + "]" + "IP:[" + userIp + "]");
		SSOResult retSSOResult = new SSOResult();
		retSSOResult.setStatus(SystemVariable.FAIL);
		SSORecord tmpSSORecord = ssoDAO.findSSORecordByGuukey(guukey);
		if (null != tmpSSORecord) {
			// 超时时间在当前时间之前，则表明没有超时
			if (tmpSSORecord.getTimeouttime().after(DateUtil.now())) {
				retSSOResult.setStatus(SystemVariable.OK);
				retSSOResult.setMsg("SSO成功");
				retSSOResult.setTimeKey(tmpSSORecord.getTimekey());
				retSSOResult.setUserid(tmpSSORecord.getUserid());
				retSSOResult.setUsername(tmpSSORecord.getUsername());
				retSSOResult.setUserpwd(tmpSSORecord.getUserpwd());
			} else {
				retSSOResult.setStatus(SystemVariable.TIMEOUT);
				retSSOResult.setMsg("SSO超时");
			}
			// 写入日志
			SSOlog tmpSSOlog = new SSOlog();
			tmpSSOlog.setActivetime(DateUtil.now());
			tmpSSOlog.setGuukey(tmpSSORecord.getGuukey());
			tmpSSOlog.setUseractive(SystemVariable.ACTIVE_SSO);
			tmpSSOlog.setUserid(tmpSSORecord.getUserid());
			tmpSSOlog.setUserip(userIp);
			tmpSSOlog.setUuid(UUIDGenerator.getUUID());
			ssoDAO.create(tmpSSOlog);
		} else {
			retSSOResult.setMsg("SSO失败");
		}
		return retSSOResult;
	}

	/**
	 * 
	 * @param guuKey
	 * @param timeKey
	 * @param userIp
	 * @return String
	 */
	@Transactional
	public String continueSSO(String guuKey, String timeKey, String userIp) {
		String retStr = SystemVariable.FAIL;
		SSORecord tmpSSORecord = ssoDAO.findSSORecordByGuukeyAndtimeKey(guuKey,
				timeKey);
		if (null != tmpSSORecord) {
			retStr = SystemVariable.OK;
			// 更新超时时间，达到续约的目的
			Integer timeoutTime = systemProperties.onelineLoginTimeout;
			tmpSSORecord.setTimeouttime(DateUtil.getTimeOutTime(timeoutTime));
			ssoDAO.update(tmpSSORecord);
			// 写入日志
			SSOlog tmpSSOlog = new SSOlog();
			tmpSSOlog.setActivetime(DateUtil.now());
			tmpSSOlog.setGuukey(tmpSSORecord.getGuukey());
			tmpSSOlog.setUseractive(SystemVariable.ACTIVE_CONTINUE);
			tmpSSOlog.setUserid(tmpSSORecord.getUserid());
			tmpSSOlog.setUserip(userIp);
			tmpSSOlog.setUuid(UUIDGenerator.getUUID());
			ssoDAO.create(tmpSSOlog);
		}
		return retStr;
	}

	@Transactional
	public void autoLogoutTimer() {
		log.debug("Begin auto logout timeout one day's SSORecord data！");
		// 自动登出超时（1天前）的用户
		List<SSORecord> retSSORecordList = new ArrayList<SSORecord>();
		Date dateBefore = DateUtil.getDateBefore(new Date(), 1);
		retSSORecordList = ssoDAO.getTimeOutSSORecord(dateBefore);
		for (SSORecord ssoR : retSSORecordList) {
			ssoDAO.delete(ssoR);
			// 写入日志
			SSOlog tmpSSOlog = new SSOlog();
			tmpSSOlog.setActivetime(DateUtil.now());
			tmpSSOlog.setGuukey(ssoR.getGuukey());
			tmpSSOlog.setUseractive(SystemVariable.ACTIVE_AUTOLOGOUT);
			tmpSSOlog.setUserid(ssoR.getUserid());
			// 表示本机
			tmpSSOlog.setUserip("0:0:0:0:0:0:0:1");
			tmpSSOlog.setUuid(UUIDGenerator.getUUID());
			ssoDAO.create(tmpSSOlog);
		}
		log.debug("Auto logout timeout one day's SSORecord data Ended！");
	}

	/**
	 * Horizon登陆认证
	 * 
	 * @param user
	 * @param password
	 * @param userType
	 * @param userIp
	 * @return <SSOLoginResult>
	 */
	public SSOLoginResult getHorizonLoginResult(String user, String password,
			String userType, String userIp) {
		// 初始化登陆结果
		SSOLoginResult retSSOLoginResult = new SSOLoginResult();
		retSSOLoginResult.setStatus(SystemVariable.FAIL);
		// 管理端SSO
		if (null != userType && userType.equalsIgnoreCase("admin")) {
			retSSOLoginResult = getAdminInfo(user, password);
			// 成功后记录日志
			if (null != retSSOLoginResult
					&& SystemVariable.OK.equals(retSSOLoginResult.getStatus())) {
				// 记录Log
				SSOlog tmpSSOlog = new SSOlog();
				tmpSSOlog.setActivetime(DateUtil.now());
				tmpSSOlog.setGuukey(retSSOLoginResult.getGuuKey());
				tmpSSOlog.setUseractive(SystemVariable.ACTIVE_LOGIN);
				tmpSSOlog.setUserid(retSSOLoginResult.getUserId());
				tmpSSOlog.setUserip(userIp);
				tmpSSOlog.setUuid(UUIDGenerator.getUUID());
				ssoDAO.create(tmpSSOlog);
			}
		} else {// 前台SSO

			URL serverURL = null;
			try {
				serverURL = new URL(systemProperties.HorizonLoginURL);
			} catch (MalformedURLException e) {
				log.error("认证URL格式错误，请联系管理员", e);
				retSSOLoginResult.setMsg("认证URL格式错误，请联系管理员");
				return retSSOLoginResult;
			}

			JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
			Map<String, Object> map = new HashMap<String, Object>();
			// 方法名
			String method = "Reader_AuthByUserName";
			// map.put("UserName", "00947825");
			// map.put("Password", "123456");
			map.put("UserName", user);
			map.put("Password", password);
			map.put("AuthSerialCode", "libtest");
			map.put("AppId", systemProperties.HorizonAppId);
			map.put("AppSecret", systemProperties.HorizonAppSecret);

			// 用来匹配一次调用与接收，实际使用时请随机生成个唯一码
			String requestID = UUIDGenerator.getUUID();
			JSONRPC2Request request = new JSONRPC2Request(method, map,
					requestID);
			JSONRPC2Response response = null;
			try {
				response = mySession.send(request);
				// System.out.println("response:" + response);//
				// System.out.println(map2);
				String tmp = response.toString();
				String tmpAuthCode = null;
				// System.out.println(tmp);
				// 判断是否认证通过
				if (tmp.indexOf("AuthResult") != -1) {
					tmpAuthCode = tmp.substring(tmp.indexOf("AuthResult") + 15,
							tmp.indexOf("\\\"}}"));
					// 登陸成功，取得账号信息
					if (null != tmpAuthCode) {
						retSSOLoginResult = getCardInfoByAuthCode(serverURL,
								tmpAuthCode, password);
						// 成功后记录日志
						if (null != retSSOLoginResult
								&& SystemVariable.OK.equals(retSSOLoginResult
										.getStatus())) {
							// 记录Log
							SSOlog tmpSSOlog = new SSOlog();
							tmpSSOlog.setActivetime(DateUtil.now());
							tmpSSOlog.setGuukey(retSSOLoginResult.getGuuKey());
							tmpSSOlog
									.setUseractive(SystemVariable.ACTIVE_LOGIN);
							tmpSSOlog.setUserid(retSSOLoginResult.getUserId());
							tmpSSOlog.setUserip(userIp);
							tmpSSOlog.setUuid(UUIDGenerator.getUUID());
							ssoDAO.create(tmpSSOlog);
						}
					} else {
						log.debug("账号或密码不正确");
						retSSOLoginResult.setMsg("账号或密码不正确");
					}
				} else {
					log.debug("账号或密码不正确");
					retSSOLoginResult.setMsg("账号或密码不正确");
				}

			} catch (JSONRPC2SessionException e) {
				log.error("认证异常", e);
				retSSOLoginResult.setMsg("认证异常");
				return retSSOLoginResult;
			}
		}
		return retSSOLoginResult;
	}

	/**
	 * 解析api返回信息
	 * 
	 * @param url
	 * @return SSOLoginResult
	 * @throws IOException
	 */
	public SSOLoginResult getCardInfoByAuthCode(URL serverURL, String authCode,
			String password) {
		SSOLoginResult retSSOLoginResult = new SSOLoginResult();
		retSSOLoginResult.setStatus(SystemVariable.FAIL);
		boolean getCardInfoOk = false;
		String AESPwd = AESGenerator.getencrypt(systemProperties.AESKey,
				password);
		// 讀者證號
		String tmpReaderCode = "";
		// 讀者姓名
		String tmpNAME = "";
		// 讀者性別
		String tmpSex = "";
		// 單位名稱
		String tmpLibName = "";
		// 單位代碼
		String tmpLibCode = "";

		JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
		Map<String, Object> map = new HashMap<String, Object>();
		// 方法名
		String method = "CardInfo";
		// D627E28F-F68D-76DC-43DC-69317E18E795
		map.put("AuthCode", authCode);
		map.put("AuthSerialCode", "libtest");
		map.put("AppId", systemProperties.HorizonAppId);
		map.put("AppSecret", systemProperties.HorizonAppSecret);

		// 用来匹配一次调用与接收，实际使用时请随机生成个唯一码
		String requestID = UUIDGenerator.getUUID();
		JSONRPC2Request request = new JSONRPC2Request(method, map, requestID);
		JSONRPC2Response response = null;

		try {
			response = mySession.send(request);
			try {
				// System.out.println("response:" + response);
				// System.out.println(map2);
				String tmp = response.toString();
				JSONObject jsonObject = JSONObject.fromObject(tmp);
				if (jsonObject.has("result")) {
					// 登陆成功
					if (!jsonObject.getString("result").equals("1007")) {
						jsonObject = JSONObject.fromObject(jsonObject
								.getString("result"));

						if (jsonObject.has("CardInfoResponse")) {
							jsonObject = JSONObject.fromObject(jsonObject
									.getString("CardInfoResponse"));
							// 卡號
							if (jsonObject.has("CardNo")) {
								tmpReaderCode = jsonObject.getString("CardNo");
								System.out.println(jsonObject
										.getString("CardNo"));
							}
							// 姓名
							if (jsonObject.has("CardOwnerInfo")) {
								tmpNAME = jsonObject.getString("CardOwnerInfo");
								System.out.println(jsonObject
										.getString("CardOwnerInfo"));
							}
						}

						if (jsonObject.has("CardInfoItem")) {
							jsonObject = JSONObject.fromObject(jsonObject
									.getString("CardInfoItem"));
							// 读者卡功能名称
							if (jsonObject.has("CardFunction")) {
								jsonObject.getString("CardFunction");
								System.out.println(jsonObject
										.getString("CardFunction"));
							}
							// 有效期
							if (jsonObject.has("CardFunctionDueTime")) {
								jsonObject.getString("CardFunctionDueTime");
								System.out.println(jsonObject
										.getString("CardFunctionDueTime"));
							}
							//
							if (jsonObject.has("CardCanRenew")) {
								jsonObject.getString("CardCanRenew");
								// System.out.println(jsonObject/.getString("CardCanRenew"));
							}
							//
							if (jsonObject.has("CardRenewID")) {
								jsonObject.getString("CardRenewID");
								// System.out.println(jsonObject.getString("CardRenewID"));
							}
						}

						getCardInfoOk = true;
						retSSOLoginResult.setStatus(SystemVariable.OK);
					}
				}
				// 获取卡和账号信息OK
				if (getCardInfoOk) {
					boolean isNotErr = true;

					// 没有错误则向tmpAuthResult写入正确的认证信息
					if (isNotErr) {
						// 設置返回值
						String guukey = authCode;
						String timeKey = UUIDGenerator.getUUID();
						retSSOLoginResult.setStatus("ok");
						retSSOLoginResult.setUserId(tmpReaderCode);
						retSSOLoginResult.setUserName(tmpNAME);
						retSSOLoginResult.setMsg("登陆成功");
						retSSOLoginResult.setGuuKey(guukey);
						retSSOLoginResult.setTimeKey(timeKey);
						retSSOLoginResult.setUserPwd(AESPwd);
						// 操作記錄存入数据库
						SSORecord tmpSSORecord = new SSORecord();
						tmpSSORecord.setGuukey(guukey);
						tmpSSORecord.setTimekey(timeKey);
						tmpSSORecord.setLogintime(DateUtil.now());
						Integer timeoutTime = systemProperties.onelineLoginTimeout;
						tmpSSORecord.setTimeouttime(DateUtil
								.getTimeOutTime(timeoutTime));
						tmpSSORecord.setUserid(tmpReaderCode);
						tmpSSORecord.setUsername(tmpNAME);
						tmpSSORecord.setUserpwd(AESPwd);
						ssoDAO.saveOrUpdate(tmpSSORecord);
						// 記錄用户信息
						SSOUser tmpSSOUser = ssoDAO
								.findSSOUserByUserid(tmpReaderCode);
						if (null == tmpSSOUser) {
							tmpSSOUser = new SSOUser();
							tmpSSOUser.setUserid(tmpReaderCode);
							tmpSSOUser.setUsername(tmpNAME);
							tmpSSOUser.setUsersex(tmpSex);
							tmpSSOUser.setLibcode(tmpLibCode);
							tmpSSOUser.setLibname(tmpLibName);
							tmpSSOUser.setUserpwd(AESPwd);
							ssoDAO.create(tmpSSOUser);
						} else {
							tmpSSOUser.setUsername(tmpNAME);
							tmpSSOUser.setUsersex(tmpSex);
							tmpSSOUser.setLibcode(tmpLibCode);
							tmpSSOUser.setLibname(tmpLibName);
							tmpSSOUser.setUserpwd(AESPwd);
							ssoDAO.update(tmpSSOUser);
						}
					}

				} else {// Result為空，為獲取到正確的數據
					retSSOLoginResult.setMsg("账号或密码不正确");
				}

			} catch (JSONException e) {
				log.error("认证异常JSONException", e);
				retSSOLoginResult.setMsg("认证异常JSONException");
				return retSSOLoginResult;
			}

		} catch (JSONRPC2SessionException e) {
			log.error("认证异常JSONRPC2SessionException", e);
			retSSOLoginResult.setMsg("认证异常JSONRPC2SessionException");
			return retSSOLoginResult;
		}

		return retSSOLoginResult;
	}

	/**
	 * 上海長寧少管理端SSO
	 * 
	 * @param user
	 * @param password
	 * @return SSOLoginResult
	 */
	public SSOLoginResult getAdminInfo(String user, String password) {
		SSOLoginResult retSSOLoginResult = new SSOLoginResult();
		retSSOLoginResult.setStatus(SystemVariable.FAIL);
		WebEmployee tmpWebEmployee = new WebEmployee();
		tmpWebEmployee = ssoDAO.getWebEmployeeByUserIDAndPWD(user, password);
		// 认证成功
		if (null != tmpWebEmployee && !"".equals(tmpWebEmployee.getUuid())) {
			String AESPwd = AESGenerator.getencrypt(systemProperties.AESKey,
					password);
			retSSOLoginResult.setStatus(SystemVariable.OK);
			// 讀者證號
			String tmpReaderCode = tmpWebEmployee.getEmployeeid();
			// 讀者姓名
			String tmpNAME = tmpWebEmployee.getEmployeename();
			// 讀者性別
			String tmpSex = "";
			// 單位名稱
			String tmpLibName = "";
			// 單位代碼
			String tmpLibCode = "";

			// 設置返回值
			String guukey = tmpWebEmployee.getUuid();
			String timeKey = UUIDGenerator.getUUID();
			retSSOLoginResult.setStatus("ok");
			retSSOLoginResult.setUserId(tmpReaderCode);
			retSSOLoginResult.setUserName(tmpNAME);
			retSSOLoginResult.setMsg("登陆成功");
			retSSOLoginResult.setGuuKey(guukey);
			retSSOLoginResult.setTimeKey(timeKey);
			retSSOLoginResult.setUserPwd(AESPwd);
			// 操作記錄存入数据库
			SSORecord tmpSSORecord = new SSORecord();
			tmpSSORecord.setGuukey(guukey);
			tmpSSORecord.setTimekey(timeKey);
			tmpSSORecord.setLogintime(DateUtil.now());
			Integer timeoutTime = systemProperties.onelineLoginTimeout;
			tmpSSORecord.setTimeouttime(DateUtil.getTimeOutTime(timeoutTime));
			tmpSSORecord.setUserid(tmpReaderCode);
			tmpSSORecord.setUsername(tmpNAME);
			tmpSSORecord.setUserpwd(AESPwd);
			ssoDAO.saveOrUpdate(tmpSSORecord);
			// 記錄用户信息
			SSOUser tmpSSOUser = ssoDAO.findSSOUserByUserid(tmpReaderCode);
			if (null == tmpSSOUser) {
				tmpSSOUser = new SSOUser();
				tmpSSOUser.setUserid(tmpReaderCode);
				tmpSSOUser.setUsername(tmpNAME);
				tmpSSOUser.setUsersex(tmpSex);
				tmpSSOUser.setLibcode(tmpLibCode);
				tmpSSOUser.setLibname(tmpLibName);
				tmpSSOUser.setUserpwd(AESPwd);
				ssoDAO.create(tmpSSOUser);
			} else {
				tmpSSOUser.setUsername(tmpNAME);
				tmpSSOUser.setUsersex(tmpSex);
				tmpSSOUser.setLibcode(tmpLibCode);
				tmpSSOUser.setLibname(tmpLibName);
				tmpSSOUser.setUserpwd(AESPwd);
				ssoDAO.update(tmpSSOUser);
			}

		} else {// Result為空，為獲取到正確的數據
			retSSOLoginResult.setMsg("账号或密码不正确");
		}

		return retSSOLoginResult;
	}
}
