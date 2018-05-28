package com.mms.cloud.dto;



public class DFSOrderDTO  implements Comparable<DFSOrderDTO>{
	
	private String orderCode;//
	
	private String createTime;//创建
	
	private String payTime;//已支付
	
	private String sendMCSTime;//已发送MCS
	
	private String MCSCreateTime;//MCS创建成功
	
	private String MCSPickTime;//MCS拣货完成
	
	private String WaitPickTime;//等待客户提货
	
	private String pickupTime;//客户提货完成
	
	private String sendMCSStatus;//发送MCS状态

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public String getSendMCSTime() {
		return sendMCSTime;
	}

	public void setSendMCSTime(String sendMCSTime) {
		this.sendMCSTime = sendMCSTime;
	}

	public String getMCSCreateTime() {
		return MCSCreateTime;
	}

	public void setMCSCreateTime(String mCSCreateTime) {
		MCSCreateTime = mCSCreateTime;
	}

	public String getMCSPickTime() {
		return MCSPickTime;
	}

	public void setMCSPickTime(String mCSPickTime) {
		MCSPickTime = mCSPickTime;
	}

	public String getWaitPickTime() {
		return WaitPickTime;
	}

	public void setWaitPickTime(String waitPickTime) {
		WaitPickTime = waitPickTime;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}
	
	public int compareTo(DFSOrderDTO arg0) {
        return arg0.getCreateTime().compareTo(this.getCreateTime());
    }

	public String getSendMCSStatus() {
		return sendMCSStatus;
	}

	public void setSendMCSStatus(String sendMCSStatus) {
		this.sendMCSStatus = sendMCSStatus;
	}

}
