package io.viva.base.info;

import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class PhoneManager {
	private int heapSize = this.activityManager.getMemoryClass();
	private int screenWidthPixels;
	private int screenHeightPixels;
	public static final int DENSITY_LOW = 120;
	public static final int DENSITY_MEDIUM = 160;
	public static final int DENSITY_HIGH = 240;
	public static final int DENSITY_XHIGH = 320;
	private int screenDensity;
	private static Context context;
	private ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
	private static WindowManager windowManager;
	private DisplayMetrics displayMetrics = new DisplayMetrics();
	private static PhoneManager sharedDeviceManager;

	private PhoneManager() {
		windowManager.getDefaultDisplay().getMetrics(this.displayMetrics);
		this.screenWidthPixels = this.displayMetrics.widthPixels;
		this.screenHeightPixels = this.displayMetrics.heightPixels;
		switch (this.displayMetrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			this.screenDensity = DENSITY_LOW;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			this.screenDensity = DENSITY_MEDIUM;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			this.screenDensity = DENSITY_HIGH;
			break;
		default:
			this.screenDensity = DENSITY_XHIGH;
		}
	}

	public static void setContext(Context paramContext) {
		context = paramContext;
	}

	public static void setWindowManager(WindowManager paramWindowManager) {
		windowManager = paramWindowManager;
	}

	public static PhoneManager getSharedDeviceManager() {
		if (sharedDeviceManager == null)
			sharedDeviceManager = new PhoneManager();
		return sharedDeviceManager;
	}

	public int getScreenWidthPixels() {
		return this.screenWidthPixels;
	}

	public int getScreenHeightPixels() {
		return this.screenHeightPixels;
	}

	public int getScreenDensity() {
		return this.screenDensity;
	}

	public int getHeapSize() {
		return this.heapSize;
	}
}