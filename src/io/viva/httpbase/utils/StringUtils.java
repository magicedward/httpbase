package io.viva.httpbase.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static int adjustFontSize(int paramInt1, int paramInt2) {
		if (paramInt1 <= 240) {
			return 10;
		}
		if (paramInt1 <= 320) {
			return 14;
		}
		if (paramInt1 <= 480) {
			return 24;
		}
		if (paramInt1 <= 540) {
			return 26;
		}
		if (paramInt1 <= 800) {
			return 30;
		}
		return 30;
	}

	public static boolean checkDate(String paramString1, String paramString2) {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(paramString2);
		Date localDate = null;
		try {
			localDate = localSimpleDateFormat.parse(paramString1);
		} catch (Exception localException) {
			return false;
		}
		String str = localSimpleDateFormat.format(localDate);
		return paramString1.equals(str);
	}

	public boolean checkEmail(String paramString) {
		Pattern localPattern = Pattern.compile("^/w+([-.]/w+)*@/w+([-]/w+)*/.(/w+([-]/w+)*/.)*[a-z]{2,3}$");
		Matcher localMatcher = localPattern.matcher(paramString);
		return localMatcher.matches();
	}

	public boolean checkPhone(String paramString) {
		Pattern localPattern = Pattern.compile("^13/d{9}||15[8,9]/d{8}$");
		Matcher localMatcher = localPattern.matcher(paramString);
		return localMatcher.matches();
	}

	public static boolean isNumeric(String paramString) {
		Pattern localPattern = Pattern.compile("-?[0-9]+.?[0-9]+");
		Matcher localMatcher = localPattern.matcher(paramString);
		return localMatcher.matches();
	}
}
