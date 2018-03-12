package com.mms.cloud.dto;

public class OrderStatusStatisticsDataDTO {
	
	private String province;
	
	private String status;
	
	private String num;
	
	private String percentage;
	
	//统计显示grid比例参数
	private String clom;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getClom() {
		return clom;
	}

	public void setClom(String clom) {
		this.clom = clom;
	}
	
}
