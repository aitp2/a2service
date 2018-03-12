package com.mms.cloud.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jtwig.functions.annotations.Parameter;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mms.cloud.dto.CountryOrderMonitorDTO;
import com.mms.cloud.dto.OrderEntity;
import com.mms.cloud.dto.OrderStatusMonitorDTO;
import com.mms.cloud.dto.OrderStatusStatisticsDataDTO;
import com.mms.cloud.facade.OrderMonitorFacade;
import com.mms.cloud.utils.MonitorStatus;
import com.mms.cloud.utils.ProvinceMap;

@Controller
public class CountryMonitorController {
	
	@Autowired
	private OrderMonitorFacade orderMonitorFacade;
	
	/**
	 * 全国地图监控
	 * @param map
	 * @return
	 */
	@RequestMapping("/countryMonitor")
    public String index(ModelMap map) {
		Map<String,List<CountryOrderMonitorDTO>> map_data = orderMonitorFacade.getCountryOrderMonitorData(null);
		StringBuffer yujing_json = new StringBuffer();
		yujing_json.append("[");
		int i=1;
		for(CountryOrderMonitorDTO countryOrderMonitorDTO:map_data.get(MonitorStatus.YUJING)){
			yujing_json.append("{name: '").append(countryOrderMonitorDTO.getProvince())
				.append("', value:").append(countryOrderMonitorDTO.getNum());
			if(i == map_data.get(MonitorStatus.YUJING).size()){
				yujing_json.append("}");
			}else{
				yujing_json.append("},");
			}
			i = i + 1;
		}
		yujing_json.append("]");
		map.addAttribute("yujing_json", yujing_json);
		
		StringBuffer jinggao_json = new StringBuffer();
		jinggao_json.append("[");
		int j=1;
		for(CountryOrderMonitorDTO countryOrderMonitorDTO:map_data.get(MonitorStatus.JINGGAO)){
			jinggao_json.append("{name: '").append(countryOrderMonitorDTO.getProvince())
				.append("', value:").append(countryOrderMonitorDTO.getNum());
			if(i == map_data.get(MonitorStatus.JINGGAO).size()){
				jinggao_json.append("}");
			}else{
				jinggao_json.append("},");
			}
			i = i + 1;
		}
		jinggao_json.append("]");
		map.addAttribute("jinggao_json", jinggao_json);
		
        return "countryMonitor";
    }
	
	/**
	 * allinone 监控
	 * @param map
	 * @param province
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/monitor")
    public String index(ModelMap map, @Parameter String province, @Parameter String status) {
		if(province != null&&  province.equals("china")){
			province = null;
		}
		Map<String,List<CountryOrderMonitorDTO>> map_data =orderMonitorFacade.getCountryOrderMonitorData(province);
		
		//预警数据
		StringBuffer yujing_json = new StringBuffer();
		yujing_json.append("[");
		int i=1;
		for(CountryOrderMonitorDTO countryOrderMonitorDTO:map_data.get(MonitorStatus.YUJING)){
			yujing_json.append("{name: '").append(countryOrderMonitorDTO.getProvince())
				.append("', value:").append(countryOrderMonitorDTO.getNum());
			//选中某省
			if(province != null && !province.equals("china")){
				if(province.equals(countryOrderMonitorDTO.getProvince())){
					yujing_json.append(",selected:true");
				}
			}
			if(i == map_data.get(MonitorStatus.YUJING).size()){
				yujing_json.append("}");
			}else{
				yujing_json.append("},");
			}
			i = i + 1;
		}
		yujing_json.append("]");
		map.addAttribute("yujing_json", yujing_json);
		
		//警告数据
		StringBuffer jinggao_json = new StringBuffer();
		jinggao_json.append("[");
		int j=1;
		for(CountryOrderMonitorDTO countryOrderMonitorDTO:map_data.get(MonitorStatus.JINGGAO)){
			jinggao_json.append("{name: '").append(countryOrderMonitorDTO.getProvince())
				.append("', value:").append(countryOrderMonitorDTO.getNum());
			if(j == map_data.get(MonitorStatus.JINGGAO).size()){
				jinggao_json.append("}");
			}else{
				jinggao_json.append("},");
			}
			j = j + 1;
		}
		jinggao_json.append("]");
		map.addAttribute("jinggao_json", jinggao_json);
		
		//状态比例数据
		List<OrderStatusStatisticsDataDTO> list_statis = orderMonitorFacade.getOrderStatusStatisticsDataDTO(province);
		for(OrderStatusStatisticsDataDTO orderStatusStatisticsDataDTO:list_statis){
			
			if(orderStatusStatisticsDataDTO.getStatus().equals(MonitorStatus.NOMARL)){
				map.addAttribute("nomarlNum", orderStatusStatisticsDataDTO.getNum());
				map.addAttribute("nomarlPercentage", orderStatusStatisticsDataDTO.getPercentage());
				map.addAttribute("nomarlColm", orderStatusStatisticsDataDTO.getClom());
			}
			if(orderStatusStatisticsDataDTO.getStatus().equals(MonitorStatus.YUJING)){
				map.addAttribute("yujingNum", orderStatusStatisticsDataDTO.getNum());
				map.addAttribute("yujingPercentage", orderStatusStatisticsDataDTO.getPercentage());
				map.addAttribute("yujingColm", orderStatusStatisticsDataDTO.getClom());
			}
			if(orderStatusStatisticsDataDTO.getStatus().equals(MonitorStatus.JINGGAO)){
				map.addAttribute("jinggaoNum", orderStatusStatisticsDataDTO.getNum());
				map.addAttribute("jinggaoPercentage", orderStatusStatisticsDataDTO.getPercentage());
				map.addAttribute("jinggaoColm", orderStatusStatisticsDataDTO.getClom());
			}
			
		}
		
		//订单状态数据getOrderStatusMonitorData  页面设置订单状态
		List<OrderStatusMonitorDTO> list_monitorOrder = this.getOrderStatusMonitorDTOList(province, status);
		
		if(province == null){
			map.addAttribute("proviceName", "china");
		}else{
			map.addAttribute("proviceName", province);
		}
		map.addAttribute("status", status==null?"":status);
		map.addAttribute("orderStatusList", list_monitorOrder);
        return "monitor";
        
    }
	
	/**
	 * allinone 订单监控-没有菜单
	 * @param map
	 * @param province
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/monitorWithoutMenu")
    public String monitorWithoutMenu(ModelMap map, @Parameter String province, @Parameter String status) {
		if(province != null&&  province.equals("china")){
			province = null;
		}
		Map<String,List<CountryOrderMonitorDTO>> map_data =orderMonitorFacade.getCountryOrderMonitorData(province);
		
		//预警数据
		StringBuffer yujing_json = new StringBuffer();
		yujing_json.append("[");
		int i=1;
		for(CountryOrderMonitorDTO countryOrderMonitorDTO:map_data.get(MonitorStatus.YUJING)){
			yujing_json.append("{name: '").append(countryOrderMonitorDTO.getProvince())
				.append("', value:").append(countryOrderMonitorDTO.getNum());
			//选中某省
			if(province != null && !province.equals("china")){
				if(province.equals(countryOrderMonitorDTO.getProvince())){
					yujing_json.append(",selected:true");
				}
			}
			if(i == map_data.get(MonitorStatus.YUJING).size()){
				yujing_json.append("}");
			}else{
				yujing_json.append("},");
			}
			i = i + 1;
		}
		yujing_json.append("]");
		map.addAttribute("yujing_json", yujing_json);
		
		//警告数据
		StringBuffer jinggao_json = new StringBuffer();
		jinggao_json.append("[");
		int j=1;
		for(CountryOrderMonitorDTO countryOrderMonitorDTO:map_data.get(MonitorStatus.JINGGAO)){
			jinggao_json.append("{name: '").append(countryOrderMonitorDTO.getProvince())
				.append("', value:").append(countryOrderMonitorDTO.getNum());
			if(j == map_data.get(MonitorStatus.JINGGAO).size()){
				jinggao_json.append("}");
			}else{
				jinggao_json.append("},");
			}
			j = j + 1;
		}
		jinggao_json.append("]");
		map.addAttribute("jinggao_json", jinggao_json);
		
		//状态比例数据
		List<OrderStatusStatisticsDataDTO> list_statis = orderMonitorFacade.getOrderStatusStatisticsDataDTO(province);
		for(OrderStatusStatisticsDataDTO orderStatusStatisticsDataDTO:list_statis){
			
			if(orderStatusStatisticsDataDTO.getStatus().equals(MonitorStatus.NOMARL)){
				map.addAttribute("nomarlNum", orderStatusStatisticsDataDTO.getNum());
				map.addAttribute("nomarlPercentage", orderStatusStatisticsDataDTO.getPercentage());
				map.addAttribute("nomarlColm", orderStatusStatisticsDataDTO.getClom());
			}
			if(orderStatusStatisticsDataDTO.getStatus().equals(MonitorStatus.YUJING)){
				map.addAttribute("yujingNum", orderStatusStatisticsDataDTO.getNum());
				map.addAttribute("yujingPercentage", orderStatusStatisticsDataDTO.getPercentage());
				map.addAttribute("yujingColm", orderStatusStatisticsDataDTO.getClom());
			}
			if(orderStatusStatisticsDataDTO.getStatus().equals(MonitorStatus.JINGGAO)){
				map.addAttribute("jinggaoNum", orderStatusStatisticsDataDTO.getNum());
				map.addAttribute("jinggaoPercentage", orderStatusStatisticsDataDTO.getPercentage());
				map.addAttribute("jinggaoColm", orderStatusStatisticsDataDTO.getClom());
			}
			
		}
		
		//订单状态数据getOrderStatusMonitorData  页面设置订单状态
		List<OrderStatusMonitorDTO> list_monitorOrder = this.getOrderStatusMonitorDTOList(province, status);
		
		if(province == null){
			map.addAttribute("proviceName", "china");
		}else{
			map.addAttribute("proviceName", province);
		}
		map.addAttribute("status", status==null?"":status);
		map.addAttribute("orderStatusList", list_monitorOrder);
        return "monitorWithoutMenu";
        
    }
	
	@RequestMapping(value="/loadOperateLog")
	public void loadOperateLog(HttpServletRequest request, HttpServletResponse response, @Parameter String orderCode){
		List<OrderEntity> list = orderMonitorFacade.getOrderOperatorData(orderCode);
		
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.print(Json.toJson(list));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//For ant design work log test
	@RequestMapping(value="/loadOperateLogant/api/profile/advanced")
	public void loadOperateLogant(HttpServletRequest request, HttpServletResponse response){
		List<OrderEntity> list = orderMonitorFacade.getOrderOperatorData("order000004");
		response.setContentType("text/plain;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			String json = "{\"advancedOperation1\":"+Json.toJson(list)+"}";
			System.out.print(json);
			writer.print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="/loadOrderStatus")
	public void loadOrderStatus(HttpServletRequest request, HttpServletResponse response, @Parameter String province, @Parameter String status){
		Map<String,List<CountryOrderMonitorDTO>> map_data =orderMonitorFacade.getCountryOrderMonitorData(province);
		List<OrderStatusMonitorDTO> list = this.getOrderStatusMonitorDTOList(province, status);
		
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.print(Json.toJson(list));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/loadOrderStatusStatistics")
	private void loadOrderStatusStatistics(HttpServletRequest request, HttpServletResponse response, @Parameter String province){
		List<OrderStatusStatisticsDataDTO> list_statis = orderMonitorFacade.getOrderStatusStatisticsDataDTO(province);
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.print(Json.toJson(list_statis));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
}
