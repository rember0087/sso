package com.claridy.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemProperties {
	
	//Login parameters
	public @Value("#{systemproperties['Properties.login_host']}") String loginHost;
	public @Value("#{systemproperties['Properties.login_userID']}") String loginID;
	public @Value("#{systemproperties['Properties.login_pwd']}") String loginPwd;
	
	//SSO Timeout
	public @Value("#{systemproperties['Properties.one_line_Login_timeout']}") int onelineLoginTimeout;
	
	//AES
	public @Value("#{systemproperties['Properties.aes_key']}") String AESKey;
	
	//ShangHai Horizon Login API
	public @Value("#{systemproperties['Properties.HorizonLoginURL']}") String HorizonLoginURL;
	//115 Portal ,116 WeChat
	public @Value("#{systemproperties['Properties.HorizonAppId']}") String HorizonAppId;
	//dd0b3b9822fe477c985cc78078c3b20d,285d13b7d1564667ae74a10910d1633a
	public @Value("#{systemproperties['Properties.HorizonAppSecret']}") String HorizonAppSecret;

}
