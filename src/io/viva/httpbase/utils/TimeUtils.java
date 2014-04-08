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
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA);
		return simpleDateFormat.format(new Date());
	}

	public static String getDateFormat(String template) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(template, Locale.CHINA);
		return simpleDateFormat.format(new Date());
	}

	public static String getDateFormat(long milliseconds, String template) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(template, Locale.CHINA);
		return simpleDateFormat.format(Long.valueOf(milliseconds));
	}

	public static String getDateFormat(long milliseconds) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return simpleDateFormat.format(new Date(milliseconds));
	}

	public static long getTimeInMillis(int year, int month, int day, int hour, int minute, int second) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, day, hour, minute, second);
		return gregorianCalendar.getTimeInMillis();
	}

	/**
	 * @param milliseconds
	 * @return
	 */
	public static String getTimeString(long seconds) {
		long l1 = System.currentTimeMillis();
		long l2 = l1 - seconds * 1000L;
		int i = 0;
		if (l2 > DAY_MILLISE_SECONDS) {
			i = (int) (l2 / DAY_MILLISE_SECONDS);
			if (i <= 3) {
				return new StringBuilder().append(i).append("天前").toString();
			}
			int j = new Date(seconds * 1000L).getYear();
			if (j >= new Date().getYear()) {
				return format1.format(new Date(seconds * 1000L));
			}
			return format2.format(new Date(seconds * 1000L));
		}
		if (l2 > HOUR_MILLISE_SECONDS) {
			i = (int) (l2 / HOUR_MILLISE_SECONDS);
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

	/**
	 * 根据毫秒计算中文字符串
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String getDuration(int milliseconds) {
		if (milliseconds == 0) {
			return "0秒";
		}
		StringBuilder sb = new StringBuilder("");
		int i = milliseconds / 3600;
		int j = milliseconds % 3600 / 60;
		int k = milliseconds % 60;
		if (i > 0) {
			sb.append(new StringBuilder().append(String.format("%d", new Object[] { Integer.valueOf(i) })).append("小时").toString());
		}
		if (j > 0) {
			sb.append(new StringBuilder().append(String.format("%2d", new Object[] { Integer.valueOf(j) })).append("分钟").toString());
		}
		if (k > 0) {
			sb.append(new StringBuilder().append(String.format("%2d", new Object[] { Integer.valueOf(k) })).append("秒").toString());
		}
		return sb.toString();
	}
}
