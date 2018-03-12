package com.mms.cloud.dto;

import java.io.Serializable;

public class ResultData<T> implements Serializable {

	public ResultData(String message, boolean isSucessful) {
		super();
		this.message = message;
		this.isSucessful = isSucessful;
	}
	
	public ResultData(String message, boolean isSucessful, Integer code) {
		super();
		this.message = message;
		this.isSucessful = isSucessful;
		this.code = code;
	}

	/**
	 *  百万序列号
	 */
	private static final long serialVersionUID = 1000000L;
	
	private boolean isSucessful;

	private String message;
	
	private Integer code;
	
	private T serializableData;

	public ResultData(boolean isSucessful, String message, Integer code, T serializableData) {
		super();
		this.isSucessful = isSucessful;
		this.message = message;
		this.code = code;
		this.serializableData = serializableData;
	}

	public ResultData(boolean isSucessful, String message, Integer code) {
		super();
		this.isSucessful = isSucessful;
		this.message = message;
		this.code = code;
	}

	public boolean isSucessful() {
		return isSucessful;
	}

	public void setSucessful(boolean isSucessful) {
		this.isSucessful = isSucessful;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public T getSerializableData() {
		return serializableData;
	}

	public void setSerializableData(T serializableData) {
		this.serializableData = serializableData;
	}

}
