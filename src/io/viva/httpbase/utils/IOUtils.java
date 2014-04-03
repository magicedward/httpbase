package io.viva.httpbase.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
	public static final int IO_BUFFER_SIZE = 16 * 10244;

	public static String readString(InputStream paramInputStream) throws IOException {
		return new String(readBytes(paramInputStream));
	}

	public static byte[] readBytes(InputStream paramInputStream) throws IOException {
		byte[] arrayOfByte1 = new byte[0];
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		byte[] arrayOfByte2 = new byte[IO_BUFFER_SIZE];
		int i = -1;
		try {
			while ((i = paramInputStream.read(arrayOfByte2)) != -1) {
				localByteArrayOutputStream.write(arrayOfByte2, 0, i);
			}
			arrayOfByte1 = localByteArrayOutputStream.toByteArray();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
			throw localIOException;
		} finally {
			try {
				if (localByteArrayOutputStream != null) {
					localByteArrayOutputStream.close();
					localByteArrayOutputStream = null;
				}
				if (paramInputStream != null) {
					paramInputStream.close();
					paramInputStream = null;
				}
			} catch (Exception localException2) {
			}
		}
		return arrayOfByte1;
	}

	public static void writeToLocal(InputStream paramInputStream, String paramString) throws IOException {
		File localFile = new File(paramString);
		FileOutputStream localFileOutputStream = null;
		if (!localFile.exists()) {
			File localObject1 = localFile.getParentFile();
			if (!((File) localObject1).exists()) {
				((File) localObject1).mkdirs();
			}
			localFile.createNewFile();
		}
		localFileOutputStream = new FileOutputStream(localFile);
		Object localObject1 = new byte[IO_BUFFER_SIZE];
		int i = -1;
		try {
			while ((i = paramInputStream.read((byte[]) localObject1)) != -1) {
				localFileOutputStream.write((byte[]) localObject1, 0, i);
			}
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
			throw localIOException;
		} finally {
			try {
				if (localFileOutputStream != null) {
					localFileOutputStream.close();
					localFileOutputStream = null;
				}
				if (paramInputStream != null) {
					paramInputStream.close();
					paramInputStream = null;
				}
			} catch (Exception localException2) {
			}
		}
	}
}
