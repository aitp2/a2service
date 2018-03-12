package com.mms.cloud.service;

import java.util.List;

import com.mms.cloud.dto.OrderEntity;

public interface OrderMonitorService {
	
	/**
	 * elastic公用查询操作，依据province
	 * 查询当前时间为止的数据,province=null为全国数据
	 * @param province
	 * @return
	 */
	public List<OrderEntity> getOperatorDataByProvince(String province);
	
	/**
	 * elastic公用查询操作，依据orderCode
	 * @param orderCode
	 * @return
	 */
	public List<OrderEntity> getOperatorDataByOrder(String orderCode);

}
