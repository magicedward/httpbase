package io.viva.httpbase.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
	public static final int IO_BUFFER_SIZE = 16 * 10244;

	/**
	 * 从输入流中读取到字符串中
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readString(InputStream in) throws IOException {
		return new String(readBytes(in));
	}

	/**
	 * 从输入流中读取到字节数组中
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes(InputStream in) throws IOException {
		byte[] result = new byte[0];
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] arrayOfByte2 = new byte[IO_BUFFER_SIZE];
		int i = -1;
		try {
			while ((i = in.read(arrayOfByte2)) != -1) {
				byteArrayOutputStream.write(arrayOfByte2, 0, i);
			}
			result = byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
					byteArrayOutputStream = null;
				}
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 将输入流写入文件
	 * 
	 * @param in
	 * @param filePath
	 * @throws IOException
	 */
	public static void writeToLocal(InputStream in, String filePath) throws IOException {
		File file = new File(filePath);
		FileOutputStream fileOutputStream = null;
		if (!file.exists()) {
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			file.createNewFile();
		}
		fileOutputStream = new FileOutputStream(file);
		byte[] b = new byte[IO_BUFFER_SIZE];
		int i = -1;
		try {
			while ((i = in.read(b)) != -1) {
				fileOutputStream.write((byte[]) b, 0, i);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
					fileOutputStream = null;
				}
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
