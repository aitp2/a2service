package com.mms.cloud.ruleengine;

public class OrderStatusFact {

	private String orderCode;
	
	private String orderStatus;
	
	private String province;
	
	private String createTime;
	
	private String cancelFlag;
	
	private String payStatus="";
	
	private String payTime="";
	
	private String sendStatus="";
	
	private String sendTime="";
	
	private String receviedStatus="";
	
	private String receviedTime="";

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getReceviedStatus() {
		return receviedStatus;
	}

	public void setReceviedStatus(String receviedStatus) {
		this.receviedStatus = receviedStatus;
	}

	public String getReceviedTime() {
		return receviedTime;
	}

	public void setReceviedTime(String receviedTime) {
		this.receviedTime = receviedTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
}
