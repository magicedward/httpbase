package io.viva.httpbase.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import android.util.Log;

public class BeanUtils {
	public static void setFieldValue(Object paramObject1, String paramString, Object paramObject2) {
		Field localField = getDeclaredField(paramObject1, paramString);
		if (localField == null) {
			throw new IllegalArgumentException("Could not find field [" + paramString + "] on target [" + paramObject1 + "]");
		}
		makeAccessible(localField);
		try {
			localField.set(paramObject1, paramObject2);
		} catch (IllegalAccessException localIllegalAccessException) {
			Log.e("", "", localIllegalAccessException);
		}
	}

	protected static Field getDeclaredField(Object paramObject, String paramString) {
		return getDeclaredField(paramObject.getClass(), paramString);
	}

	protected static Field getDeclaredField(Class paramClass, String paramString) {
		Class localClass = paramClass;
		while (localClass != Object.class)
			try {
				return localClass.getDeclaredField(paramString);
			} catch (NoSuchFieldException localNoSuchFieldException) {
				localClass = localClass.getSuperclass();
			}
		return null;
	}

	protected static void makeAccessible(Field paramField) {
		if ((!Modifier.isPublic(paramField.getModifiers())) || (!Modifier.isPublic(paramField.getDeclaringClass().getModifiers()))) {
			paramField.setAccessible(true);
		}
	}
}
