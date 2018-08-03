package com.mms.cloud.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mms.cloud.RestClientConfig;
import com.mms.cloud.dto.A5EventEntity;
import com.mms.cloud.dto.A5EventEntityTypeReference;
import com.mms.cloud.dto.ExceptionAggs;
import com.mms.cloud.dto.ExceptionEntity;
import com.mms.cloud.dto.ExceptionEntityTypeReference;
import com.mms.cloud.dto.PageableEntity;
import com.mms.cloud.dto.ResultData;
import com.mms.cloud.dto.TracknumEntity;
import com.mms.cloud.dto.TracknumEntityTypeReference;
import com.mms.cloud.search.SearchByTemplateRequest;
import com.mms.cloud.search.SearchService;
import com.mms.cloud.search.response.HitsAggsResponse;
import com.mms.cloud.search.response.HitsResponse;
import com.mms.cloud.search.response.aggregations.Aggregation;
import com.mms.cloud.search.response.aggregations.bucket.TermsAggregation;
import com.mms.cloud.search.response.aggregations.bucket.TermsBucket;

@RestController
@RequestMapping("/a5api")
@ContextConfiguration(classes = RestClientConfig.class)
public class A5MonitorController {
	
	private String index;
	
    @Autowired
    private SearchService searchService;
	
    /**
     * 查询a5格式化日志
     * @param exception
     * @param starttime
     * @param endtime
     * @return
     */
    @RequestMapping(value="/querya5event", method=RequestMethod.GET)
	public ResultData<PageableEntity> queryException(
			@RequestParam("query") String query,
			@RequestParam(value = "starttime", required = true) String starttime,
			@RequestParam(value = "endtime", required = true) String endtime,
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "size", required = true) String size) {
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("query_a5event.twig")
                .setAddId(false)
                .setTypeReference(new A5EventEntityTypeReference())
                .addModelParam("query", query.equals("")?"**":query)
                .addModelParam("starttime", starttime)
                .addModelParam("endtime", endtime);;
		HitsResponse<A5EventEntity> hitsResponse = searchService.queryByTemplate(request,size,from);
        List<A5EventEntity> entities = hitsResponse.getHits();
        PageableEntity pageableEntity = new PageableEntity();
        pageableEntity.setList_a5EventEntity(entities);
        pageableEntity.setTotal(hitsResponse.getTotalHits());
		return new ResultData<PageableEntity>(true, "success", 20000, pageableEntity);
	}
    
	/**
	 * 查询所有日志
	 * @param message
	 * @param starttime
	 * @param endtime
	 * @return
	 */
    @RequestMapping(value="/queryAllMessage", method=RequestMethod.GET)
	public ResultData<List<TracknumEntity>> queryAllMessage(
			@RequestParam("message") String message,
			@RequestParam(value = "starttime", required = true) String starttime,
			@RequestParam(value = "endtime", required = true) String endtime,
			@RequestParam(value = "size", required = false) String size) {
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("query_all_message.twig")
                .setAddId(false)
                .setTypeReference(new TracknumEntityTypeReference())
                .addModelParam("message", message)
                .addModelParam("starttime", starttime)
                .addModelParam("endtime", endtime);;
        HitsResponse<TracknumEntity> hitsResponse = null;
        if(size == null || size.equals("")) {
        	hitsResponse = searchService.queryByTemplate(request);
        }else {
        	hitsResponse = searchService.queryByTemplate(request,size);
        }
        List<TracknumEntity> entities = hitsResponse.getHits();
		return new ResultData<List<TracknumEntity>>(true, "success", new Long(hitsResponse.getTotalHits()).intValue(), entities);
	}
  
	@Value("${elastic.index.name}")
    public void setIndex(String index) {
        this.index = index;
    }

}
