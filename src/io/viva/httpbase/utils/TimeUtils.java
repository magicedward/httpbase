package io.viva.httpbase.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeUtils {
	public static final long DAY_MILLISE_SECONDS = 86400000L;
	public static final long HOUR_MILLISE_SECONDS = 3600000L;
	private static SimpleDateFormat format1 = new SimpleDateFormat("MM月dd日");
	private static SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日");

	public static String getDateFormat() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA);
		return localSimpleDateFormat.format(new Date());
	}

	public static String getDateFormat(String paramString) {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(paramString, Locale.CHINA);
		return localSimpleDateFormat.format(new Date());
	}

	public static String getDateFormat(long paramLong, String paramString) {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(paramString, Locale.CHINA);
		return localSimpleDateFormat.format(Long.valueOf(paramLong));
	}

	public static String getDateFormat(long paramLong) {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return localSimpleDateFormat.format(new Date(paramLong));
	}

	public static long getTimeInMillis(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
		GregorianCalendar localGregorianCalendar = new GregorianCalendar(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
		return localGregorianCalendar.getTimeInMillis();
	}

	public static String getTimeString(long paramLong) {
		long l1 = System.currentTimeMillis();
		long l2 = l1 - paramLong * 1000L;
		int i = 0;
		if (l2 > 86400000L) {
			i = (int) (l2 / 86400000L);
			if (i <= 3) {
				return new StringBuilder().append(i).append("天前").toString();
			}
			int j = new Date(paramLong * 1000L).getYear();
			if (j >= new Date().getYear()) {
				return format1.format(new Date(paramLong * 1000L));
			}
			return format2.format(new Date(paramLong * 1000L));
		}
		if (l2 > 3600000L) {
			i = (int) (l2 / 3600000L);
			if (i > 0) {
				return new StringBuilder().append(i).append("小时前").toString();
			}
		} else if (l2 > 60000L) {
			i = (int) (l2 / 60000L);
			if (i > 0) {
				return new StringBuilder().append(i).append("分钟前").toString();
			}
		}
		return "刚刚";
	}

	public static String getDuration(int paramInt) {
		if (paramInt == 0) {
			return "0秒";
		}
		StringBuilder localStringBuilder = new StringBuilder("");
		int i = paramInt / 3600;
		int j = paramInt % 3600 / 60;
		int k = paramInt % 60;
		if (i > 0) {
			localStringBuilder.append(new StringBuilder().append(String.format("%d", new Object[] { Integer.valueOf(i) })).append("小时").toString());
		}
		if (j > 0) {
			localStringBuilder.append(new StringBuilder().append(String.format("%2d", new Object[] { Integer.valueOf(j) })).append("分钟").toString());
		}
		if (k > 0) {
			localStringBuilder.append(new StringBuilder().append(String.format("%2d", new Object[] { Integer.valueOf(k) })).append("秒").toString());
		}
		return localStringBuilder.toString();
	}
}
