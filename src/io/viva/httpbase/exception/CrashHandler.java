package io.viva.httpbase.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import android.text.TextUtils;
import android.util.Log;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
	private OnCrashInterface onCrashInterface;

	public CrashHandler(OnCrashInterface paramOnCrashInterface) {
		this.onCrashInterface = paramOnCrashInterface;
	}

	public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
		ByteArrayOutputStream localByteArrayOutputStream = null;
		PrintStream localPrintStream = null;
		String str = null;
		try {
			localByteArrayOutputStream = new ByteArrayOutputStream();
			localPrintStream = new PrintStream(localByteArrayOutputStream);
			paramThrowable.printStackTrace(localPrintStream);
			Log.e("CrashHandler", "CrashHandler", paramThrowable);
			str = new String(localByteArrayOutputStream.toByteArray());
		} catch (Exception localException2) {
			localException2.printStackTrace();
		} finally {
			try {
				if (localPrintStream != null) {
					localPrintStream.close();
					localPrintStream = null;
				}
				if (localByteArrayOutputStream != null) {
					localByteArrayOutputStream.close();
					localByteArrayOutputStream = null;
				}
			} catch (Exception localException4) {
				localException4.printStackTrace();
			}
		}
		if ((this.onCrashInterface != null) && (str != null) && (!TextUtils.isEmpty(str))) {
			this.onCrashInterface.onCrash(str);
		}
	}

	public interface OnCrashInterface {
		public void onCrash(String paramString);
	}
}