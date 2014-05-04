package io.viva.httpbase.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	public static String encode(String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] result = messageDigest.digest(password.getBytes());

			StringBuffer sb = new StringBuffer();
			for (byte b : result) {
				int num = b & 0xff;
				String hex = Integer.toHexString(num);
				if (hex.length() == 1) {
					sb.append(0);
				}
				sb.append(hex);
			}
			return sb.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
