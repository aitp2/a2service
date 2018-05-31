package com.mms.quartz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mms.cloud.dto.ResultData;
import com.mms.quartz.config.QuartzJobFactory;
import com.mms.quartz.enums.ConfigEnum;
import com.mms.quartz.model.AlertRule;
import com.mms.quartz.utils.HttpClientUtil;

@Service
public class QuartzTableService {
	@Autowired
	SchedulerFactoryBean schedulerFactoryBean;
	
	private String weburl;

	public boolean update(AlertRule config) throws Exception {
		if (config == null) {
			new RuntimeException("未找到此告警规则");
		}
		if ("1".equals(config.getStatus())) {
			// stop 禁用
			config.setStatus(ConfigEnum.STATUS_STOP.getCode());
			deleteJob(config);
			return true;
		} else {
			// start 启用
			config.setStatus(ConfigEnum.STATUS_START.getCode());
			//根据id获取完整AlertRule
			Map<String, String> para = new HashMap<String, String>();
			para.put("id", config.getId());
			String result = HttpClientUtil.httpGet(
					this.weburl+"/alertapi/getAlertRule",
					para);
			ResultData<AlertRule> parseObject = JSON.parseObject(result,
					new TypeReference<ResultData<AlertRule>>() {
					});
			config = parseObject.getSerializableData();
			addJob(config);
			return true;
		}

	}

	/**
	 *
	 * 微服务启动时启动所有的任务
	 * 
	 */
	public void startJobs() {
		try {
			String result = HttpClientUtil.httpGet(
					this.weburl+"/alertapi/getAllAlertRules",
					null);
			ResultData<List<AlertRule>> parseObject = JSON.parseObject(result,
					new TypeReference<ResultData<List<AlertRule>>>() {
					});

			// temp test
			// AlertRule config_temp = new AlertRule();
			// config_temp.setName("productPendingException");
			// config_temp.setCron("0 */1 * * * ?");
			// config_temp.setStatus("0");
			// config_temp.setTextCondition("productCode:** AND message:\\\"com.dfs.jms.exception.DFSProductFeedPendingException\\\"");
			// config_temp.setStarttime("2008/05/11 01:10:49");
			// config_temp.setEndtime("2028/05/11 01:20:49");
			// config_temp.setResultNumCompareFlag(">");
			// config_temp.setResultNumCompareNum("10");
			// config_temp.setEmail_to("mytemp163mail@126.com");
			// config_temp.setEmail_subject("productPendingException产品异常");
			// config_temp.setEmail_body("你好,"+config_temp.getStarttime()+"至"+config_temp.getEndtime()+"产生productPendingException异常数量为$resultNum$,超出告警阀值"
			// +config_temp.getResultNumCompareNum()+",请到业务日志监控平台核查处理数据");
			// configList.add(config_temp);
			if (parseObject.getCode() > 0) {
				for (AlertRule config : parseObject.getSerializableData()) {
					if (ConfigEnum.STATUS_START.getCode().equals(
							config.getStatus())) {
						addJob(config);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /**
	// * 修改cron表达式
	// * @param id
	// * @param cronSchedule
	// */
	// public void updateCron(Long id, String cronSchedule) {
	// int i= configRepository.setScheduleById(cronSchedule,id);
	// if (i<=0){
	// throw new RuntimeException("500!");
	// }
	// }

	/**
	 * 增加任务
	 * 
	 * @param :com.mms.quartz.model.config
	 * @Date: 2018/2/26 9:57
	 * @return: void
	 */
	private void addJob(AlertRule config) throws SchedulerException {
		// 得到调度器
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = this.getJobKey(config);
		// 获得触发器
		TriggerKey triggerKey = TriggerKey.triggerKey(config.getName(),
				config.getGroup());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 判断触发器是否存在（如果存在说明之前运行过但是在当前被禁用了，如果不存在说明一次都没运行过）
		if (trigger == null) {
			// 新建一个工作任务 指定任务类型为串接进行的
			JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
					.withIdentity(jobKey).build();
			// 将工作添加到工作任务当中去
			jobDetail.getJobDataMap().put(QuartzJobFactory.SCHEDULEJOBKEY,
					config);
			// 将cron表达式进行转换
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
					.cronSchedule(config.getCron());
			// 创建触发器并将cron表达式对象给塞入
			trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
					.withSchedule(cronScheduleBuilder).build();
			// 在调度器中将触发器和任务进行组合
			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
					.cronSchedule(config.getCron());
			// 按照新的规则进行
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
					.withSchedule(cronScheduleBuilder).build();
			// 重启
			scheduler.rescheduleJob(triggerKey, trigger);
		}
	}

	/**
	 * 删除任务
	 *
	 * @param : com.mms.quartz.model.config
	 * @Date: 2018/2/24 18:23
	 * @return: void
	 */
	private void deleteJob(AlertRule config) throws SchedulerException {
		// 得到调度器
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		// 找到key值
		JobKey jobKey = this.getJobKey(config);
		if(jobKey != null){
			// 从触发器找到此任务然后进行删除
			scheduler.deleteJob(jobKey);
		}
	}

	/**
	 * 根据name和group得到任务的key
	 *
	 * @param :com.mms.quartz.model.config
	 * @Date: 2018/2/24 18:27
	 * @return: org.quartz.JobKey
	 */
	private JobKey getJobKey(AlertRule config) {
		return getJobKey(config.getName(), config.getId());
	}

	private JobKey getJobKey(String name, String id) {
		return JobKey.jobKey(name, id);
	}

	@Value("${monitor.weburl}")
	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}

}
