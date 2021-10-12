package com.claridy.common.util;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class AESGenerator {
	/**
	 * AES加密
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public static String getencrypt(String key, String data) {
		String encryptBase64 = "";
		byte[] dataBytes;
		try {
			dataBytes = data.getBytes("UTF-8");
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			byte[] keyBytes = key.getBytes();

			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);

			byte[] encrypted = new byte[cipher.getOutputSize(dataBytes.length)];
			int ctLength = cipher.update(dataBytes, 0, dataBytes.length,
					encrypted, 0);
			ctLength += cipher.doFinal(encrypted, ctLength);
			encryptBase64 = new String(new Base64().encode(encrypted));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptBase64;
	}

	/**
	 * AES解密
	 * 
	 * @param key
	 * @param base64
	 * @return
	 */
	public static String getdecrypt(String key, String base64) {
		String decrypted = "";
		try {
			byte[] b = new Base64().decode(base64);
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			byte[] keyBytes = key.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] decrypt = cipher.doFinal(b);
			decrypted = new String(decrypt, "UTF-8");

		} catch (Exception e) {
			decrypted = "-1";
			// System.out.println(e);
		}
		return decrypted;
	}
}
