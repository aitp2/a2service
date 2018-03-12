package com.mms.cloud.ruleengine;

public class SourceRuleTemplate {

	//
	private String name;
	
	//示例：LastOrderStatus==Created && （now - CreatedTime）> ?h
	private String condition;
	
	//示例:payStatus == ?
	private String action;
	
	//日志模板
	//示例：orderStatus:%{WORD:orderStatus},orderCode:%{WORD:orderCode},province:(?<province>来自.*(订单)),modifyTime:%{TIMESTAMP_ISO8601:modifyTime}
	private String logTemplate;
	
	//日志中唯一trackCode
	//示例：orderCode
	private String trackCode;

}
