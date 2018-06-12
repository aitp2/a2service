package com.mms.cloud.dto;

public class SystemFlowDTO extends TracknumEntity implements Comparable<SystemFlowDTO>{
	
	private String tracknum;
	
	private String tracknumtype;
	
	private String nodeno;
	
	private String name;
	
	private String nomarl_pattern;
	
	private String exception_pattern;

	public String getTracknumtype() {
		return tracknumtype;
	}

	public void setTracknumtype(String tracknumtype) {
		this.tracknumtype = tracknumtype;
	}

	public String getTracknum() {
		return tracknum;
	}

	public void setTracknum(String tracknum) {
		this.tracknum = tracknum;
	}

	public String getNodeno() {
		return nodeno;
	}

	public void setNodeno(String nodeno) {
		this.nodeno = nodeno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNomarl_pattern() {
		return nomarl_pattern;
	}

	public void setNomarl_pattern(String nomarl_pattern) {
		this.nomarl_pattern = nomarl_pattern;
	}

	public String getException_pattern() {
		return exception_pattern;
	}

	public void setException_pattern(String exception_pattern) {
		this.exception_pattern = exception_pattern;
	}
	
	public int compareTo(SystemFlowDTO arg0) {
        return this.getNodeno().compareTo(arg0.getNodeno());
    }

}
