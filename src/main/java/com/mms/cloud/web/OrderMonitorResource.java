package com.mms.cloud.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mms.cloud.dto.CountryOrderMonitorDTO;
import com.mms.cloud.dto.DFSOrderDTO;
import com.mms.cloud.dto.ExceptionAggs;
import com.mms.cloud.dto.ExceptionEntity;
import com.mms.cloud.dto.ExceptionEntityTypeReference;
import com.mms.cloud.dto.LibyOrderDTO;
import com.mms.cloud.dto.OrderEntity;
import com.mms.cloud.dto.OrderEntityTypeReference;
import com.mms.cloud.dto.OrderStatusEntity;
import com.mms.cloud.dto.OrderStatusMonitorDTO;
import com.mms.cloud.dto.OrderStatusStatisticsDataDTO;
import com.mms.cloud.dto.PageableEntity;
import com.mms.cloud.dto.ProductTotal;
import com.mms.cloud.dto.ProductTotalTypeReference;
import com.mms.cloud.dto.ResultData;
import com.mms.cloud.dto.SystemFlowDTO;
import com.mms.cloud.dto.SystemFlowTracknumDTO;
import com.mms.cloud.dto.TracknumEntity;
import com.mms.cloud.dto.TracknumEntityTypeReference;
import com.mms.cloud.facade.OrderMonitorFacade;
import com.mms.cloud.RestClientConfig;
import com.mms.cloud.search.SearchByTemplateRequest;
import com.mms.cloud.search.SearchService;
import com.mms.cloud.search.response.HitsAggsResponse;
import com.mms.cloud.search.response.HitsResponse;
import com.mms.cloud.search.response.aggregations.Aggregation;
import com.mms.cloud.search.response.aggregations.bucket.TermsAggregation;
import com.mms.cloud.search.response.aggregations.bucket.TermsBucket;
import com.mms.cloud.utils.CityLocation;
import com.mms.cloud.utils.MonitorStatus;
import com.mms.cloud.utils.ProvinceMap;
import com.mms.quartz.model.AlertRule;
import com.mms.quartz.service.QuartzTableService;
import com.mms.quartz.utils.HttpClientUtil;

@RestController
@RequestMapping("/api")
@ContextConfiguration(classes = RestClientConfig.class)
public class OrderMonitorResource {
	
	@Autowired
	private OrderMonitorFacade orderMonitorFacade;
	
    private String index;
    
    private String weburl;

    @Autowired
    private SearchService searchService;
    
    @Autowired
    private QuartzTableService quartzTableService;
    
    String TERM_EXCEPTION = "terms_exception";
    
    String ORDER_EXCEPTION = "terms_orderCode";
    
    /**
     * 异常聚合查询
     * @param starttime
     * @param endtime
     */
    @RequestMapping(value="/queryExceptionByAggs", method=RequestMethod.GET)
    public ResultData<List<ExceptionAggs>> queryExceptionByAggs(@RequestParam(value = "starttime", required = true) String starttime,
			@RequestParam(value = "endtime", required = true) String endtime){
    	starttime = starttime.replaceAll("-", "/");
		endtime = endtime.replaceAll("-", "/");
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("aggs_exception.twig")
                .setAddId(false)
                .setTypeReference(new ExceptionEntityTypeReference())
                .addModelParam("starttime", starttime)
                .addModelParam("endtime", endtime);

		HitsAggsResponse<ExceptionEntity> hitsAggsResponse = searchService.aggsByTemplate(request);
		Map<String, Aggregation> aggs = hitsAggsResponse.getAggregations();
		TermsAggregation termsAggregation = (TermsAggregation)aggs.get(TERM_EXCEPTION);
		List<ExceptionAggs> list = new ArrayList<ExceptionAggs>();
		for(TermsBucket termsBucket:termsAggregation.getBuckets()){
			ExceptionAggs e = new ExceptionAggs();
			e.setName(termsBucket.getKey());
			e.setCount(termsBucket.getDocCount().toString());
			list.add(e);
		}
		return new ResultData<List<ExceptionAggs>>(true, "success", 20000, list);
    }
    
    /**
     * 查询异常
     * @param exception
     * @param starttime
     * @param endtime
     * @return
     */
    @RequestMapping(value="/queryException", method=RequestMethod.GET)
	public ResultData<List<ExceptionEntity>> queryException(
			@RequestParam("exception") String exception,
			@RequestParam(value = "starttime", required = true) String starttime,
			@RequestParam(value = "endtime", required = true) String endtime) {
    	starttime = starttime.replaceAll("-", "/");
		endtime = endtime.replaceAll("-", "/");
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("query_exception.twig")
                .setAddId(false)
                .setTypeReference(new ExceptionEntityTypeReference())
                .addModelParam("exception", exception)
                .addModelParam("starttime", starttime)
                .addModelParam("endtime", endtime);;

        HitsResponse<ExceptionEntity> hitsResponse = searchService.queryByTemplate(request,"10");
        List<ExceptionEntity> entities = hitsResponse.getHits();
        if(entities.size() == 0){
        	return new ResultData<List<ExceptionEntity>>(true, "fail", -1, entities);
        }
        
		return new ResultData<List<ExceptionEntity>>(true, "success", 20000, entities);
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
			@RequestParam(value = "endtime", required = true) String endtime) {
    	starttime = starttime.replaceAll("-", "/");
		endtime = endtime.replaceAll("-", "/");
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("query_all_message.twig")
                .setAddId(false)
                .setTypeReference(new TracknumEntityTypeReference())
                .addModelParam("message", message)
                .addModelParam("starttime", starttime)
                .addModelParam("endtime", endtime);;

        HitsResponse<TracknumEntity> hitsResponse = searchService.queryByTemplate(request);
        List<TracknumEntity> entities = hitsResponse.getHits();
        if(entities.size() == 0){
        	return new ResultData<List<TracknumEntity>>(true, "fail", -1, entities);
        }
        
		return new ResultData<List<TracknumEntity>>(true, "success", 20000, entities);
	}
    
    /**
     * DFS产品日志查询
     * @param starttime
     * @param endtime
     */
    @RequestMapping(value="/queryDFSProductByTime", method=RequestMethod.GET)
    public ResultData<PageableEntity> queryDFSProductByTime(
    		@RequestParam(value = "productCode", required = false) String productCode,
    		@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "size", required = false) String size,
			@RequestParam(value = "pendingtypeCondition", required = false) String pendingtypeCondition){
    	starttime = starttime.replaceAll("-", "/");
		endtime = endtime.replaceAll("-", "/");
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("dfs_fint_productlog_by_tracknum.twig")
                .setAddId(false)
                .setTypeReference(new TracknumEntityTypeReference())
                .addModelParam("productCode", productCode)
                .addModelParam("starttime", starttime)
                .addModelParam("endtime", endtime)
                .addModelParam("pendingtypeCondition", pendingtypeCondition);

		HitsResponse<TracknumEntity> hitsResponse = searchService.queryByTemplate(request,size,from);
        List<TracknumEntity> entities = hitsResponse.getHits();
        
        List<TracknumEntity> list = new ArrayList<TracknumEntity>();
        for(TracknumEntity oe:entities){
        	if(oe.getMessage().contains("Exception")){
        		list.add(oe);
        	}
        }
        PageableEntity pageableEntity = new PageableEntity();
        pageableEntity.setList(list);
        pageableEntity.setTotal(hitsResponse.getTotalHits());
		return new ResultData<PageableEntity>(true, "success", 20000, pageableEntity);
    }
    
    /**
     * DFS订单查询
     * @param starttime
     * @param endtime
     */
    @RequestMapping(value="/queryDFSOperatorByTime", method=RequestMethod.GET)
    public ResultData<List<DFSOrderDTO>> queryDFSOperatorByTime(
    		@RequestParam(value = "orderCode", required = false) String orderCode,
    		@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime){
    	starttime = starttime.replaceAll("-", "/");
		endtime = endtime.replaceAll("-", "/");
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("dfs_find_operator_by_time.twig")
                .setAddId(false)
                .setTypeReference(new TracknumEntityTypeReference())
                .addModelParam("orderCode", orderCode)
                .addModelParam("starttime", starttime)
                .addModelParam("endtime", endtime);

		HitsResponse<TracknumEntity> hitsResponse = searchService.queryByTemplate(request);
        List<TracknumEntity> entities = hitsResponse.getHits();
        
        Map<String,DFSOrderDTO> map = new HashMap<String,DFSOrderDTO>();
        for(TracknumEntity oe:entities){
        	DFSOrderDTO dfsorderDTO = null;
        	if(map.get(oe.getOrderCode())==null){
        		dfsorderDTO = new DFSOrderDTO();
        	}else{
        		dfsorderDTO = map.get(oe.getOrderCode());
        	}
        	dfsorderDTO.setOrderCode(oe.getOrderCode());
        	//设置时间
        	if(oe.getMessage().contains("DFSPaymentCheckoutFacadeImpl.log(856)")){
        		dfsorderDTO.setCreateTime(oe.getLogtime());
        	}
        	if(oe.getMessage().contains("SubmitOrderEventListener.onSiteEvent(153)")){
        		dfsorderDTO.setPayTime(oe.getLogtime());
        	}
        	if(oe.getMessage().contains("DFSSendToMCSAction.executeAction(48)")){
        		dfsorderDTO.setSendMCSTime(oe.getLogtime());
        	}
        	if(oe.getMessage().contains("DFSStockServiceImpl.reserveForOrder(64)")){
        		if(oe.getMessage().contains("Exception")){
        			dfsorderDTO.setSendMCSStatus("Error");
        		}
        	}
        	if(oe.getMessage().contains("DFSConsignmentServiceImpl.updateStatus(158)")){
        		dfsorderDTO.setMCSCreateTime(oe.getLogtime());
        	}
        	if(oe.getMessage().contains("DFSConsignmentServiceImpl.updateStatus(127)")){
        		dfsorderDTO.setMCSPickTime(oe.getLogtime());
        	}
        	if(oe.getMessage().contains("DFSReceiveConsignmnetStatusAction.executeAction(45)")){
        		dfsorderDTO.setWaitPickTime(oe.getLogtime());
        	}
        	if(oe.getMessage().contains("DFSClearPreOrderAction.log(856)")){
        		dfsorderDTO.setPickupTime(oe.getLogtime());
        	}
        	map.put(oe.getOrderCode(), dfsorderDTO);
        }
        List<DFSOrderDTO> returnList = new ArrayList<DFSOrderDTO> ();
        for(String orderCode_:map.keySet()){
        	if(map.get(orderCode_).getCreateTime() != null && map.get(orderCode_).getPayTime() != null){
        		returnList.add(map.get(orderCode_));
        	}
        }
        Collections.sort(returnList);
		return new ResultData<List<DFSOrderDTO>>(true, "success", 20000, returnList);
    }

    
    /**
     * liby订单查询
     * @param starttime
     * @param endtime
     */
    @RequestMapping(value="/queryLibyOperatorByTime", method=RequestMethod.GET)
    public ResultData<List<LibyOrderDTO>> queryLibyOperatorByTime(
    		@RequestParam(value = "orderCode", required = false) String orderCode,
    		@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime){
    	starttime = starttime.replaceAll("-", "/");
		endtime = endtime.replaceAll("-", "/");
		
		Map<String,LibyOrderDTO> map = new HashMap<String,LibyOrderDTO>();
		SearchByTemplateRequest request_ordertotal = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("liby_find_total.twig")
                .setAddId(false)
                .setTypeReference(new TracknumEntityTypeReference())
                .addModelParam("starttime", starttime)
                .addModelParam("endtime", endtime);

		HitsAggsResponse<TracknumEntity> hitsAggsResponse = searchService.aggsByTemplate(request_ordertotal,"0");
		Map<String, Aggregation> aggs = hitsAggsResponse.getAggregations();
		TermsAggregation termsAggregation = (TermsAggregation)aggs.get(ORDER_EXCEPTION);
		if(orderCode != null && !orderCode.equals("") && !orderCode.equals("all")){
			SearchByTemplateRequest request = SearchByTemplateRequest.create()
	                .setIndexName(index)
	                .setTemplateName("dfs_find_operator_by_time.twig")
	                .setAddId(false)
	                .setTypeReference(new TracknumEntityTypeReference())
	                .addModelParam("orderCode", orderCode)
	                .addModelParam("starttime", starttime)
	                .addModelParam("endtime", endtime);

			HitsResponse<TracknumEntity> hitsResponse = searchService.queryByTemplate(request);
	        List<TracknumEntity> entities = hitsResponse.getHits();
	        
	        for(TracknumEntity oe:entities){
	        	LibyOrderDTO libyOrderDTO = null;
	        	if(map.get(oe.getOrderCode())==null){
	        		libyOrderDTO = new LibyOrderDTO();
	        	}else{
	        		libyOrderDTO = map.get(oe.getOrderCode());
	        	}
	        	libyOrderDTO.setOrderCode(oe.getOrderCode());
	        	//设置时间
	        	if(oe.getMessage().contains("split Order end")){
	        		libyOrderDTO.getList_splittime().add(oe.getLogtime());
	        	}
	        	if(oe.getMessage().contains("sender service type")){
	        		libyOrderDTO.getList_sendsaptime().add(oe.getLogtime());
	        	}
	        	if(oe.getMessage().contains("order-so-reply")){
	        		libyOrderDTO.getList_sapreturntime().add(oe.getLogtime());
	        	}
	        	if(oe.getMessage().contains("process Order") && oe.getMessage().contains("success")){
	        		libyOrderDTO.getList_dealtime().add(oe.getLogtime());
	        	}
	        	map.put(oe.getOrderCode(), libyOrderDTO);
	        }
		}else{
			int i =0,num=10;
			if(orderCode != null && orderCode.equals("all")){
				num = 100000;
			}
			for(TermsBucket termsBucket:termsAggregation.getBuckets()){
				if(i<num){
					SearchByTemplateRequest request = SearchByTemplateRequest.create()
			                .setIndexName(index)
			                .setTemplateName("dfs_find_operator_by_time.twig")
			                .setAddId(false)
			                .setTypeReference(new TracknumEntityTypeReference())
			                .addModelParam("orderCode", termsBucket.getKey())
			                .addModelParam("starttime", starttime)
			                .addModelParam("endtime", endtime);

					HitsResponse<TracknumEntity> hitsResponse = searchService.queryByTemplate(request);
			        List<TracknumEntity> entities = hitsResponse.getHits();
			        
			        for(TracknumEntity oe:entities){
			        	LibyOrderDTO libyOrderDTO = null;
			        	if(map.get(oe.getOrderCode())==null){
			        		libyOrderDTO = new LibyOrderDTO();
			        	}else{
			        		libyOrderDTO = map.get(oe.getOrderCode());
			        	}
			        	libyOrderDTO.setOrderCode(oe.getOrderCode());
			        	//设置时间
			        	if(oe.getMessage().contains("split Order end")){
			        		libyOrderDTO.getList_splittime().add(oe.getLogtime());
			        	}
			        	if(oe.getMessage().contains("sender service type")){
			        		libyOrderDTO.getList_sendsaptime().add(oe.getLogtime());
			        	}
			        	if(oe.getMessage().contains("order-so-reply")){
			        		libyOrderDTO.getList_sapreturntime().add(oe.getLogtime());
			        	}
			        	if(oe.getMessage().contains("process Order") && oe.getMessage().contains("success")){
			        		libyOrderDTO.getList_dealtime().add(oe.getLogtime());
			        	}
			        	map.put(oe.getOrderCode(), libyOrderDTO);
			        }
				}
				i = i +1;
			}
		}
		
		
        List<LibyOrderDTO> returnList = new ArrayList<LibyOrderDTO> ();
        for(String orderCode_:map.keySet()){
//        	if(map.get(orderCode_).getList_splittime() != null && map.get(orderCode_).getList_splittime().size() > 0){
        		returnList.add(map.get(orderCode_));
//        	}
        }
        Collections.sort(returnList);
		return new ResultData<List<LibyOrderDTO>>(true, "success", 20000, returnList);
    }
    
    /**
     * 根据时间条件查询订单
     * @param queryStartDate
     * @param queryEndDate
     * @return
     */
	@RequestMapping(value="/queryByTracknumAndTime", method=RequestMethod.GET)
	public ResultData<List<TracknumEntity>> queryByTracknumAndTime(@RequestParam(value = "tracknum", required = true) String tracknum,
			@RequestParam(value = "starttime", required = true) String starttime,
			@RequestParam(value = "endtime", required = true) String endtime) {
		TracknumEntity e = new TracknumEntity();
		starttime = starttime.replaceAll("-", "/");
		endtime = endtime.replaceAll("-", "/");
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("fint_log_by_tracknum.twig")
                .setAddId(false)
                .setTypeReference(new TracknumEntityTypeReference())
                .addModelParam("tracknum", tracknum)
                .addModelParam("starttime", starttime)
                .addModelParam("endtime", endtime);

        HitsResponse<TracknumEntity> hitsResponse = searchService.queryByTemplate(request);
        List<TracknumEntity> entities = hitsResponse.getHits();
        for(TracknumEntity oe:entities){
        	System.out.println(oe.toString());
        }
        
        
		return new ResultData<List<TracknumEntity>>(true, "success", 20000, entities);
	}
	
	/**
	 * 根据tracknum查询日志事件
	 * @param tracknum
	 * @return
	 */
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
	
	/**
	 * 查询警告数量数据
	 * @return
	 */
	@RequestMapping(value="/queryJinggaoData", method=RequestMethod.GET)
	public String queryJinggaoData() {
		Map<String,List<CountryOrderMonitorDTO>> map_data =orderMonitorFacade.getCountryOrderMonitorData(null);
		//警告数据
		StringBuffer jinggao_json = new StringBuffer();
		jinggao_json.append("[");
		int j=1;
		for(CountryOrderMonitorDTO countryOrderMonitorDTO:map_data.get(MonitorStatus.JINGGAO)){
			if(new Integer(countryOrderMonitorDTO.getNum()) > 0){
				jinggao_json.append("{cha: '").append(CityLocation.cityLocation.get(countryOrderMonitorDTO.getProvince()))
				.append("', name:'").append(countryOrderMonitorDTO.getProvince())
				.append("', jinggao:'").append(countryOrderMonitorDTO.getNum()).append("'");
				jinggao_json.append("},");
			j = j + 1;
			}
			
		}
		if(j > 1){
			jinggao_json = new StringBuffer(jinggao_json.substring(0, jinggao_json.length()-1));
		}
		jinggao_json.append("]");
        
        return jinggao_json.toString();
	}
	
	/**
	 * 查询警告数量数据
	 * @return
	 */
	@RequestMapping(value="/queryJinggaoShuliang", method=RequestMethod.GET)
	public String queryJinggaoShuliang() {
		Map<String,List<CountryOrderMonitorDTO>> map_data =orderMonitorFacade.getCountryOrderMonitorData(null);
		//警告数据
		StringBuffer jinggao_json = new StringBuffer();
		jinggao_json.append("[");
		int j=1;
		for(CountryOrderMonitorDTO countryOrderMonitorDTO:map_data.get(MonitorStatus.JINGGAO)){
			if(new Integer(countryOrderMonitorDTO.getNum()) > 0){
				jinggao_json.append(countryOrderMonitorDTO.getNum()+",");
			j = j + 1;
			}
			
		}
		if(j > 1){
			jinggao_json = new StringBuffer(jinggao_json.substring(0, jinggao_json.length()-1));
		}
		jinggao_json.append("]");
        
        return jinggao_json.toString();
	}
	
	/**
	 * 查询订单状态数据
	 * @return
	 */
	@RequestMapping(value="/queryOrderStatusData", method=RequestMethod.GET)
	public ResultData<List<OrderStatusEntity>> queryOrderStatusData() {
		List<OrderStatusEntity> list = new ArrayList<OrderStatusEntity> ();
		List<OrderStatusStatisticsDataDTO> list_statis = orderMonitorFacade.getOrderStatusStatisticsDataDTO(null);
		for(OrderStatusStatisticsDataDTO orderStatusStatisticsDataDTO:list_statis){
			OrderStatusEntity e = new OrderStatusEntity();
			if(orderStatusStatisticsDataDTO.getStatus().equals(MonitorStatus.NOMARL)){
				e.setStatus("正常订单");
				e.setNum(orderStatusStatisticsDataDTO.getNum());
				list.add(e);
			}
			if(orderStatusStatisticsDataDTO.getStatus().equals(MonitorStatus.YUJING)){
				e.setStatus("预警订单");
				e.setNum(orderStatusStatisticsDataDTO.getNum());
				list.add(e);
			}
			if(orderStatusStatisticsDataDTO.getStatus().equals(MonitorStatus.JINGGAO)){
				e.setStatus("警告订单");
				e.setNum(orderStatusStatisticsDataDTO.getNum());
				list.add(e);
			}
			
		}
        
        return new ResultData<List<OrderStatusEntity>>(true, "success", 20000, list);
	}
	
	/**
	 * 按省查询警告数据
	 * @param province
	 * @return
	 */
	@RequestMapping(value="/queryJinggaoDataByProvince", method=RequestMethod.GET)
	public ResultData<List<OrderStatusMonitorDTO>> queryJinggaoDataByProvince(@RequestParam(value = "province", required = true) String province) {
		Map<String,List<CountryOrderMonitorDTO>> map_data =orderMonitorFacade.getCountryOrderMonitorData(province);
		List<OrderStatusMonitorDTO> list = this.getOrderStatusMonitorDTOList(province, MonitorStatus.JINGGAO);
        return new ResultData<List<OrderStatusMonitorDTO>>(true, "success", 20000, list);
	}
	
	private List<OrderStatusMonitorDTO> getOrderStatusMonitorDTOList(String province,String status){
		List<OrderStatusMonitorDTO> list_monitorOrder = new ArrayList<OrderStatusMonitorDTO>();
		if(province == null || province.equals("")){
			//全国全状态
			if(status == null || status.equals("")){
				list_monitorOrder.addAll(ProvinceMap.china_nomarl_order);
				list_monitorOrder.addAll(ProvinceMap.china_yujing_order);
				list_monitorOrder.addAll(ProvinceMap.china_jinggao_order);
			}
			//全国某状态
			else{
				if(status.equals(MonitorStatus.NOMARL)){
					list_monitorOrder.addAll(ProvinceMap.china_nomarl_order);
				}
				if(status.equals(MonitorStatus.YUJING)){
					list_monitorOrder.addAll(ProvinceMap.china_yujing_order);
				}
				if(status.equals(MonitorStatus.JINGGAO)){
					list_monitorOrder.addAll(ProvinceMap.china_jinggao_order);
				}
			}
		}
		
		else{
			//某省全状态
			if(status == null || status.equals("")){
				list_monitorOrder.addAll(ProvinceMap.province_nomarl_order.get(province));
				list_monitorOrder.addAll(ProvinceMap.province_yujing_order.get(province));
				list_monitorOrder.addAll(ProvinceMap.province_jinggao_order.get(province));
			}
			//某省某状态
			else{
				if(status.equals(MonitorStatus.NOMARL)){
					list_monitorOrder.addAll(ProvinceMap.province_nomarl_order.get(province));
				}
				if(status.equals(MonitorStatus.YUJING)){
					list_monitorOrder.addAll(ProvinceMap.province_yujing_order.get(province));
				}
				if(status.equals(MonitorStatus.JINGGAO)){
					list_monitorOrder.addAll(ProvinceMap.province_jinggao_order.get(province));
				}
			}
		}
		return list_monitorOrder;
	}
	
	@RequestMapping(value="/queryAllOrder", method=RequestMethod.GET)
	public ResultData<List<OrderStatusMonitorDTO>> queryAllOrder() {
		Map<String,List<CountryOrderMonitorDTO>> map_data =orderMonitorFacade.getCountryOrderMonitorData(null);
		List<OrderStatusMonitorDTO> list = new ArrayList<OrderStatusMonitorDTO>();
		list.addAll(this.getOrderStatusMonitorDTOList(null, MonitorStatus.NOMARL));
		list.addAll(this.getOrderStatusMonitorDTOList(null, MonitorStatus.YUJING));
		list.addAll(this.getOrderStatusMonitorDTOList(null, MonitorStatus.JINGGAO));
        return new ResultData<List<OrderStatusMonitorDTO>>(true, "success", 20000, list);
	}
	
	@RequestMapping(value="/queryAllProduct", method=RequestMethod.GET)
	public ResultData<List<ProductTotal>> queryAllProduct() {
		SearchByTemplateRequest request = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("fint_producttotal_by_product.twig")
                .setAddId(false)
                .setTypeReference(new ProductTotalTypeReference());

        HitsResponse<ProductTotal> hitsResponse = searchService.queryByTemplate(request);
        List<ProductTotal> entities = hitsResponse.getHits();
        if(entities.size() == 0){
        	return new ResultData<List<ProductTotal>>(true, "fail", -1, entities);
        }
        
		return new ResultData<List<ProductTotal>>(true, "success", 20000, entities);
	}
	
	/**
	 * 更新警告cronjob
	 * @param id
	 * @param name
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/updateAlertRuleCronjob", method=RequestMethod.GET)
    public boolean updateAlertRuleCronjob(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "name", required = true) String name ,@RequestParam(value = "status", required = true) String status){
		AlertRule alertRule = new AlertRule();
		alertRule.setId(id);
		alertRule.setName(name);
		alertRule.setStatus(status);
		try {
			return quartzTableService.update(alertRule);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
    /**
     * 自定义系统流程查询
     * @param starttime
     * @param endtime
     */
    @RequestMapping(value="/querySystemFlowByTracknum", method=RequestMethod.GET)
    public ResultData<PageableEntity> querySystemFlowByTracknum(
    		@RequestParam(value = "tracknumtype", required = false) String tracknumtype,
    		@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "size", required = false) String size,
			@RequestParam(value = "tracknum", required = false) String tracknum){
    	starttime = starttime.replaceAll("-", "/");
		endtime = endtime.replaceAll("-", "/");
		
		List<SystemFlowDTO> list_flow_config = new ArrayList<SystemFlowDTO>();
		Map<String, String> para = new HashMap<String, String>();
		para.put("tracknumtype", tracknumtype);
		try {
			String result = HttpClientUtil.httpGet(
					this.weburl+"/api/getSystemFlow",para);
			ResultData<List<SystemFlowDTO>> parseObject = JSON.parseObject(result,
					new TypeReference<ResultData<List<SystemFlowDTO>>>() {
					});
			list_flow_config = parseObject.getSerializableData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long totalsize = 0;
		Map<String,List<SystemFlowDTO>> map_SystemFlow_value = new HashMap<String,List<SystemFlowDTO>>();
		for(SystemFlowDTO systemFlow_config:list_flow_config){
			if(systemFlow_config.getNodeno().equals("1")){
				//获取所有第一个节点,用于分页及后续数据范围查找
				SearchByTemplateRequest request_page_tracknum = SearchByTemplateRequest.create()
		                .setIndexName(index)
		                .setTemplateName("query_page_tracknum.twig")
		                .setAddId(false)
		                .setTypeReference(new TracknumEntityTypeReference())
		                .addModelParam("tracknumtype", tracknumtype)
		                .addModelParam("starttime", starttime)
		                .addModelParam("endtime", endtime)
		                .addModelParam("nomarl_pattern", systemFlow_config.getNomarl_pattern())
		                .addModelParam("tracknumValue", tracknum);
				
				HitsResponse<TracknumEntity> hitsResponse_page_tracknum = searchService.queryByTemplate(request_page_tracknum,size,from);
				List<TracknumEntity> entities_page_tracknum = hitsResponse_page_tracknum.getHits();
				totalsize = hitsResponse_page_tracknum.getTotalHits();
				for(TracknumEntity tracknumEntity:entities_page_tracknum){
					map_SystemFlow_value.put(tracknumEntity.getOrderCode(), new ArrayList<SystemFlowDTO>());
				}
			}
		}
		String pagetracknum = "";
		for(String key:map_SystemFlow_value.keySet()){
			if(pagetracknum.equals("")){
				pagetracknum = "\""+key+"\"";
			}else{
				pagetracknum = pagetracknum+ ",\"" + key +"\"";
			}
		}
		//查询上面第一节点tracknum范围内所有系统节点message数据
		SearchByTemplateRequest request_page_tracknum = SearchByTemplateRequest.create()
                .setIndexName(index)
                .setTemplateName("query_message_by_page_tracknum.twig")
                .setAddId(false)
                .setTypeReference(new TracknumEntityTypeReference())
                .addModelParam("tracknumtype", tracknumtype)
                .addModelParam("starttime", starttime)
                .addModelParam("endtime", endtime)
                .addModelParam("pagetracknum", pagetracknum);
		HitsResponse<TracknumEntity> hitsResponse_page_tracknum = searchService.queryByTemplate(request_page_tracknum);
		List<TracknumEntity> entities_page_tracknum = hitsResponse_page_tracknum.getHits();
		for(TracknumEntity tracknumEntity:entities_page_tracknum){
			SystemFlowDTO systemFlow_value = new SystemFlowDTO();
			systemFlow_value.setTracknum(tracknumEntity.getOrderCode());
			for(SystemFlowDTO systemFlow_config2:list_flow_config){
				if(tracknumEntity.getMessage().contains(systemFlow_config2.getNomarl_pattern())){
					systemFlow_value.setNodeno(systemFlow_config2.getNodeno());
					systemFlow_value.setName(systemFlow_config2.getName());
				}
			}
			systemFlow_value.setLogtime(tracknumEntity.getLogtime());
			boolean addflag = true;
			if(map_SystemFlow_value.get(tracknumEntity.getOrderCode()) != null
					&& map_SystemFlow_value.get(tracknumEntity.getOrderCode()).size() > 0){
				for(SystemFlowDTO systemFlow_haveadd:map_SystemFlow_value.get(tracknumEntity.getOrderCode())){
					System.out.println(systemFlow_haveadd.getTracknum()+systemFlow_haveadd.getName()+systemFlow_haveadd.getNodeno()+"  "+systemFlow_value.getNodeno());
					if(systemFlow_haveadd.getNodeno().equals(systemFlow_value.getNodeno())){
						addflag = false;
					}
				}
				
			}
			if(addflag){
				if(systemFlow_value.getNodeno() != null){
					map_SystemFlow_value.get(tracknumEntity.getOrderCode()).add(systemFlow_value);
				}
			}
		}
		//补足为null节点	
		for(SystemFlowDTO sf:list_flow_config){
			for(String key:map_SystemFlow_value.keySet()){
				boolean esitflag = false;
				List<SystemFlowDTO>  list_sy=map_SystemFlow_value.get(key);
				for(SystemFlowDTO sf_:list_sy){
					if(sf_.getNodeno().equals(sf.getNodeno())){
						esitflag = true;
					}
				}
				if(esitflag == false){
					SystemFlowDTO SystemFlow_null = new SystemFlowDTO();
					SystemFlow_null.setTracknum(key);
					SystemFlow_null.setNodeno(sf.getNodeno());
					map_SystemFlow_value.get(key).add(SystemFlow_null);
				}
			}
    	}
		
        List<SystemFlowTracknumDTO> list_return = new ArrayList<SystemFlowTracknumDTO>();
        for(String key:map_SystemFlow_value.keySet()){
        	SystemFlowTracknumDTO systemFlowTracknum = new SystemFlowTracknumDTO();
        	systemFlowTracknum.setTracknum(key);
        	Collections.sort(map_SystemFlow_value.get(key));
        	
        	systemFlowTracknum.setSystemFlowList(map_SystemFlow_value.get(key));
        	list_return.add(systemFlowTracknum);
        }
        
        
        
        Collections.sort(list_return);
        PageableEntity pageableEntity = new PageableEntity();
        pageableEntity.setList_systemFlowTracknum(list_return);
        pageableEntity.setTotal(totalsize);
		return new ResultData<PageableEntity>(true, "success", 20000, pageableEntity);
		
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
	
	@Value("${monitor.weburl}")
	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}
}
