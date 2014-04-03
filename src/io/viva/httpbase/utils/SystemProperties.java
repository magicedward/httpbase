package io.viva.httpbase.utils;

public class SystemProperties {
	public static final int PROP_NAME_MAX = 31;
	public static final int PROP_VALUE_MAX = 91;

	private static native String native_get(String paramString);

	private static native String native_get(String paramString1, String paramString2);

	private static native int native_get_int(String paramString, int paramInt);

	private static native long native_get_long(String paramString, long paramLong);

	private static native boolean native_get_boolean(String paramString, boolean paramBoolean);

	private static native void native_set(String paramString1, String paramString2);

	public static String get(String paramString) {
		if (paramString.length() > PROP_NAME_MAX)
			throw new IllegalArgumentException("key.length > 31");
		return native_get(paramString);
	}

	public static String get(String paramString1, String paramString2) {
		if (paramString1.length() > PROP_NAME_MAX)
			throw new IllegalArgumentException("key.length > 31");
		return native_get(paramString1, paramString2);
	}

	public static int getInt(String paramString, int paramInt) {
		if (paramString.length() > PROP_NAME_MAX)
			throw new IllegalArgumentException("key.length > 31");
		return native_get_int(paramString, paramInt);
	}

	public static long getLong(String paramString, long paramLong) {
		if (paramString.length() > PROP_NAME_MAX)
			throw new IllegalArgumentException("key.length > 31");
		return native_get_long(paramString, paramLong);
	}

	public static boolean getBoolean(String paramString, boolean paramBoolean) {
		if (paramString.length() > PROP_NAME_MAX)
			throw new IllegalArgumentException("key.length > 31");
		return native_get_boolean(paramString, paramBoolean);
	}

	public static void set(String paramString1, String paramString2) {
		if (paramString1.length() > PROP_NAME_MAX)
			throw new IllegalArgumentException("key.length > 31");
		if ((paramString2 != null) && (paramString2.length() > PROP_VALUE_MAX))
			throw new IllegalArgumentException("val.length > 91");
		native_set(paramString1, paramString2);
	}
}
