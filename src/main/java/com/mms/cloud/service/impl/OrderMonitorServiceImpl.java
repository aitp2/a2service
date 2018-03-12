package com.mms.cloud.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mms.cloud.dto.OrderEntity;
import com.mms.cloud.dto.OrderEntityTypeReference;
import com.mms.cloud.search.SearchByTemplateRequest;
import com.mms.cloud.search.SearchService;
import com.mms.cloud.search.response.HitsResponse;
import com.mms.cloud.service.OrderMonitorService;

@Service
public class OrderMonitorServiceImpl implements OrderMonitorService {
	
	protected final Log logger = LogFactory.getLog(OrderMonitorServiceImpl.class);
	
	private String index;
	
	private String operator_by_province_template;
	
	private String operator_by_order_template;
	
    @Autowired
    private SearchService searchService;

	@Override
	public List<OrderEntity> getOperatorDataByProvince(String province) {
		if(province == null || province.equals("")){
			province = "订单";
		}
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName(operator_by_province_template)
                .setAddId(false)
                .setTypeReference(new OrderEntityTypeReference())
                .addModelParam("province", province);

        HitsResponse<OrderEntity> hitsResponse = searchService.queryByTemplate(request);
        List<OrderEntity> entities = hitsResponse.getHits();
        logger.info("getOrderData size:"+entities.size());
        for(OrderEntity oe:entities){
        	oe.setProvince(oe.getProvince().replace("来自", ""));
        	oe.setProvince(oe.getProvince().replace("订单", ""));
//        	logger.info(oe.toString());
        }
        return entities;
	}
	
	@Override
	public List<OrderEntity> getOperatorDataByOrder(String orderCode) {
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName(operator_by_order_template)
                .setAddId(false)
                .setTypeReference(new OrderEntityTypeReference())
                .addModelParam("orderCode", orderCode);

        HitsResponse<OrderEntity> hitsResponse = searchService.queryByTemplate(request);
        List<OrderEntity> entities = hitsResponse.getHits();
        logger.info("getOrderData size:"+entities.size());
        for(OrderEntity oe:entities){
        	oe.getProvince().replace("来自", "");
        	oe.getProvince().replace("订单", "");
//        	logger.debug(oe.toString());
        }
        return entities;
	}
	
	@Value("${elastic.index.name}")
    public void setIndex(String index) {
        this.index = index;
    }

	@Value("${elastic.operator.province.template}")
	public void setOperator_by_province_template(
			String operator_by_province_template) {
		this.operator_by_province_template = operator_by_province_template;
	}

	@Value("${elastic.operator.order.template}")
	public void setOperator_by_order_template(String operator_by_order_template) {
		this.operator_by_order_template = operator_by_order_template;
	}

}
