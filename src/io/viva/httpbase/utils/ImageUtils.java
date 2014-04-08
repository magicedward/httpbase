package io.viva.httpbase.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;

public class ImageUtils {

	/**
	 * 拉伸图片（失真）
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getScaleBitmap(Bitmap bitmap, int width, int height) {
		int i = bitmap.getWidth();
		int j = bitmap.getHeight();
		float f1 = width / i;
		float f2 = height / j;
		Matrix matrix = new Matrix();
		matrix.postScale(f1, f2);
		Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, i, j, matrix, true);
		bitmap.recycle();
		bitmap = null;
		return b;
	}

	/**
	 * 拉伸图片（无失真）
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getScaleBitmapAdjust(Bitmap bitmap, int width, int height) {
		if (bitmap == null) {
			return null;
		}
		int i = bitmap.getWidth();
		int j = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float f1 = width / i;
		float f2 = height / j;
		if (f1 < f2) {
			if ((f1 < 0.99F) || (f1 > 1.0F)) {
				matrix.postScale(f1, f1);
			} else {
				return bitmap;
			}
		} else if ((f2 < 0.99F) || (f2 > 1.0F)) {
			matrix.postScale(f2, f2);
		} else {
			return bitmap;
		}
		Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, i, j, matrix, true);
		bitmap.recycle();
		bitmap = null;
		return b;
	}

	/**
	 * 拉伸图片
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @param px
	 * @param py
	 * @return
	 */
	public static Bitmap getScaleBitmapAdjust(Bitmap bitmap, int width, int height, float px, float py) {
		if (bitmap == null) {
			return null;
		}
		int i = bitmap.getWidth();
		int j = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float f1 = width / i;
		float f2 = height / j;
		if (f1 < f2) {
			if ((f1 < 0.99F) || (f1 > 1.0F)) {
				matrix.postScale(f1, f1, px, py);
			} else {
				return bitmap;
			}
		} else if ((f2 < 0.99F) || (f2 > 1.0F)) {
			matrix.postScale(f2, f2, px, py);
		} else {
			return bitmap;
		}
		Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, i, j, matrix, true);
		bitmap.recycle();
		bitmap = null;
		return b;
	}

	/**
	 * 压缩图片
	 * 
	 * @param bitmap
	 * @param paramFloat
	 * @return
	 */
	public static Bitmap getCompressBitmap(Bitmap bitmap, float paramFloat) {
		if (bitmap == null) {
			return null;
		}
		float f1 = bitmap.getWidth();
		float f2 = bitmap.getHeight();
		if ((f1 > paramFloat) || (f2 > paramFloat)) {
			float f3 = 0.0F;
			float f4 = 0.0F;
			if (f1 > f2) {
				f4 = paramFloat / f1 * f2;
				f3 = paramFloat;
			} else {
				f3 = paramFloat / f2 * f1;
				f4 = paramFloat;
			}
			Bitmap localBitmap = Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
			bitmap.recycle();
			bitmap = null;
			return localBitmap;
		}
		return bitmap;
	}

	/**
	 * 从raw资源中解码bitmap
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap decodeBitmap(Context context, int resId) {
		InputStream inputStream = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inDither = false;
			inputStream = context.getResources().openRawResource(resId);
			BitmapFactory.decodeStream(inputStream, null, options);
			DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			return BitmapFactory.decodeStream(inputStream, null, getScaleOptions(displayMetrics.widthPixels, displayMetrics.heightPixels, options));
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 从文件系统中解码bitmap
	 * 
	 * @param context
	 * @param pathName
	 * @return
	 */
	public static Bitmap decodeBitmap(Context context, String pathName) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return decodeBitmap(pathName, dm.widthPixels, dm.heightPixels);
	}

	/**
	 * 从文件系统中解码bitmap
	 * 
	 * @param pathName
	 * @return
	 */
	public static Bitmap decodeBitmap(String pathName) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return BitmapFactory.decodeFile(pathName, options);
	}

	/**
	 * @param pathName
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap decodeBitmap(String pathName, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false;
		return BitmapFactory.decodeFile(pathName, getScaleOptions(width, height, options));
	}

	/**
	 * @param pathName
	 * @param width
	 * @param height
	 * @param config
	 * @return
	 */
	public static Bitmap decodeBitmap(String pathName, int width, int height, Bitmap.Config config) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false;
		BitmapFactory.decodeFile(pathName, options);
		options = getScaleOptions(width, height, options);
		options.inPreferredConfig = config;
		return BitmapFactory.decodeFile(pathName, options);
	}

	/**
	 * 从输入流中解码bitmap
	 * 
	 * @param in
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap decodeBitmap(InputStream in, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false;
		BitmapFactory.decodeStream(in, null, options);
		return BitmapFactory.decodeStream(in, null, getScaleOptions(width, height, options));
	}

	/**
	 * 从字节数组中解码bitmap
	 * 
	 * @param b
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap decodeBitmap(byte[] b, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false;
		BitmapFactory.decodeByteArray(b, 0, b.length, options);
		return BitmapFactory.decodeByteArray(b, 0, b.length, getScaleOptions(width, height, options));
	}

	/**
	 * 从文件系统解码bitmap获取缩略图
	 * 
	 * @param pathName
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getThumbnail(String pathName, int width, int height) {
		Bitmap bitmap = decodeBitmap(pathName, width, height);
		Bitmap result = ThumbnailUtils.extractThumbnail(bitmap, width, height, 2);
		bitmap.recycle();
		bitmap = null;
		return result;
	}

	/**
	 * @param width
	 * @param height
	 * @param paramOptions
	 * @return
	 */
	private static BitmapFactory.Options getScaleOptions(int width, int height, BitmapFactory.Options paramOptions) {
		int i = 1;
		int j = paramOptions.outWidth;
		int k = paramOptions.outHeight;
		while ((j / 2 > width) && (k / 2 > height)) {
			j /= 2;
			k /= 2;
			i *= 2;
		}
		if ((j > width) || (k > height)) {
			int m = Math.round(j / width);
			int n = Math.round(k / height);
			int i1 = m > n ? m : n;
			if (i > 1) {
				i += i1;
			} else {
				i = i1;
			}
		}
		paramOptions.inSampleSize = i;
		paramOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		paramOptions.inTempStorage = new byte[65536];
		paramOptions.inPurgeable = true;
		paramOptions.inInputShareable = true;
		paramOptions.inJustDecodeBounds = false;
		paramOptions.inDither = false;
		try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(paramOptions, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return paramOptions;
	}

	/**
	 * 从bitmap竖直方向创建倒影,只返回倒影部分
	 * 
	 * @param bitmap
	 * @param heightScale
	 * @return
	 */
	public static Bitmap createReflectedImage(Bitmap bitmap, float heightScale) {
		int i = bitmap.getWidth();
		int j = bitmap.getHeight();
		int k = Math.round(j * heightScale);
		Matrix matrix = new Matrix();
		matrix.preScale(1.0F, -1.0F);
		Bitmap b = Bitmap.createBitmap(bitmap, 0, j - k, i, k, matrix, false);
		bitmap.recycle();
		bitmap = null;
		Bitmap result = Bitmap.createBitmap(i, k, Bitmap.Config.ARGB_8888);
		Canvas localCanvas = new Canvas(result);
		localCanvas.drawBitmap(b, 0.0F, 0.0F, null);
		Paint localPaint = new Paint();
		LinearGradient localLinearGradient = new LinearGradient(0.0F, 0.0F, 0.0F, result.getHeight(), -1, 16777215, Shader.TileMode.CLAMP);
		localPaint.setShader(localLinearGradient);
		localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		localCanvas.drawRect(0.0F, 0.0F, result.getWidth(), result.getHeight(), localPaint);
		b.recycle();
		b = null;
		return result;
	}

	/**
	 * 从bitmap竖直方向创建倒影,返回原图+倒影
	 * 
	 * @param bitmap
	 * @param heightScale
	 * @param heightExt
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap, float heightScale, int heightExt) {
		int i = bitmap.getWidth();
		int j = bitmap.getHeight();
		int k = Math.round(j * heightScale);
		Matrix matrix = new Matrix();
		matrix.preScale(1.0F, -1.0F);
		Bitmap b = Bitmap.createBitmap(bitmap, 0, j - k, i, k, matrix, false);
		Bitmap result = Bitmap.createBitmap(i, j + k + heightExt, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
		Paint p = new Paint();
		p.setColor(-1);
		canvas.drawRect(0.0F, j, i, j + heightExt, p);
		canvas.drawBitmap(b, 0.0F, j + heightExt, null);
		Paint paint = new Paint();
		LinearGradient localLinearGradient = new LinearGradient(0.0F, bitmap.getHeight(), 0.0F, result.getHeight() + heightExt, 1895825407, 16777215, Shader.TileMode.CLAMP);
		paint.setShader(localLinearGradient);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		canvas.drawRect(0.0F, j, i, result.getHeight() + heightExt, paint);
		b.recycle();
		b = null;
		bitmap.recycle();
		bitmap = null;
		return result;
	}

	/**
	 * 获取圆角图片
	 * 
	 * @param bitmap
	 * @param r
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int r) {
		Bitmap localBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas localCanvas = new Canvas(localBitmap);
		Paint localPaint = new Paint();
		Rect localRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF localRectF = new RectF(localRect);
		localPaint.setAntiAlias(true);
		localCanvas.drawARGB(0, 0, 0, 0);
		localPaint.setColor(-12434878);
		localCanvas.drawRoundRect(localRectF, r, r, localPaint);
		localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		localCanvas.drawBitmap(bitmap, localRect, localRect, localPaint);
		bitmap.recycle();
		bitmap = null;
		return localBitmap;
	}

	/**
	 * @param activity
	 * @param uri
	 * @return
	 */
	public static String getRealPathFromURI(Activity activity, Uri uri) {
		Cursor cursor = activity.managedQuery(uri, new String[] { "_data" }, null, null, null);
		if ((cursor == null) || (cursor.getCount() == 0)) {
			return null;
		}
		int i = cursor.getColumnIndex("_data");
		cursor.moveToNext();
		String str = cursor.getString(i);
		cursor.close();
		return str;
	}

	/**
	 * 获取bitmap大小
	 * 
	 * @param bitmap
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static int getBitmapSize(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= 12) {
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * 转换drawable到bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap result = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(result);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return result;
	}

	/**
	 * 获取bitmap对应字节数组
	 * 
	 * @param bitmap
	 * @return
	 */
	public static byte[] bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * 将bitmap写到文件中
	 * @param bitmap
	 * @param filePath
	 * @throws Exception
	 */
	public static void writeBitmap2Local(Bitmap bitmap, String filePath) throws Exception {
		if ((null == filePath) || ("".equals(filePath))) {
			return;
		}
		File file = new File(filePath);
		FileOutputStream fileOutputStream = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw e;
		} finally {
			if (fileOutputStream != null)
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
