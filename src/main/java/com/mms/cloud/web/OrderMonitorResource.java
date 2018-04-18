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

import com.mms.cloud.dto.CountryOrderMonitorDTO;
import com.mms.cloud.dto.OrderEntity;
import com.mms.cloud.dto.OrderEntityTypeReference;
import com.mms.cloud.dto.OrderStatusEntity;
import com.mms.cloud.dto.OrderStatusMonitorDTO;
import com.mms.cloud.dto.OrderStatusStatisticsDataDTO;
import com.mms.cloud.dto.ProductTotal;
import com.mms.cloud.dto.ProductTotalTypeReference;
import com.mms.cloud.dto.ResultData;
import com.mms.cloud.dto.TracknumEntity;
import com.mms.cloud.dto.TracknumEntityTypeReference;
import com.mms.cloud.facade.OrderMonitorFacade;
import com.mms.cloud.RestClientConfig;
import com.mms.cloud.search.SearchByTemplateRequest;
import com.mms.cloud.search.SearchService;
import com.mms.cloud.search.response.HitsResponse;
import com.mms.cloud.utils.CityLocation;
import com.mms.cloud.utils.MonitorStatus;
import com.mms.cloud.utils.ProvinceMap;

@RestController
@RequestMapping("/api")
@ContextConfiguration(classes = RestClientConfig.class)
public class OrderMonitorResource {
	
	@Autowired
	private OrderMonitorFacade orderMonitorFacade;
	
    private String index;

    @Autowired
    private SearchService searchService;

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
