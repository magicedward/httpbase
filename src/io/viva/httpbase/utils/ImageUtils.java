package io.viva.httpbase.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
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
import android.util.Log;

public class ImageUtils {
	public static Bitmap getScaleBitmap(Bitmap paramBitmap, int paramInt1, int paramInt2) {
		int i = paramBitmap.getWidth();
		int j = paramBitmap.getHeight();
		float f1 = paramInt1 / i;
		float f2 = paramInt2 / j;
		Matrix localMatrix = new Matrix();
		localMatrix.postScale(f1, f2);
		Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, i, j, localMatrix, true);
		paramBitmap.recycle();
		paramBitmap = null;
		return localBitmap;
	}

	public static Bitmap getScaleBitmapAdjust(Bitmap paramBitmap, int paramInt1, int paramInt2) {
		if (paramBitmap == null) {
			return null;
		}
		int i = paramBitmap.getWidth();
		int j = paramBitmap.getHeight();
		Matrix localMatrix = new Matrix();
		float f1 = paramInt1 / i;
		float f2 = paramInt2 / j;
		if (f1 < f2) {
			if ((f1 < 0.99F) || (f1 > 1.0F)) {
				localMatrix.postScale(f1, f1);
			} else {
				return paramBitmap;
			}
		} else if ((f2 < 0.99F) || (f2 > 1.0F)) {
			localMatrix.postScale(f2, f2);
		} else {
			return paramBitmap;
		}
		Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, i, j, localMatrix, true);
		paramBitmap.recycle();
		paramBitmap = null;
		return localBitmap;
	}

	public static Bitmap getScaleBitmapAdjust(Bitmap paramBitmap, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2) {
		if (paramBitmap == null) {
			return null;
		}
		int i = paramBitmap.getWidth();
		int j = paramBitmap.getHeight();
		Matrix localMatrix = new Matrix();
		float f1 = paramInt1 / i;
		float f2 = paramInt2 / j;
		if (f1 < f2) {
			if ((f1 < 0.99F) || (f1 > 1.0F)) {
				localMatrix.postScale(f1, f1, paramFloat1, paramFloat2);
			} else {
				return paramBitmap;
			}
		} else if ((f2 < 0.99F) || (f2 > 1.0F)) {
			localMatrix.postScale(f2, f2, paramFloat1, paramFloat2);
		} else {
			return paramBitmap;
		}
		Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, i, j, localMatrix, true);
		paramBitmap.recycle();
		paramBitmap = null;
		return localBitmap;
	}

	public static Bitmap getCompressBitmap(Bitmap paramBitmap, float paramFloat) {
		if (paramBitmap == null) {
			return null;
		}
		float f1 = paramBitmap.getWidth();
		float f2 = paramBitmap.getHeight();
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
			Bitmap localBitmap = Bitmap.createScaledBitmap(paramBitmap, (int) f3, (int) f4, false);
			paramBitmap.recycle();
			paramBitmap = null;
			return localBitmap;
		}
		return paramBitmap;
	}

	public static Bitmap decodeBitmap(Context paramContext, int paramInt) {
		InputStream localInputStream = null;
		try {
			BitmapFactory.Options localOptions = new BitmapFactory.Options();
			localOptions.inJustDecodeBounds = true;
			localOptions.inDither = false;
			localInputStream = paramContext.getResources().openRawResource(paramInt);
			BitmapFactory.decodeStream(localInputStream, null, localOptions);
			DisplayMetrics localDisplayMetrics = paramContext.getResources().getDisplayMetrics();
			Bitmap localBitmap1 = BitmapFactory.decodeStream(localInputStream, null,
					getScaleOptions(localDisplayMetrics.widthPixels, localDisplayMetrics.heightPixels, localOptions));
			Bitmap localBitmap2 = localBitmap1;
			return localBitmap2;
		} finally {
			try {
				if (localInputStream != null) {
					localInputStream.close();
					localInputStream = null;
				}
			} catch (IOException localIOException2) {
			}
		}
	}

	public static Bitmap decodeBitmap(Context paramContext, String paramString) {
		DisplayMetrics localDisplayMetrics = paramContext.getResources().getDisplayMetrics();
		return decodeBitmap(paramString, localDisplayMetrics.widthPixels, localDisplayMetrics.heightPixels);
	}

	public static Bitmap decodeBitmap(String paramString) {
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inDither = false;
		localOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		localOptions.inPurgeable = true;
		localOptions.inInputShareable = true;
		try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(localOptions, true);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			localIllegalArgumentException.printStackTrace();
		} catch (SecurityException localSecurityException) {
			localSecurityException.printStackTrace();
		} catch (IllegalAccessException localIllegalAccessException) {
			localIllegalAccessException.printStackTrace();
		} catch (NoSuchFieldException localNoSuchFieldException) {
			localNoSuchFieldException.printStackTrace();
		}
		return BitmapFactory.decodeFile(paramString, localOptions);
	}

	public static Bitmap decodeBitmap(String paramString, int paramInt1, int paramInt2) {
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inJustDecodeBounds = true;
		localOptions.inDither = false;
		BitmapFactory.decodeFile(paramString, localOptions);
		return BitmapFactory.decodeFile(paramString, getScaleOptions(paramInt1, paramInt2, localOptions));
	}

	public static Bitmap decodeBitmap(String paramString, int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inJustDecodeBounds = true;
		localOptions.inDither = false;
		BitmapFactory.decodeFile(paramString, localOptions);
		localOptions = getScaleOptions(paramInt1, paramInt2, localOptions);
		localOptions.inPreferredConfig = paramConfig;
		return BitmapFactory.decodeFile(paramString, localOptions);
	}

	public static Bitmap decodeBitmap(InputStream paramInputStream, int paramInt1, int paramInt2) {
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inJustDecodeBounds = true;
		localOptions.inDither = false;
		BitmapFactory.decodeStream(paramInputStream, null, localOptions);
		return BitmapFactory.decodeStream(paramInputStream, null, getScaleOptions(paramInt1, paramInt2, localOptions));
	}

	public static Bitmap decodeBitmap(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inJustDecodeBounds = true;
		localOptions.inDither = false;
		BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length, localOptions);
		return BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length, getScaleOptions(paramInt1, paramInt2, localOptions));
	}

	public static Bitmap getThumbnail(String paramString, int paramInt1, int paramInt2) {
		Bitmap localBitmap1 = decodeBitmap(paramString, paramInt1, paramInt2);
		Bitmap localBitmap2 = ThumbnailUtils.extractThumbnail(localBitmap1, paramInt1, paramInt2, 2);
		localBitmap1.recycle();
		localBitmap1 = null;
		return localBitmap2;
	}

	private static BitmapFactory.Options getScaleOptions(int paramInt1, int paramInt2, BitmapFactory.Options paramOptions) {
		int i = 1;
		int j = paramOptions.outWidth;
		int k = paramOptions.outHeight;
		while ((j / 2 > paramInt1) && (k / 2 > paramInt2)) {
			j /= 2;
			k /= 2;
			i *= 2;
		}
		if ((j > paramInt1) || (k > paramInt2)) {
			int m = Math.round(j / paramInt1);
			int n = Math.round(k / paramInt2);
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
		} catch (IllegalArgumentException localIllegalArgumentException) {
			localIllegalArgumentException.printStackTrace();
		} catch (SecurityException localSecurityException) {
			localSecurityException.printStackTrace();
		} catch (IllegalAccessException localIllegalAccessException) {
			localIllegalAccessException.printStackTrace();
		} catch (NoSuchFieldException localNoSuchFieldException) {
			localNoSuchFieldException.printStackTrace();
		}
		return paramOptions;
	}

	public static Bitmap createReflectedImage(Bitmap paramBitmap, float paramFloat) {
		int i = paramBitmap.getWidth();
		int j = paramBitmap.getHeight();
		int k = Math.round(j * paramFloat);
		Matrix localMatrix = new Matrix();
		localMatrix.preScale(1.0F, -1.0F);
		Bitmap localBitmap1 = Bitmap.createBitmap(paramBitmap, 0, j - k, i, k, localMatrix, false);
		paramBitmap.recycle();
		paramBitmap = null;
		Bitmap localBitmap2 = Bitmap.createBitmap(i, k, Bitmap.Config.ARGB_8888);
		Canvas localCanvas = new Canvas(localBitmap2);
		localCanvas.drawBitmap(localBitmap1, 0.0F, 0.0F, null);
		Paint localPaint = new Paint();
		LinearGradient localLinearGradient = new LinearGradient(0.0F, 0.0F, 0.0F, localBitmap2.getHeight(), -1, 16777215, Shader.TileMode.CLAMP);
		localPaint.setShader(localLinearGradient);
		localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		localCanvas.drawRect(0.0F, 0.0F, localBitmap2.getWidth(), localBitmap2.getHeight(), localPaint);
		localBitmap1.recycle();
		localBitmap1 = null;
		return localBitmap2;
	}

	public static Bitmap createReflectionImageWithOrigin(Bitmap paramBitmap, float paramFloat, int paramInt) {
		int i = paramBitmap.getWidth();
		int j = paramBitmap.getHeight();
		int k = Math.round(j * paramFloat);
		Matrix localMatrix = new Matrix();
		localMatrix.preScale(1.0F, -1.0F);
		Bitmap localBitmap1 = Bitmap.createBitmap(paramBitmap, 0, j - k, i, k, localMatrix, false);
		Bitmap localBitmap2 = Bitmap.createBitmap(i, j + k + paramInt, Bitmap.Config.ARGB_8888);
		Canvas localCanvas = new Canvas(localBitmap2);
		localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, null);
		Paint localPaint1 = new Paint();
		localPaint1.setColor(-1);
		localCanvas.drawRect(0.0F, j, i, j + paramInt, localPaint1);
		localCanvas.drawBitmap(localBitmap1, 0.0F, j + paramInt, null);
		Paint localPaint2 = new Paint();
		LinearGradient localLinearGradient = new LinearGradient(0.0F, paramBitmap.getHeight(), 0.0F, localBitmap2.getHeight() + paramInt, 1895825407, 16777215,
				Shader.TileMode.CLAMP);
		localPaint2.setShader(localLinearGradient);
		localPaint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		localCanvas.drawRect(0.0F, j, i, localBitmap2.getHeight() + paramInt, localPaint2);
		localBitmap1.recycle();
		localBitmap1 = null;
		paramBitmap.recycle();
		paramBitmap = null;
		return localBitmap2;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap paramBitmap, int paramInt) {
		Bitmap localBitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas localCanvas = new Canvas(localBitmap);
		Paint localPaint = new Paint();
		Rect localRect = new Rect(0, 0, paramBitmap.getWidth(), paramBitmap.getHeight());
		RectF localRectF = new RectF(localRect);
		localPaint.setAntiAlias(true);
		localCanvas.drawARGB(0, 0, 0, 0);
		localPaint.setColor(-12434878);
		localCanvas.drawRoundRect(localRectF, paramInt, paramInt, localPaint);
		localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		localCanvas.drawBitmap(paramBitmap, localRect, localRect, localPaint);
		paramBitmap.recycle();
		paramBitmap = null;
		return localBitmap;
	}

	public static String getRealPathFromURI(Activity paramActivity, Uri paramUri) {
		Cursor localCursor = paramActivity.managedQuery(paramUri, new String[] { "_data" }, null, null, null);
		if ((localCursor == null) || (localCursor.getCount() == 0)) {
			Log.i("aabb", "===========not find pic");
			return null;
		}
		int i = localCursor.getColumnIndex("_data");
		localCursor.moveToNext();
		String str = localCursor.getString(i);
		localCursor.close();
		return str;
	}

	@SuppressLint("NewApi")
	public static int getBitmapSize(Bitmap paramBitmap) {
		if (Build.VERSION.SDK_INT >= 12) {
			return paramBitmap.getByteCount();
		}
		return paramBitmap.getRowBytes() * paramBitmap.getHeight();
	}

	public static Bitmap drawable2Bitmap(Drawable paramDrawable) {
		Bitmap localBitmap = Bitmap.createBitmap(paramDrawable.getIntrinsicWidth(), paramDrawable.getIntrinsicHeight(), paramDrawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas localCanvas = new Canvas(localBitmap);
		paramDrawable.setBounds(0, 0, paramDrawable.getIntrinsicWidth(), paramDrawable.getIntrinsicHeight());
		paramDrawable.draw(localCanvas);
		return localBitmap;
	}

	private byte[] bitmap2Bytes(Bitmap paramBitmap) {
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
		return localByteArrayOutputStream.toByteArray();
	}

	public static void writeBitmap2Local(Bitmap paramBitmap, String paramString) throws Exception {
		if ((null == paramString) || ("".equals(paramString))) {
			return;
		}
		File localFile = new File(paramString);
		FileOutputStream localFileOutputStream = null;
		try {
			if (!localFile.exists()) {
				localFile.createNewFile();
			}
			localFileOutputStream = new FileOutputStream(localFile);
			paramBitmap.compress(Bitmap.CompressFormat.JPEG, 80, localFileOutputStream);
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} catch (IOException localIOException3) {
			throw localIOException3;
		} finally {
			if (localFileOutputStream != null)
				try {
					localFileOutputStream.flush();
					localFileOutputStream.close();
				} catch (IOException localIOException4) {
					localIOException4.printStackTrace();
				}
		}
	}
}
