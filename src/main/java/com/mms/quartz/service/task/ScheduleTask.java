package com.mms.quartz.service.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mms.cloud.dto.TracknumEntity;
import com.mms.cloud.dto.TracknumEntityTypeReference;
import com.mms.cloud.search.SearchByTemplateRequest;
import com.mms.cloud.search.SearchService;
import com.mms.cloud.search.response.HitsResponse;
import com.mms.quartz.model.AlertRule;
import com.mms.quartz.utils.SendEmailUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ScheduleTask: 定时任务的服务 TODO 此处执行es查询，得到结果判断是否满足触发动作阀值
 **/
@Service
public class ScheduleTask {
    private static final Logger LOGGER= LoggerFactory.getLogger(ScheduleTask.class);
    
    private String index;

    @Autowired
    private SearchService searchService;
    
    public void exeute(AlertRule config){
    	//转换时间范围为starttime与endtime
    	this.convertTimeRange(config);
    	SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("query_all_message.twig")
                .setAddId(false)
                .setTypeReference(new TracknumEntityTypeReference())
                .addModelParam("message", config.getTextCondition())
                .addModelParam("starttime", config.getStarttime())
                .addModelParam("endtime", config.getEndtime());;

        HitsResponse<TracknumEntity> hitsResponse = searchService.queryByTemplate(request,"0");
        long resultSize = hitsResponse.getTotalHits();
        long resultNumCompareNum = new Long(config.getResultNumCompareNum());
        if(config.getResultNumCompareFlag().equals(">")){
        	if(resultSize > resultNumCompareNum){
        		//发送邮件
        		try {
        			String body = config.getEmail_body().replace("$resultNum$", new Long(resultSize).toString());
					SendEmailUtils.sendTextEmail(config.getEmail_to(), config.getEmail_subject(), body);
				} catch (Exception e) {
					e.printStackTrace();
				}
        		 LocalDateTime time = LocalDateTime.now();
        	     LOGGER.info(time.getHour()+"时"+time.getMinute()+"分"+time.getSecond()+"秒,发送日志警告邮件成功to"+config.getEmail_to());
        	}
        }
    }
    
    private void convertTimeRange(AlertRule alertRule){
    	//TODO 此处暂设置时间范围最大
    	if(alertRule.equals("1h")){
    		
    	}else if(alertRule.equals("1h")){
    		
    	}else if(alertRule.equals("today")){
    		
    	}
    	alertRule.setStarttime("2008/05/11 01:10:49");
    	alertRule.setEndtime("2028/05/11 01:20:49");
    }
    
	@Value("${elastic.index.name}")
    public void setIndex(String index) {
        this.index = index;
    }
}
