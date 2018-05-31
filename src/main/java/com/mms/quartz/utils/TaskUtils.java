package com.mms.quartz.utils;

import com.mms.quartz.config.SpringUtils;
import com.mms.quartz.model.AlertRule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description: :
 * @date :Create in  2018/2/26- 15:55
 * @Version: V1.0
 * @Modified By:
 **/
public class TaskUtils {

    public static void invokMethod(AlertRule config){
        Object obj=null;
        Class clazz=null;
        //通过Spring上下文去找 也有可能找不到
        try {
            obj= SpringUtils.getBean(config.getClassPath().split("\\.")[config.getClassPath().split("\\.").length - 1]);
            if (obj == null){
                clazz = Class.forName(config.getClassPath());
                obj = clazz.newInstance();
            }else{
                clazz =obj.getClass();
            }
        }catch (Exception e){
            throw  new RuntimeException("ERROR:TaskUtils is Bean Create please check the classpath is`t right or not");
        }
        Method method=null;
        //获得方法名
        try {
        	Class[] cArg = new Class[1];
            cArg[0] = AlertRule.class;
            method = clazz.getDeclaredMethod(config.getMethodName(),cArg);
        } catch (NoSuchMethodException e) {
            throw  new RuntimeException("ERROR:TaskUtils is Bean the method Create please check the methodName is`t right or not");
        }
        //方法执行
        try {
            method.invoke(obj,config);
        } catch (Exception e) {
            throw  new RuntimeException("ERROR:TaskUtils is Bean the method execute please check the methodName is`t right or not");
        }

    }
}
