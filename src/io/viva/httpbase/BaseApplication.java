package io.viva.httpbase;

import io.viva.httpbase.exception.CrashHandler;
import io.viva.httpbase.info.BaseAppInfo;
import android.app.Application;
import android.os.Process;

public class BaseApplication extends Application implements CrashHandler.OnCrashInterface {
	private static final float TARGET_HEAP_UTILIZATION = 0.75F;
	private static BaseApplication mApplication;

	public static BaseApplication getApplication() {
		return mApplication;
	}

	public void onCreate() {
		super.onCreate();
		mApplication = this;
		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
		BaseAppInfo.init(this);
	}

	public void onCrash(String paramString) {
		// do something when app crashed!
	}

	public void exitApplication() {
		Process.killProcess(Process.myPid());
	}
}
