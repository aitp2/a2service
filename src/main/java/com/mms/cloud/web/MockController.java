package com.mms.cloud.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jtwig.functions.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MockController {
	
	private static final Logger LOG = LoggerFactory.getLogger(MockController.class);
	
	private String payMockStatus;
	
	private String sendMockStatus;
			
	@RequestMapping(value="/payMock")
	public void payMock(HttpServletRequest request, HttpServletResponse response, @Parameter String orderCode, @Parameter String province){
		String lastnum = orderCode.substring(orderCode.length()-1, orderCode.length());
		if(lastnum.equals("4") || lastnum.equals("5") || lastnum.equals("6")){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			LOG.info("orderStatus:PayedFail,orderCode:"+orderCode+",province:来自"+province+"订单,modifyTime:"+formatter.format(new Date())+
					"失败原因签名验证失败");
		}else{
			if(payMockStatus != null&&payMockStatus.equals("1")){
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				LOG.info("orderStatus:Payed,orderCode:"+orderCode+",province:来自"+province+"订单,modifyTime:"+formatter.format(new Date()));
			}else{
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				LOG.info("orderStatus:PayedFail,orderCode:"+orderCode+",province:来自"+province+"订单,modifyTime:"+formatter.format(new Date())+
						"失败原因签名验证失败");
			}
		}
		
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.print("{\"resultCode\": \""+payMockStatus+"\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	@RequestMapping(value="/sendMock")
	public void sendMock(HttpServletRequest request, HttpServletResponse response, @Parameter String orderCode, @Parameter String province){
		String lastnum = orderCode.substring(orderCode.length()-1, orderCode.length());
		if(lastnum.equals("7") || lastnum.equals("8") || lastnum.equals("9") || lastnum.equals("10")){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			LOG.info("orderStatus:SentFailed,orderCode:"+orderCode+",province:来自"+province+"订单,modifyTime:"+formatter.format(new Date())+
					"失败原因仓库库存不足");
		}else{
			if(sendMockStatus != null&&sendMockStatus.equals("1")){
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				LOG.info("orderStatus:Sent,orderCode:"+orderCode+",province:来自"+province+"订单,modifyTime:"+formatter.format(new Date()));
			}else{
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				LOG.info("orderStatus:SentFailed,orderCode:"+orderCode+",province:来自"+province+"订单,modifyTime:"+formatter.format(new Date())+
						"失败原因仓库库存不足");
			}
		}
		
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.print("{\"resultCode\": \""+sendMockStatus+"\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	@Value("${pay.mock.status}")
    public void setPayMockStatus(String payMockStatus) {
        this.payMockStatus = payMockStatus;
    }
	
	@Value("${send.mock.status}")
    public void setSendMockStatus(String sendMockStatus) {
        this.sendMockStatus = sendMockStatus;
    }
}
