package com.mms.quartz.model;

/**
 * @Description: : 返回对象
 **/
public class Result {
    /**
     *结果 true:false
     */
    private boolean result;
    /**
     * 信息
     */
    private String msg;
    
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}



}
