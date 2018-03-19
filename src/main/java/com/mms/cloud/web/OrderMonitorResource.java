package com.mms.cloud.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mms.cloud.dto.OrderEntity;
import com.mms.cloud.dto.OrderEntityTypeReference;
import com.mms.cloud.dto.ResultData;
import com.mms.cloud.dto.TracknumEntity;
import com.mms.cloud.dto.TracknumEntityTypeReference;
import com.mms.cloud.facade.OrderMonitorFacade;
import com.mms.cloud.RestClientConfig;
import com.mms.cloud.search.SearchByTemplateRequest;
import com.mms.cloud.search.SearchService;
import com.mms.cloud.search.response.HitsResponse;

@RestController
@RequestMapping("/api")
@ContextConfiguration(classes = RestClientConfig.class)
public class OrderMonitorResource {
	
	@Autowired
	private OrderMonitorFacade orderMonitorFacade;
	
//    private final static String INDEX = "orderpoc-*";
    private final static String TYPE = "log";
    
    private String index;

    @Autowired
    private SearchService searchService;

	@RequestMapping(value="/queryOrderList", method=RequestMethod.GET)
	public ResultData<List<OrderEntity>> queryMmsMonitorData(@RequestParam(value = "queryStartDate", required = true) String queryStartDate,
			@RequestParam(value = "queryEndDate", required = true) String queryEndDate) {
		OrderEntity e = new OrderEntity();
		
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("find_order.twig")
                .setAddId(false)
                .setTypeReference(new OrderEntityTypeReference())
                .addModelParam("queryStartDate", queryStartDate)
                .addModelParam("queryEndDate", queryEndDate);

        HitsResponse<OrderEntity> hitsResponse = searchService.queryByTemplate(request);
        List<OrderEntity> entities = hitsResponse.getHits();
        for(OrderEntity oe:entities){
        	System.out.println(oe.toString());
        }
        
        
		return new ResultData<List<OrderEntity>>(true, "success", 20000, entities);
	}
	
	@RequestMapping(value="/queryByTracknum", method=RequestMethod.GET)
	public ResultData<List<TracknumEntity>> queryMmsMonitorData(@RequestParam(value = "tracknum", required = true) String tracknum) {
		TracknumEntity e = new TracknumEntity();
		
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("fint_log_by_tracknum.twig")
                .setAddId(false)
                .setTypeReference(new TracknumEntityTypeReference())
                .addModelParam("tracknum", tracknum);

        HitsResponse<TracknumEntity> hitsResponse = searchService.queryByTemplate(request);
        List<TracknumEntity> entities = hitsResponse.getHits();
        for(TracknumEntity oe:entities){
        	System.out.println(oe.toString());
        }
        if(entities.size() == 0){
        	return new ResultData<List<TracknumEntity>>(true, "fail", -1, entities);
        }
        
		return new ResultData<List<TracknumEntity>>(true, "success", 20000, entities);
	}
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	public String getusers(){
		String json = "[{"+
	    "\"key\": \"1\",\"name\": \"John Brown\",\"age\": 32, \"address\": \"New York No. 1 Lake Park\"}, {"+
	    "\"key\": \"2\",\"name\": \"Jim Green\",\"age\": 42,\"address\": \"London No. 1 Lake Park\"}, {"+
	    "\"key\": \"3\",\"name\": \"Joe Black\",\"age\": 32,\"address\": \"Sidney No. 1 Lake Park\"}]";
		
		return json;
	}
	
	@RequestMapping(value="/currentUser", method=RequestMethod.GET)
	public String getcurrentUser(){
		System.out.println("{\"name\":\"test\",\"avatar\":\"https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png\",\"userid\":\"00000001\",\"notifyCount\":12}");
		return "{\"name\":\"test\",\"avatar\":\"https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png\",\"userid\":\"00000001\",\"notifyCount\":12}";
		
	}
	
	@Value("${elastic.index.name}")
    public void setIndex(String index) {
        this.index = index;
    }
}
