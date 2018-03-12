package com.mms.cloud.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateCompare {
	
	/**
	 * 大于等于0 未超时，小于0超时
	 * @param modifyTime
	 * @param rulehour
	 * @return
	 */
	public static int compareRuleTimeAndNow(String modifyTime,String rulehour){
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar c_ruletime=Calendar.getInstance();
		Calendar c_now=Calendar.getInstance();
		try {
			c_ruletime.setTime(simpleFormat.parse(modifyTime));
			//TODO 待还原，为demo，设定为分钟
			c_ruletime.add(Calendar.MINUTE, new Integer(rulehour));
			//c_ruletime.add(Calendar.HOUR_OF_DAY, new Integer(rulehour));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return c_ruletime.compareTo(c_now);
	}

}
