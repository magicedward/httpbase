package io.viva.httpbase.utils;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class NinePatchHelper {
	private static final String LCAT = "TiNinePatch";

	public static Drawable process(Drawable paramDrawable) {
		Drawable localObject = paramDrawable;
		if ((paramDrawable instanceof BitmapDrawable)) {
			Bitmap localBitmap = ((BitmapDrawable) paramDrawable).getBitmap();
			if ((localBitmap != null) && (isNinePatch(localBitmap))) {
				byte[] arrayOfByte = createChunk(localBitmap);
				localObject = new NinePatchDrawable(cropNinePatch(localBitmap), arrayOfByte, new Rect(1, 1, 1, 1), "");
			}
		}
		return localObject;
	}

	public static Drawable process(Bitmap paramBitmap) {
		Drawable localObject = null;
		if (paramBitmap != null) {
			if (isNinePatch(paramBitmap)) {
				byte[] arrayOfByte = createChunk(paramBitmap);
				localObject = new NinePatchDrawable(cropNinePatch(paramBitmap), arrayOfByte, new Rect(1, 1, 1, 1), "");
			} else {
				localObject = new BitmapDrawable(paramBitmap);
			}
		}
		return localObject;
	}

	private static boolean isNinePatch(Bitmap paramBitmap) {
		boolean bool = true;
		int i = paramBitmap.getWidth();
		int j = paramBitmap.getHeight();
		int k = 0;
		int m = 0;
		int n = 0;
		int i1 = 0;
		if ((i >= 3) && (j >= 3)) {
			int i3;
			for (int i2 = 0; i2 < i; i2++) {
				i3 = paramBitmap.getPixel(i2, 0);
				k += (i3 == 0 ? 0 : 1);
				if (!isValidColor(i3)) {
					bool = false;
					break;
				}
				i3 = paramBitmap.getPixel(i2, j - 1);
				i1 += (i3 == 0 ? 0 : 1);
				if (!isValidColor(i3)) {
					bool = false;
					break;
				}
			}
			if (bool)
				for (int i2 = 0; i2 < j; i2++) {
					i3 = paramBitmap.getPixel(0, i2);
					m += (i3 == 0 ? 0 : 1);
					if (!isValidColor(i3)) {
						bool = false;
						break;
					}
					i3 = paramBitmap.getPixel(i - 1, i2);
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

	private static Bitmap cropNinePatch(Bitmap paramBitmap) {
		Bitmap localBitmap = null;
		localBitmap = Bitmap.createBitmap(paramBitmap.getWidth() - 2, paramBitmap.getHeight() - 2, paramBitmap.getConfig());
		int[] arrayOfInt = new int[localBitmap.getWidth() * localBitmap.getHeight()];
		paramBitmap.getPixels(arrayOfInt, 0, localBitmap.getWidth(), 1, 1, localBitmap.getWidth(), localBitmap.getHeight());
		localBitmap.setPixels(arrayOfInt, 0, localBitmap.getWidth(), 0, 0, localBitmap.getWidth(), localBitmap.getHeight());
		return localBitmap;
	}

	static byte[] createChunk(Bitmap paramBitmap) {
		byte[] arrayOfByte = null;
		int i = 0;
		int j = 0;
		int k = 1;
		int m = paramBitmap.getPixel(0, 0);
		ArrayList<SegmentColor> localArrayList1 = new ArrayList<SegmentColor>();
		for (int n = 1; n < paramBitmap.getWidth(); n++) {
			int i1 = paramBitmap.getPixel(n, 0);
			if (i1 != m) {
				SegmentColor localSegmentColor1 = new SegmentColor();
				localSegmentColor1.index = n;
				localSegmentColor1.color = m;
				localArrayList1.add(localSegmentColor1);
				i++;
				m = i1;
			}
		}
		m = paramBitmap.getPixel(0, 0);
		ArrayList<SegmentColor> localArrayList2 = new ArrayList<SegmentColor>();
		for (int i1 = 1; i1 < paramBitmap.getHeight(); i1++) {
			int i2 = paramBitmap.getPixel(0, i1);
			if (i2 != m) {
				SegmentColor localSegmentColor2 = new SegmentColor();
				localSegmentColor2.index = i1;
				localSegmentColor2.color = m;
				localArrayList2.add(localSegmentColor2);
				j++;
				m = i2;
			}
		}
		ArrayList<Integer> localArrayList3 = new ArrayList<Integer>();
		for (int i2 = 0; i2 < localArrayList2.size(); i2++) {
			int i3 = ((SegmentColor) localArrayList2.get(i2)).color;
			for (int i4 = 0; i4 < localArrayList1.size(); i4++)
				if (i3 == 0) {
					localArrayList3.add(Integer.valueOf(((SegmentColor) localArrayList1.get(i4)).color == 0 ? 0 : 1));
				} else {
					localArrayList3.add(Integer.valueOf(((SegmentColor) localArrayList2.get(i2)).color == 0 ? 0 : 1));
				}
			if (i3 == 0) {
				localArrayList3.add(Integer.valueOf(((Integer) localArrayList3.get(localArrayList3.size() - 1)).intValue() == 1 ? 0 : 1));
			} else {
				localArrayList3.add(Integer.valueOf(1));
			}
		}
		for (int i2 = 0; i2 < localArrayList1.size() + 1; i2++) {
			localArrayList3.add(localArrayList3.get(i2));
		}
		k = localArrayList3.size();
		int i2 = 32 + i * 32 + j * 32 + k * 32;
		arrayOfByte = new byte[i2];
		arrayOfByte[0] = 0;
		arrayOfByte[1] = ((byte) (0xFF & i));
		arrayOfByte[2] = ((byte) (0xFF & j));
		arrayOfByte[3] = ((byte) (0xFF & k));
		int i3 = 32;
		for (int i4 = 0; i4 < localArrayList1.size(); i4++) {
			toBytes(arrayOfByte, i3 + i4 * 4, ((SegmentColor) localArrayList1.get(i4)).index - 1);
		}
		int i4 = i3 + i * 4;
		for (int i5 = 0; i5 < localArrayList2.size(); i5++) {
			toBytes(arrayOfByte, i4 + i5 * 4, ((SegmentColor) localArrayList2.get(i5)).index - 1);
		}
		int i5 = i4 + j * 4;
		for (int i6 = 0; i6 < localArrayList3.size(); i6++) {
			toBytes(arrayOfByte, i5 + i6 * 4, 1);
		}
		return arrayOfByte;
	}

	private static int toInt(byte[] paramArrayOfByte, int paramInt) {
		int i = 0;
		i |= paramArrayOfByte[paramInt];
		i |= paramArrayOfByte[(paramInt + 1)] << 8;
		i |= paramArrayOfByte[(paramInt + 2)] << 16;
		i |= paramArrayOfByte[(paramInt + 3)] << 24;
		return i;
	}

	private static void toBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
		paramArrayOfByte[paramInt1] = ((byte) (0xFF & paramInt2));
		paramArrayOfByte[(paramInt1 + 1)] = ((byte) ((0xFF00 & paramInt2) >> 8));
		paramArrayOfByte[(paramInt1 + 2)] = ((byte) ((0xFF0000 & paramInt2) >> 16));
		paramArrayOfByte[(paramInt1 + 3)] = ((byte) ((0xFF000000 & paramInt2) >> 24));
	}
}
