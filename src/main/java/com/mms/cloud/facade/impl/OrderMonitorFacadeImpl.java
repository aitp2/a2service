package com.mms.cloud.facade.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mms.cloud.dto.CountryOrderMonitorDTO;
import com.mms.cloud.dto.OrderEntity;
import com.mms.cloud.dto.OrderStatusMonitorDTO;
import com.mms.cloud.dto.OrderStatusStatisticsDataDTO;
import com.mms.cloud.facade.OrderMonitorFacade;
import com.mms.cloud.service.OrderMonitorService;
import com.mms.cloud.utils.DateCompare;
import com.mms.cloud.utils.MonitorStatus;
import com.mms.cloud.utils.OrderStatus;
import com.mms.cloud.utils.ProvinceMap;

@Component
public class OrderMonitorFacadeImpl implements OrderMonitorFacade{
	
	@Autowired
	private OrderMonitorService orderMonitorService;
	
	private String rule_pay_yujing_hour;
	private String rule_send_yujing_hour;
	private String rule_send_jinggao_hour;
	private String rule_recevied_yujing_hour;
	private String rule_recevied_jinggao_hour;

	/**
	 * 获取订单操作日志
	 * 
	 */
	public List<OrderEntity> getOrderOperatorData(String orderCode) {
		return orderMonitorService.getOperatorDataByOrder(orderCode);
	}
	
	/**
	 * 获取全国订单监控数据
	 * @param province
	 * @return
	 */
	public Map<String,List<CountryOrderMonitorDTO>> getCountryOrderMonitorData(String province){
		this.calculateOrderStatusNum(province);
		Map<String,List<CountryOrderMonitorDTO>> map = new HashMap<String,List<CountryOrderMonitorDTO>>();
		List<CountryOrderMonitorDTO> list_yujing = new ArrayList<CountryOrderMonitorDTO>();
		for(String p:ProvinceMap.province_yujing.keySet()){
			CountryOrderMonitorDTO countryOrderMonitorDTO = new CountryOrderMonitorDTO();
			countryOrderMonitorDTO.setProvince(p);
			countryOrderMonitorDTO.setNum(ProvinceMap.province_yujing.get(p).toString());
			list_yujing.add(countryOrderMonitorDTO);
		}
		List<CountryOrderMonitorDTO> list_jinggao = new ArrayList<CountryOrderMonitorDTO>();
		for(String p:ProvinceMap.province_jinggao.keySet()){
			CountryOrderMonitorDTO countryOrderMonitorDTO = new CountryOrderMonitorDTO();
			countryOrderMonitorDTO.setProvince(p);
			countryOrderMonitorDTO.setNum(ProvinceMap.province_jinggao.get(p).toString());
			list_jinggao.add(countryOrderMonitorDTO);
		}
		map.put(MonitorStatus.YUJING, list_yujing);
		map.put(MonitorStatus.JINGGAO, list_jinggao);
		return  map;
	}
	
	/**
	 * 获取订单统计数据
	 * @param province
	 * @return
	 */
	public List<OrderStatusStatisticsDataDTO> getOrderStatusStatisticsDataDTO(String province){
		this.calculateOrderStatusNum(province);
		List<OrderStatusStatisticsDataDTO> list = new ArrayList<OrderStatusStatisticsDataDTO>();
		OrderStatusStatisticsDataDTO dto_nomarl = new OrderStatusStatisticsDataDTO();
		OrderStatusStatisticsDataDTO dto_yujing = new OrderStatusStatisticsDataDTO();
		OrderStatusStatisticsDataDTO dto_jinggao = new OrderStatusStatisticsDataDTO();
		DecimalFormat clonm_nf=new DecimalFormat("0");
		NumberFormat nf   =   NumberFormat.getPercentInstance();
		clonm_nf.setMaximumFractionDigits(0);
		if(province == null || province.equals("")){
			int all = ProvinceMap.china_namarl+ProvinceMap.china_yujing+ProvinceMap.china_jinggao;
			dto_nomarl.setStatus(MonitorStatus.NOMARL);
			dto_nomarl.setProvince("china");
			dto_nomarl.setNum(ProvinceMap.china_namarl.toString());
			dto_nomarl.setPercentage(nf.format((float)ProvinceMap.china_namarl/all));
			dto_nomarl.setClom(clonm_nf.format(24*(float)ProvinceMap.china_namarl/all));
			dto_yujing.setStatus(MonitorStatus.YUJING);
			dto_yujing.setProvince("china");
			dto_yujing.setNum(ProvinceMap.china_yujing.toString());
			dto_yujing.setPercentage(nf.format((float)ProvinceMap.china_yujing/all));
			dto_yujing.setClom(clonm_nf.format(24*(float)ProvinceMap.china_yujing/all));
			dto_jinggao.setStatus(MonitorStatus.JINGGAO);
			dto_jinggao.setProvince("china");
			dto_jinggao.setNum(ProvinceMap.china_jinggao.toString());
			dto_jinggao.setPercentage(nf.format((float)ProvinceMap.china_jinggao/all));
			dto_jinggao.setClom( new Integer(24 - new Integer(dto_nomarl.getClom())-new Integer(dto_yujing.getClom())).toString());
		}else{
			int all = ProvinceMap.province_nomarl.get(province)+
					ProvinceMap.province_yujing.get(province)+ProvinceMap.province_jinggao.get(province);
			if(all > 0 ){
				dto_nomarl.setStatus(MonitorStatus.NOMARL);
				dto_nomarl.setProvince(province);
				dto_nomarl.setNum(ProvinceMap.province_nomarl.get(province).toString());
				dto_nomarl.setPercentage(nf.format((float)ProvinceMap.province_nomarl.get(province)/all));
				dto_nomarl.setClom(clonm_nf.format(24*(float)ProvinceMap.province_nomarl.get(province)/all));
				dto_yujing.setStatus(MonitorStatus.YUJING);
				dto_yujing.setProvince(province);
				dto_yujing.setNum(ProvinceMap.province_yujing.get(province).toString());
				dto_yujing.setPercentage(nf.format((float)ProvinceMap.province_yujing.get(province)/all));
				dto_yujing.setClom(clonm_nf.format(24*(float)ProvinceMap.province_yujing.get(province)/all));
				dto_jinggao.setStatus(MonitorStatus.JINGGAO);
				dto_jinggao.setProvince(province);
				dto_jinggao.setNum(ProvinceMap.province_jinggao.get(province).toString());
				dto_jinggao.setPercentage(nf.format((float)ProvinceMap.province_jinggao.get(province)/all));
				dto_jinggao.setClom( new Integer(24 - new Integer(dto_nomarl.getClom())-new Integer(dto_yujing.getClom())).toString());
			}else{
				dto_nomarl.setStatus(MonitorStatus.NOMARL);
				dto_nomarl.setProvince(province);
				dto_nomarl.setNum(ProvinceMap.province_nomarl.get(province).toString());
				dto_nomarl.setPercentage("100%");
				dto_nomarl.setClom("24");
				dto_yujing.setStatus(MonitorStatus.YUJING);
				dto_yujing.setProvince(province);
				dto_yujing.setNum(ProvinceMap.province_yujing.get(province).toString());
				dto_yujing.setPercentage("0%");
				dto_yujing.setClom("0");
				dto_jinggao.setStatus(MonitorStatus.JINGGAO);
				dto_jinggao.setProvince(province);
				dto_jinggao.setNum(ProvinceMap.province_jinggao.get(province).toString());
				dto_jinggao.setPercentage("0%");
				dto_jinggao.setClom("0");
			}
			
		}
		list.add(dto_jinggao);
		list.add(dto_yujing);
		list.add(dto_nomarl);
		return list;
	}
	
	/**
	 * 获取订单监控列表
	 * @param province
	 * @return
	 */
	public List<OrderStatusMonitorDTO> getOrderStatusMonitorData(String province){
		List<OrderEntity> list_orderEntry =  orderMonitorService.getOperatorDataByProvince(province);
		Map<String,List<OrderEntity>> map_singleOrderStatusList =  new HashMap<String,List<OrderEntity>>();
		for(OrderEntity orderEntity:list_orderEntry){
			if(map_singleOrderStatusList.get(orderEntity.getOrderCode()) == null){
				List<OrderEntity> list_orderStatusList = new ArrayList<OrderEntity>();
				list_orderStatusList.add(orderEntity);
				map_singleOrderStatusList.put(orderEntity.getOrderCode(), list_orderStatusList);
			}else{
				map_singleOrderStatusList.get(orderEntity.getOrderCode()).add(orderEntity);
			}
		}
		List<OrderStatusMonitorDTO> list_orderStatusMonitorDTO = new ArrayList<OrderStatusMonitorDTO>();
		for(String orderCode:map_singleOrderStatusList.keySet()){
			OrderStatusMonitorDTO orderStatusMonitorDTO = new OrderStatusMonitorDTO();
			List<OrderEntity> list_orderStatusList = map_singleOrderStatusList.get(orderCode);
			String TotalPrice = "",User="";
			for(OrderEntity orderEntity: list_orderStatusList){
				if(orderEntity.getOrderStatus().equals(OrderStatus.CREATED)){
					TotalPrice = orderEntity.getTotalPrice();
					User = orderEntity.getUser();
				}
			}
			orderStatusMonitorDTO.setOrderCode(list_orderStatusList.get(0).getOrderCode());
			orderStatusMonitorDTO.setProvince(list_orderStatusList.get(0).getProvince());
			orderStatusMonitorDTO.setTotalPrice(TotalPrice);
			orderStatusMonitorDTO.setUser(User);
			//判断是否为取消
			String cancel_flag = "false";
			for(OrderEntity orderEntity:list_orderStatusList){
				if(orderEntity.getOrderStatus().equals(OrderStatus.CANCELLED)){
					cancel_flag = "true";
				}
			}
			orderStatusMonitorDTO.setCancelFlag(cancel_flag);
			if(cancel_flag.equals("false")){
				orderStatusMonitorDTO.setPayStatus(this.calculatePayStatusAccordingRule(list_orderStatusList));
				if(orderStatusMonitorDTO.getPayStatus().equals(MonitorStatus.NOMARL)){
					orderStatusMonitorDTO.setSendStatus(this.calculateSendStatusAccordingRule(list_orderStatusList));
					if(orderStatusMonitorDTO.getSendStatus().equals(MonitorStatus.NOMARL)){
						orderStatusMonitorDTO.setReceviedStatus(this.calculateReceviedAccordingRule(list_orderStatusList));
					}
				}
			}
			//设置时间
			this.setTimeForOrder(orderStatusMonitorDTO,list_orderStatusList);
			list_orderStatusMonitorDTO.add(orderStatusMonitorDTO);
		}
		return  list_orderStatusMonitorDTO;
	}
	
	/**
	 * 计算各状态数量，保存到内存
	 * 当查询全国数据时，缓存全部清除设置；当查询省份时，仅重新设置省份数据
	 * @param province
	 */
	private void  calculateOrderStatusNum(String province){
		ProvinceMap.reset(province);
		List<OrderStatusMonitorDTO> list_orderStatusMonitorDTO = this.getOrderStatusMonitorData(province);
		for(OrderStatusMonitorDTO orderStatusMonitorDTO:list_orderStatusMonitorDTO){
			if((orderStatusMonitorDTO.getPayStatus()!=null&&orderStatusMonitorDTO.getPayStatus().equals(MonitorStatus.YUJING ))
					||(orderStatusMonitorDTO.getSendStatus()!=null&&orderStatusMonitorDTO.getSendStatus().equals(MonitorStatus.YUJING))
					||(orderStatusMonitorDTO.getReceviedStatus()!=null&&orderStatusMonitorDTO.getReceviedStatus().equals(MonitorStatus.YUJING))){
				ProvinceMap.province_yujing.put(orderStatusMonitorDTO.getProvince(), 
						ProvinceMap.province_yujing.get(orderStatusMonitorDTO.getProvince())==null?0:ProvinceMap.province_yujing.get(orderStatusMonitorDTO.getProvince())+1);
				ProvinceMap.province_yujing_order.get(orderStatusMonitorDTO.getProvince()).add(orderStatusMonitorDTO);
				
			}else if((orderStatusMonitorDTO.getSendStatus()!=null&&orderStatusMonitorDTO.getSendStatus().equals(MonitorStatus.JINGGAO))
					||(orderStatusMonitorDTO.getReceviedStatus()!=null&&orderStatusMonitorDTO.getReceviedStatus().equals(MonitorStatus.JINGGAO))){
				ProvinceMap.province_jinggao.put(orderStatusMonitorDTO.getProvince(), 
						ProvinceMap.province_jinggao.get(orderStatusMonitorDTO.getProvince())==null?0:ProvinceMap.province_jinggao.get(orderStatusMonitorDTO.getProvince())+1);
				ProvinceMap.province_jinggao_order.get(orderStatusMonitorDTO.getProvince()).add(orderStatusMonitorDTO);
			}else{
				ProvinceMap.province_nomarl.put(orderStatusMonitorDTO.getProvince(), 
						ProvinceMap.province_nomarl.get(orderStatusMonitorDTO.getProvince())==null?0:ProvinceMap.province_nomarl.get(orderStatusMonitorDTO.getProvince())+1);
				ProvinceMap.province_nomarl_order.get(orderStatusMonitorDTO.getProvince()).add(orderStatusMonitorDTO);
			}
		}
		
		if(province == null || province.equals("")|| province.equals("china")){
			for(String provice:ProvinceMap.province_nomarl.keySet()){
				ProvinceMap.china_namarl = ProvinceMap.china_namarl +ProvinceMap.province_nomarl.get(provice);
				ProvinceMap.china_nomarl_order.addAll(ProvinceMap.province_nomarl_order.get(provice));
			}
			for(String provice:ProvinceMap.province_yujing.keySet()){
				ProvinceMap.china_yujing = ProvinceMap.china_yujing +ProvinceMap.province_yujing.get(provice);
				ProvinceMap.china_yujing_order.addAll(ProvinceMap.province_yujing_order.get(provice));
			}
			for(String provice:ProvinceMap.province_jinggao.keySet()){
				ProvinceMap.china_jinggao = ProvinceMap.china_jinggao +ProvinceMap.province_jinggao.get(provice);
				ProvinceMap.china_jinggao_order.addAll(ProvinceMap.province_jinggao_order.get(provice));
			}
		}
	}
	
	/**
	 * 设置状态时间
	 * @param orderStatusMonitorDTO
	 * @param list_orderStatusList
	 */
	private void setTimeForOrder(OrderStatusMonitorDTO orderStatusMonitorDTO,List<OrderEntity> list_orderStatusList){
		for(OrderEntity orderEntity:list_orderStatusList){
			if(orderEntity.getOrderStatus().equals(OrderStatus.CREATED)){
				orderStatusMonitorDTO.setCreateTime(orderEntity.getModifyTime().substring(0, 16));
			}
			if(orderEntity.getOrderStatus().equals(OrderStatus.PAYED)){
				orderStatusMonitorDTO.setPayTime(orderEntity.getModifyTime().substring(0, 16));
			}
			if(orderEntity.getOrderStatus().equals(OrderStatus.SENT)){
				orderStatusMonitorDTO.setSendTime(orderEntity.getModifyTime().substring(0, 16));
			}
			if(orderEntity.getOrderStatus().equals(OrderStatus.RECEIVED)){
				orderStatusMonitorDTO.setReceviedTime(orderEntity.getModifyTime().substring(0, 16));
			}
		}
		
	}
	
	/**
	 * 根据规则计算支付状态
	 * @param list_orderStatusList
	 * @return
	 */
	private String calculatePayStatusAccordingRule(List<OrderEntity> list_orderStatusList){
		String createdTime = null;
		String cancelTime = null;
		String payTime = null;
		boolean payfailFlag = false;
		for(OrderEntity orderEntity:list_orderStatusList){
			if(orderEntity.getOrderStatus().equals(OrderStatus.CREATED)){
				createdTime = orderEntity.getModifyTime();
			}
			if(orderEntity.getOrderStatus().equals(OrderStatus.CANCELLED)){
				cancelTime = orderEntity.getModifyTime();
			}
			if(orderEntity.getOrderStatus().equals(OrderStatus.PAYED)){
				payTime = orderEntity.getModifyTime();
			}
			if(orderEntity.getOrderStatus().equals(OrderStatus.PAYFAILED)){
				payfailFlag = true;
			}
		}
		if(payTime != null){
			return MonitorStatus.NOMARL;
		}else{
			//已创建24h内或已取消为正常
			if(cancelTime != null || DateCompare.compareRuleTimeAndNow(createdTime, rule_pay_yujing_hour) >= 0 
					){
				if(payfailFlag == true){
					return MonitorStatus.YUJING;
				}
				return "";
			}
			//超24h未支付为警告
			else return MonitorStatus.YUJING;
		}
	}
	
	/**
	 * 根据规则计算发货状态
	 * @param list_orderStatusList
	 * @return
	 */
	private String calculateSendStatusAccordingRule(List<OrderEntity> list_orderStatusList){
		String payTime = null;
		String sendTime = null;
		for(OrderEntity orderEntity:list_orderStatusList){
			if(orderEntity.getOrderStatus().equals(OrderStatus.PAYED)){
				payTime = orderEntity.getModifyTime();
			}
			if(orderEntity.getOrderStatus().equals(OrderStatus.SENT)){
				sendTime = orderEntity.getModifyTime();
			}
		}
		if(sendTime != null){
			return MonitorStatus.NOMARL;
		}else{
			//已支付24h内为正常
			if(DateCompare.compareRuleTimeAndNow(payTime, rule_send_yujing_hour) >= 0 ){
				return "";
			}
			//24h-30h未发货为预警
			else if(DateCompare.compareRuleTimeAndNow(payTime, rule_send_yujing_hour) < 0
					&& DateCompare.compareRuleTimeAndNow(payTime, rule_send_jinggao_hour) >= 0 ){
				return MonitorStatus.YUJING;
			}
			//超过30h未发货为警告
			else{
				return MonitorStatus.JINGGAO;
			}
		}
		
	}
	
	/**
	 * 根据规则计算签收状态
	 * @param list_orderStatusList
	 * @return
	 */
	private String calculateReceviedAccordingRule(List<OrderEntity> list_orderStatusList){
		String sendTime = null;
		String receviedTime = null;
		for(OrderEntity orderEntity:list_orderStatusList){
			if(orderEntity.getOrderStatus().equals(OrderStatus.SENT)){
				sendTime = orderEntity.getModifyTime();
			}
			if(orderEntity.getOrderStatus().equals(OrderStatus.RECEIVED)){
				receviedTime = orderEntity.getModifyTime();
			}
		}
		if(sendTime == null){
			return "";
		}
		if(receviedTime != null){
			return MonitorStatus.NOMARL;
		}else{
			////已发货48h内为正常
			if(DateCompare.compareRuleTimeAndNow(sendTime, rule_recevied_yujing_hour) >= 0 ){
				return "";
			}
			////已发货48h-72h为预警
			else if(DateCompare.compareRuleTimeAndNow(sendTime, rule_recevied_yujing_hour) < 0
					&& DateCompare.compareRuleTimeAndNow(sendTime, rule_recevied_jinggao_hour) >= 0 ){
				return MonitorStatus.YUJING;
			}
			//超过72h为警告
			else{
				return MonitorStatus.JINGGAO;
			}
		}
	}

	@Value("${rule.pay.yujing.hour}")
	public void setRule_pay_yujing_hour(String rule_pay_yujing_hour) {
		this.rule_pay_yujing_hour = rule_pay_yujing_hour;
	}

	@Value("${rule.send.yujing.hour}")
	public void setRule_send_yujing_hour(String rule_send_yujing_hour) {
		this.rule_send_yujing_hour = rule_send_yujing_hour;
	}

	@Value("${rule.send.jinggao.hour}")
	public void setRule_send_jinggao_hour(String rule_send_jinggao_hour) {
		this.rule_send_jinggao_hour = rule_send_jinggao_hour;
	}

	@Value("${rule.recevied.yujing.hour}")
	public void setRule_recevied_yujing_hour(String rule_recevied_yujing_hour) {
		this.rule_recevied_yujing_hour = rule_recevied_yujing_hour;
	}

	@Value("${rule.recevied.jinggao.hour}")
	public void setRule_recevied_jinggao_hour(String rule_recevied_jinggao_hour) {
		this.rule_recevied_jinggao_hour = rule_recevied_jinggao_hour;
	}

}
