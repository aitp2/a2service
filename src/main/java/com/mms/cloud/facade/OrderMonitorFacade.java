package com.mms.cloud.facade;

import java.util.List;
import java.util.Map;

import com.mms.cloud.dto.CountryOrderMonitorDTO;
import com.mms.cloud.dto.OrderEntity;
import com.mms.cloud.dto.OrderStatusMonitorDTO;
import com.mms.cloud.dto.OrderStatusStatisticsDataDTO;

public interface OrderMonitorFacade {
	
	/**
	 * 获取全国订单监控数据
	 * @param province
	 * @return
	 */
	public Map<String,List<CountryOrderMonitorDTO>> getCountryOrderMonitorData(String province);
	
	/**
	 * 获取订单统计数据
	 * @param province
	 * @return
	 */
	public List<OrderStatusStatisticsDataDTO> getOrderStatusStatisticsDataDTO(String province);
	
	/**
	 * 获取订单监控列表
	 * @param province
	 * @return
	 */
	public List<OrderStatusMonitorDTO> getOrderStatusMonitorData(String province);
	
	
	/**
	 * 获取订单操作日志
	 * 
	 */
	public List<OrderEntity> getOrderOperatorData(String orderCode);
	
}
