package io.viva.httpbase.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.util.Base64;

public class TripleDESUtils {

	/**
	 * ecb方式加密
	 * @param key 加密key，注意密钥是24位
	 * @param data 待加密数据
	 * @return 加密后的数据
	 * @throws Exception
	 */
	public static String des3EncodeECB(String key, String data) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] bOut = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64.encodeToString(bOut, Base64.DEFAULT);
	}

	/**
	 * ecb方式解密
	 * @param key 解密key，和加密key一样
	 * @param data 待解密数据
	 * @return 解密后的数据
	 * @throws Exception
	 */
	public static String des3DecodeECB(String key, String data) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, deskey);
		byte[] bOut = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));
		return new String(bOut, "UTF-8");

	}

	/**
	 * cbc方式加密
	 * @param key 加密key，注意密钥是24位
	 * @param keyiv 向量key
	 * @param data 待加密数据
	 * @return 加密后的数据
	 * @throws Exception
	 */
	public static String des3EncodeCBC(String key, byte[] keyiv, String data) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(keyiv);
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] bOut = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64.encodeToString(bOut, Base64.DEFAULT);
	}

	/**
	 * cbc方式解密
	 * @param key 解密key，和加密key一样
	 * @param keyiv 向量key
	 * @param data 待解密数据
	 * @return 解密后的数据
	 * @throws Exception
	 */
	public static String des3DecodeCBC(String key, byte[] keyiv, String data) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(keyiv);
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte[] bOut = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));
		return new String(bOut, "UTF-8");

	}
}
