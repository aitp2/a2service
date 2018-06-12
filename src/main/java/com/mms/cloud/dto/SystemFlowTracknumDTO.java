package com.mms.cloud.dto;

import java.util.List;

public class SystemFlowTracknumDTO implements Comparable<SystemFlowTracknumDTO>{
	
	private String tracknum;
	
	private List<SystemFlowDTO> systemFlowList;

	public String getTracknum() {
		return tracknum;
	}

	public void setTracknum(String tracknum) {
		this.tracknum = tracknum;
	}

	public List<SystemFlowDTO> getSystemFlowList() {
		return systemFlowList;
	}

	public void setSystemFlowList(List<SystemFlowDTO> systemFlowList) {
		this.systemFlowList = systemFlowList;
	}
	
	public int compareTo(SystemFlowTracknumDTO arg0) {
        return arg0.getTracknum().compareTo(this.getTracknum());
    }


}
