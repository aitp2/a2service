package com.mms.cloud.dto;

public class ExceptionEntity {
	private String logsource;
	
	private String logtime;
	
	private String exception;
	
	private String message;

	public String getLogsource() {
		return logsource;
	}

	public void setLogsource(String logsource) {
		this.logsource = logsource;
	}

	public String getLogtime() {
		return logtime;
	}

	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
