package com.mms.cloud.dto;

import java.io.Serializable;

public class OrderStatusMonitorDTO implements Serializable{
	
	private static final long serialVersionUID = -6127196101102494843L;
	
	private String totalPrice;
	
	private String orderCode;
	
	private String user;
	
	private String province;
	
	private String createTime;
	
	private String cancelFlag;
	
	private String payStatus="";
	
	private String payTime="";
	
	private String sendStatus="";
	
	private String sendTime="";
	
	private String receviedStatus="";
	
	private String receviedTime="";

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getReceviedStatus() {
		return receviedStatus;
	}

	public void setReceviedStatus(String receviedStatus) {
		this.receviedStatus = receviedStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getReceviedTime() {
		return receviedTime;
	}

	public void setReceviedTime(String receviedTime) {
		this.receviedTime = receviedTime;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

}
