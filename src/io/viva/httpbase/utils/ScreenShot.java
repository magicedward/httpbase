package io.viva.httpbase.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ScreenShot {
	static final String TAG = "ScreenShot";

	/**
	 * 返回
	 * @param activity
	 * @return
	 */
	private static Bitmap takeScreenShot(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		Rect rect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		int i = rect.top;
		Log.i("TAG", "" + i);
		int j = activity.getWindowManager().getDefaultDisplay().getWidth();
		int k = activity.getWindowManager().getDefaultDisplay().getHeight();
		Bitmap b = Bitmap.createBitmap(bitmap, 0, i, j, k - i);
		view.destroyDrawingCache();
		return b;
	}

	/**
	 * @param bitmap
	 * @param filePath
	 */
	private static void savePic(Bitmap bitmap, String filePath) {
		Log.d("ScreenShot", "savpic : " + filePath);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filePath);
			if (null != fileOutputStream) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param activity
	 * @param filePath
	 */
	public static void shoot(Activity activity, String filePath) {
		savePic(takeScreenShot(activity), filePath);
		Toast.makeText(activity, "截屏成功,图片保存在 " + filePath, 0).show();
	}

	/**
	 * @param activity
	 */
	public static void shoot(Activity activity) {
		if (Environment.getExternalStorageDirectory() != null) {
			shoot(activity, Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png");
		} else {
			Toast.makeText(activity, "请插入存储卡", 0).show();
		}
	}
}
