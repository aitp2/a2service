package com.mms.cloud.dto;

import java.io.Serializable;

public class OrderEntity implements Serializable{

	private static final long serialVersionUID = 3034215225533144349L;

	private String totalPrice;
	
	private String orderStatus;
	
	private String orderCode;
	
	private String user;
	
	private String province;
	
	private String modifyTime;
	
	private String message;
	
	private String sourceSystem;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	@Override
    public String toString() {
        return "OrderEntity{" +
                "orderCode=" + orderCode  +
                ", totalPrice=" + totalPrice  +
                ", orderStatus=" + orderStatus +
                ", user=" + user +
                ", province=" + province +
                ", modifyTime=" + modifyTime+
                ", message=" + message+
                '}';
    } 
}
