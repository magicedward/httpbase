package io.viva.httpbase.utils;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class NinePatchHelper {

	/**
	 * 从Drawable创建点九图片
	 * @param drawable
	 * @return
	 */
	public static Drawable process(Drawable drawable) {
		Drawable d = drawable;
		if ((drawable instanceof BitmapDrawable)) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			if ((bitmap != null) && (isNinePatch(bitmap))) {
				byte[] b = createChunk(bitmap);
				d = new NinePatchDrawable(cropNinePatch(bitmap), b, new Rect(1, 1, 1, 1), "");
			}
		}
		return d;
	}

	/**
	 * 从bitmap创建点九图片
	 * @param bitmap
	 * @return
	 */
	public static Drawable process(Bitmap bitmap) {
		Drawable d = null;
		if (bitmap != null) {
			if (isNinePatch(bitmap)) {
				byte[] b = createChunk(bitmap);
				d = new NinePatchDrawable(cropNinePatch(bitmap), b, new Rect(1, 1, 1, 1), "");
			} else {
				d = new BitmapDrawable(bitmap);
			}
		}
		return d;
	}

	/**
	 * @param bitmap
	 * @return 是否点九图片
	 */
	private static boolean isNinePatch(Bitmap bitmap) {
		boolean bool = true;
		int i = bitmap.getWidth();
		int j = bitmap.getHeight();
		int k = 0;
		int m = 0;
		int n = 0;
		int i1 = 0;
		if ((i >= 3) && (j >= 3)) {
			int i3;
			for (int i2 = 0; i2 < i; i2++) {
				i3 = bitmap.getPixel(i2, 0);
				k += (i3 == 0 ? 0 : 1);
				if (!isValidColor(i3)) {
					bool = false;
					break;
				}
				i3 = bitmap.getPixel(i2, j - 1);
				i1 += (i3 == 0 ? 0 : 1);
				if (!isValidColor(i3)) {
					bool = false;
					break;
				}
			}
			if (bool)
				for (int i2 = 0; i2 < j; i2++) {
					i3 = bitmap.getPixel(0, i2);
					m += (i3 == 0 ? 0 : 1);
					if (!isValidColor(i3)) {
						bool = false;
						break;
					}
					i3 = bitmap.getPixel(i - 1, i2);
					n += (i3 == 0 ? 0 : 1);
					if (!isValidColor(i3)) {
						bool = false;
						break;
					}
				}
		} else {
			bool = false;
		}
		if (m + k + n + i1 == 0) {
			bool = false;
		}
		return bool;
	}

	private static boolean isValidColor(int paramInt) {
		return (paramInt == 0) || (paramInt == Color.BLACK);
	}

	/**
	 * 修剪bitmap
	 * @param bitmap
	 * @return 
	 */
	private static Bitmap cropNinePatch(Bitmap bitmap) {
		Bitmap b = null;
		b = Bitmap.createBitmap(bitmap.getWidth() - 2, bitmap.getHeight() - 2, bitmap.getConfig());
		int[] arrayOfInt = new int[b.getWidth() * b.getHeight()];
		bitmap.getPixels(arrayOfInt, 0, b.getWidth(), 1, 1, b.getWidth(), b.getHeight());
		b.setPixels(arrayOfInt, 0, b.getWidth(), 0, 0, b.getWidth(), b.getHeight());
		return b;
	}

	static byte[] createChunk(Bitmap bitmap) {
		byte[] arrayOfByte = null;
		int i = 0;
		int j = 0;
		int k = 1;
		int m = bitmap.getPixel(0, 0);
		ArrayList<SegmentColor> list = new ArrayList<SegmentColor>();
		for (int n = 1; n < bitmap.getWidth(); n++) {
			int i1 = bitmap.getPixel(n, 0);
			if (i1 != m) {
				SegmentColor sc = new SegmentColor();
				sc.index = n;
				sc.color = m;
				list.add(sc);
				i++;
				m = i1;
			}
		}
		m = bitmap.getPixel(0, 0);
		ArrayList<SegmentColor> list2 = new ArrayList<SegmentColor>();
		for (int i1 = 1; i1 < bitmap.getHeight(); i1++) {
			int i2 = bitmap.getPixel(0, i1);
			if (i2 != m) {
				SegmentColor localSegmentColor2 = new SegmentColor();
				localSegmentColor2.index = i1;
				localSegmentColor2.color = m;
				list2.add(localSegmentColor2);
				j++;
				m = i2;
			}
		}
		ArrayList<Integer> list3 = new ArrayList<Integer>();
		for (int i2 = 0; i2 < list2.size(); i2++) {
			int i3 = ((SegmentColor) list2.get(i2)).color;
			for (int i4 = 0; i4 < list.size(); i4++)
				if (i3 == 0) {
					list3.add(Integer.valueOf(((SegmentColor) list.get(i4)).color == 0 ? 0 : 1));
				} else {
					list3.add(Integer.valueOf(((SegmentColor) list2.get(i2)).color == 0 ? 0 : 1));
				}
			if (i3 == 0) {
				list3.add(Integer.valueOf(((Integer) list3.get(list3.size() - 1)).intValue() == 1 ? 0 : 1));
			} else {
				list3.add(Integer.valueOf(1));
			}
		}
		for (int i2 = 0; i2 < list.size() + 1; i2++) {
			list3.add(list3.get(i2));
		}
		k = list3.size();
		int i2 = 32 + i * 32 + j * 32 + k * 32;
		arrayOfByte = new byte[i2];
		arrayOfByte[0] = 0;
		arrayOfByte[1] = ((byte) (0xFF & i));
		arrayOfByte[2] = ((byte) (0xFF & j));
		arrayOfByte[3] = ((byte) (0xFF & k));
		int i3 = 32;
		for (int i4 = 0; i4 < list.size(); i4++) {
			toBytes(arrayOfByte, i3 + i4 * 4, ((SegmentColor) list.get(i4)).index - 1);
		}
		int i4 = i3 + i * 4;
		for (int i5 = 0; i5 < list2.size(); i5++) {
			toBytes(arrayOfByte, i4 + i5 * 4, ((SegmentColor) list2.get(i5)).index - 1);
		}
		int i5 = i4 + j * 4;
		for (int i6 = 0; i6 < list3.size(); i6++) {
			toBytes(arrayOfByte, i5 + i6 * 4, 1);
		}
		return arrayOfByte;
	}

	private static int toInt(byte[] b, int paramInt) {
		int i = 0;
		i |= b[paramInt];
		i |= b[(paramInt + 1)] << 8;
		i |= b[(paramInt + 2)] << 16;
		i |= b[(paramInt + 3)] << 24;
		return i;
	}

	private static void toBytes(byte[] b, int paramInt1, int paramInt2) {
		b[paramInt1] = ((byte) (0xFF & paramInt2));
		b[(paramInt1 + 1)] = ((byte) ((0xFF00 & paramInt2) >> 8));
		b[(paramInt1 + 2)] = ((byte) ((0xFF0000 & paramInt2) >> 16));
		b[(paramInt1 + 3)] = ((byte) ((0xFF000000 & paramInt2) >> 24));
	}
}
