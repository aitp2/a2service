package com.mms.cloud.dto;

import java.io.Serializable;

public class CountryOrderMonitorDTO implements Serializable{

	private static final long serialVersionUID = -7385146665378178002L;
	
	private String province;
	
	private String num;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

}
