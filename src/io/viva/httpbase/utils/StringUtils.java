package io.viva.httpbase.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	
	public static int adjustFontSize(int i) {
		if (i <= 240) {
			return 10;
		}
		if (i <= 320) {
			return 14;
		}
		if (i <= 480) {
			return 24;
		}
		if (i <= 540) {
			return 26;
		}
		if (i <= 800) {
			return 30;
		}
		return 30;
	}

	public static boolean checkDate(String destStr, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = simpleDateFormat.parse(destStr);
		} catch (Exception e) {
			return false;
		}
		return destStr.equals(simpleDateFormat.format(date));
	}

	public boolean checkEmail(String str) {
		Pattern pattern = Pattern.compile("^/w+([-.]/w+)*@/w+([-]/w+)*/.(/w+([-]/w+)*/.)*[a-z]{2,3}$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public boolean checkPhone(String str) {
		Pattern pattern = Pattern.compile("^13/d{9}||15[8,9]/d{8}$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
