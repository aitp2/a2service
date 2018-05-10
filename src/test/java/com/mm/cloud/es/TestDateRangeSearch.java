package com.mm.cloud.es;

import java.util.List;

import com.mms.cloud.dto.OrderEntity;
import com.mms.cloud.dto.OrderEntityTypeReference;
import com.mms.cloud.search.SearchByTemplateRequest;
import com.mms.cloud.search.response.HitsResponse;

public class TestDateRangeSearch {
	public static void main(String[] args) {
		
OrderEntity e = new OrderEntity();
		
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName("monitorpoc18-*")
                .setTemplateName("find_operator_by_order.twig")
                .setAddId(false)
                .setTypeReference(new OrderEntityTypeReference())
                .addModelParam("orderCode", "00031017")
                .addModelParam("startTime", "2018-03-20 17:46:40")
                .addModelParam("endTime", "2018-03-30 17:48:12");

//        HitsResponse<OrderEntity> hitsResponse = searchService.queryByTemplate(request);
//        List<OrderEntity> entities = hitsResponse.getHits();
//        for(OrderEntity oe:entities){
//        	System.out.println(oe.toString());
//        }
	}
}
