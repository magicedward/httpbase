package io.viva.httpbase.exception;

import android.content.Context;

public abstract class BaseException extends Exception {

	private static final long serialVersionUID = 1L;
	protected int code;
	protected String errorMessage;

	public BaseException() {
	}

	public BaseException(String paramString) {
		super(paramString);
	}

	public BaseException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	public BaseException(int paramInt, String paramString) {
		this.code = paramInt;
		this.errorMessage = paramString;
	}

	public BaseException(String paramString, Throwable paramThrowable) {
		super(paramThrowable);
		this.errorMessage = paramString;
	}

	public BaseException(int paramInt, String paramString, Throwable paramThrowable) {
		super(paramThrowable);
		this.code = paramInt;
		this.errorMessage = paramString;
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(int paramInt) {
		this.code = paramInt;
	}

	public String getErrorMessage() {
		if (this.errorMessage != null) {
			return this.errorMessage;
		}
		return super.getMessage();
	}

	public void setErrorMessage(String paramString) {
		this.errorMessage = paramString;
	}

	public abstract boolean handle(Context paramContext);
}