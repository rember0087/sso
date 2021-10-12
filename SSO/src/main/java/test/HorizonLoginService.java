package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

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
import com.claridy.sso.service.moduleName.domain.SSOUser;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

@Service
public class HorizonLoginService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	public SystemProperties systemProperties;
	@Autowired
	public ISSODAO ssoDAO;

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
		JSONRPC2Request request = new JSONRPC2Request(method, map, requestID);
		JSONRPC2Response response = null;

		System.out.println("request:" + request);

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
				// TODO 取得账号信息
				if (null != tmpAuthCode) {
					retSSOLoginResult = getCardInfoByAuthCode(serverURL,
							tmpAuthCode, password);

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

			System.out.println("response:" + response);// System.out.println(map2);
			String tmp = response.toString();
			System.out.println("===" + tmp);
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
							System.out.println(jsonObject.getString("CardNo"));
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

			} else {// Result為空，為獲取到正確的數據
				retSSOLoginResult.setMsg("Auth Result is null");
			}

		} catch (JSONRPC2SessionException e) {
			log.error("认证异常", e);
			retSSOLoginResult.setMsg("认证异常");
			return retSSOLoginResult;
		}

		return retSSOLoginResult;
	}

	public static void main(String[] args) {

		URL serverURL = null;

		try {

			serverURL = new URL("http://218.1.116.99/ShlibWeb/jsonrpc");

		} catch (MalformedURLException e) {

			e.printStackTrace();

		}

		JSONRPC2Session mySession = new JSONRPC2Session(serverURL);

		Map<String, Object> map = new HashMap<String, Object>();
		//登陸
		String method = "Reader_AuthByUserName";
		// 方法名
		//String method = "CardInfo";
		//String method = "CardInfo";
		//
		// map.put("UserName", "00947825");
		//
		// map.put("Password", "123456");
		// D627E28F-F68D-76DC-43DC-69317E18E795
//92007851
		//
		 map.put("UserName", "9129349");
		 map.put("Password", "12456");
		 //map.put("AuthCode", "D627E28F-F68D-76DC-43DC-69317E18E795");
		map.put("AuthSerialCode", "libtest");
		map.put("AppId", "116");
		map.put("AppSecret", "285d13b7d1564667ae74a10910d1633a");

		// 用来匹配一次调用与接收，实际使用时请随机生成个唯一码
		String requestID = UUIDGenerator.getUUID();

		JSONRPC2Request request = new JSONRPC2Request(method, map, requestID);

		JSONRPC2Response response = null;

		System.out.println("request:" + request);

		try {

			response = mySession.send(request);

			System.out.println("response:" + response);// System.out.println(map2);
			String tmp = response.toString();
			System.out.println("===" + tmp);
			/*
			 * if (tmp.indexOf("AuthResult") != -1) { tmp =
			 * tmp.substring(tmp.indexOf("AuthResult") + 15,
			 * tmp.indexOf("\\\"}}")); } if (tmp.indexOf("CardNo") != -1) { tmp
			 * = tmp.substring(tmp.indexOf("CardNo") + 11,
			 * tmp.indexOf("\\\"}}")); }
			 */

			try {
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
								jsonObject.getString("CardNo");
								System.out.println(jsonObject
										.getString("CardNo"));
							}
							// 姓名
							if (jsonObject.has("CardOwnerInfo")) {
								jsonObject.getString("CardOwnerInfo");
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
								System.out.println(jsonObject
										.getString("CardCanRenew"));
							}
							//
							if (jsonObject.has("CardRenewID")) {
								jsonObject.getString("CardRenewID");
								System.out.println(jsonObject
										.getString("CardRenewID"));
							}
						}
					} else {
						// 登陆失败
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			System.out.println(tmp);
		} catch (JSONRPC2SessionException e) {

			e.printStackTrace();

		}

	}
}
