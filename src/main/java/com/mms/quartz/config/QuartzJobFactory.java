package com.mms.quartz.config;

import com.mms.quartz.model.AlertRule;
import com.mms.quartz.utils.TaskUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Description: : 禁止并发执行
 **/
//当上一个任务未结束时下一个任务需进行等待
@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {

    public static final   String SCHEDULEJOBKEY="scheduleJob";
    //execute会根据cron的规则进行执行
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        AlertRule config = (AlertRule) jobExecutionContext.getMergedJobDataMap().get(SCHEDULEJOBKEY);
        TaskUtils.invokMethod(config);
    }
}
