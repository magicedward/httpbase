package io.viva.httpbase.info;

import java.io.Serializable;

public class ExceptionInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	public String exception;
	public String type;
	public String createTime;

	private ExceptionInfo() {
	}

	public ExceptionInfo(String exception, String type, long createTime) {
		this.exception = exception;
		this.type = type;
		this.createTime = String.valueOf(createTime);
	}
}