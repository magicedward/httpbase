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

	private static Bitmap takeScreenShot(Activity paramActivity) {
		View localView = paramActivity.getWindow().getDecorView();
		localView.setDrawingCacheEnabled(true);
		localView.buildDrawingCache();
		Bitmap localBitmap1 = localView.getDrawingCache();
		Rect localRect = new Rect();
		paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
		int i = localRect.top;
		Log.i("TAG", "" + i);
		int j = paramActivity.getWindowManager().getDefaultDisplay().getWidth();
		int k = paramActivity.getWindowManager().getDefaultDisplay().getHeight();
		Bitmap localBitmap2 = Bitmap.createBitmap(localBitmap1, 0, i, j, k - i);
		localView.destroyDrawingCache();
		return localBitmap2;
	}

	private static void savePic(Bitmap paramBitmap, String paramString) {
		Log.d("ScreenShot", "savpic : " + paramString);
		FileOutputStream localFileOutputStream = null;
		try {
			localFileOutputStream = new FileOutputStream(paramString);
			if (null != localFileOutputStream) {
				paramBitmap.compress(Bitmap.CompressFormat.PNG, 90, localFileOutputStream);
				localFileOutputStream.flush();
				localFileOutputStream.close();
			}
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
	}

	public static void shoot(Activity paramActivity, String paramString) {
		savePic(takeScreenShot(paramActivity), paramString);
		Toast.makeText(paramActivity, "截屏成功,图片保存在 " + paramString, 0).show();
	}

	public static void shoot(Activity paramActivity) {
		if (Environment.getExternalStorageDirectory() != null) {
			shoot(paramActivity, Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png");
		} else {
			Toast.makeText(paramActivity, "请插入存储卡", 0).show();
		}
	}
}
