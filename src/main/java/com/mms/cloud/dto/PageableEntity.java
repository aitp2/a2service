package com.mms.cloud.dto;

import java.util.List;

public class PageableEntity {
	
	private List<TracknumEntity> list;
	
	private long total;

	public List<TracknumEntity> getList() {
		return list;
	}

	public void setList(List<TracknumEntity> list) {
		this.list = list;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
