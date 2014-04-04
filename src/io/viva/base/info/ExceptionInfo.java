package io.viva.base.info;

import java.io.Serializable;

public class ExceptionInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	public String exception;
	public String type;
	public String createTime;

	private ExceptionInfo() {
	}

	public ExceptionInfo(String paramString1, String paramString2, long paramLong) {
		this.exception = paramString1;
		this.type = paramString2;
		this.createTime = String.valueOf(paramLong);
	}
}