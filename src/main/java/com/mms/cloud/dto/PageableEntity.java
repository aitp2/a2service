package com.mms.cloud.dto;

import java.util.List;

public class PageableEntity {
	
	private List<TracknumEntity> list;
	
	private List<SystemFlowTracknumDTO> list_systemFlowTracknum;
	
	private List<A5EventEntity> list_a5EventEntity;
	
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

	public List<SystemFlowTracknumDTO> getList_systemFlowTracknum() {
		return list_systemFlowTracknum;
	}

	public void setList_systemFlowTracknum(
			List<SystemFlowTracknumDTO> list_systemFlowTracknum) {
		this.list_systemFlowTracknum = list_systemFlowTracknum;
	}

	public List<A5EventEntity> getList_a5EventEntity() {
		return list_a5EventEntity;
	}

	public void setList_a5EventEntity(List<A5EventEntity> list_a5EventEntity) {
		this.list_a5EventEntity = list_a5EventEntity;
	}

}
