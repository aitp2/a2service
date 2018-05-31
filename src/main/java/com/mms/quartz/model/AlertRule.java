package com.mms.quartz.model;


/**
 * @Description: :
 **/
public class AlertRule {
	
	//告警基础
    private String id;

    private String name;
    
    private String description;

    private String group = "logAlert";

    private String classPath = "com.mms.quartz.service.task.ScheduleTask";

    private String methodName = "exeute";

    private String status;

    //告警触发
    private String cron;
    
    private String textCondition;
    
    private String starttime;
    
    private String endtime;
    
    private String resultNumCompareFlag;
    
    private String resultNumCompareNum;
    
    //告警通知 - 邮件
    
    private String email_to;
    
    private String email_from = "mytemp163mail@126.com";
    
    private String email_subject;
    
    private String email_body;
    
    //告警抑制  在2h内,告警超过1次后,5h内不再发送告警通知       2-h:1;5-h
    private String restrain;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTextCondition() {
		return textCondition;
	}

	public void setTextCondition(String textCondition) {
		this.textCondition = textCondition;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getResultNumCompareFlag() {
		return resultNumCompareFlag;
	}

	public void setResultNumCompareFlag(String resultNumCompareFlag) {
		this.resultNumCompareFlag = resultNumCompareFlag;
	}

	public String getResultNumCompareNum() {
		return resultNumCompareNum;
	}

	public void setResultNumCompareNum(String resultNumCompareNum) {
		this.resultNumCompareNum = resultNumCompareNum;
	}

	public String getEmail_to() {
		return email_to;
	}

	public void setEmail_to(String email_to) {
		this.email_to = email_to;
	}

	public String getEmail_from() {
		return email_from;
	}

	public void setEmail_from(String email_from) {
		this.email_from = email_from;
	}

	public String getEmail_subject() {
		return email_subject;
	}

	public void setEmail_subject(String email_subject) {
		this.email_subject = email_subject;
	}

	public String getEmail_body() {
		return email_body;
	}

	public void setEmail_body(String email_body) {
		this.email_body = email_body;
	}

	public String getRestrain() {
		return restrain;
	}

	public void setRestrain(String restrain) {
		this.restrain = restrain;
	}
}
