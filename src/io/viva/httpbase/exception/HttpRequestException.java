package io.viva.httpbase.exception;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import android.content.Context;
import android.widget.Toast;

public class HttpRequestException extends BaseException {

	private static final long serialVersionUID = 1L;

	public HttpRequestException() {
	}

	public HttpRequestException(String paramString) {
		super(paramString);
	}

	public HttpRequestException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	public HttpRequestException(int paramInt, String paramString) {
		super(paramInt, paramString);
	}

	public HttpRequestException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

	public HttpRequestException(int paramInt, String paramString, Throwable paramThrowable) {
		super(paramInt, paramString, paramThrowable);
	}

	public boolean handle(Context paramContext) {
		if (this.errorMessage != null)
			Toast.makeText(paramContext, this.errorMessage, 1).show();
		return true;
	}

	public static boolean isHttpException(Exception paramException) {
		return ((paramException instanceof ConnectTimeoutException)) || ((paramException instanceof HttpHostConnectException))
				|| ((paramException instanceof UnknownHostException)) || ((paramException instanceof ConnectException)) || ((paramException instanceof SocketException));
	}
}