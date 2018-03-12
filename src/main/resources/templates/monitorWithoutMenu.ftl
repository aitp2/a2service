<html lang="en">
 <head> 
  <meta charset="UTF-8" /> 
  <meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
  <meta name="viewport" content="width=device-width, initial-scale=1" /> 
  <title>业务监控dashboard - Ant Design Pro</title> 
  <link rel="icon" href="/favicon.png" type="image/x-icon" /> 
	
  <link type="text/css" rel="stylesheet" href="style.css" />
  <script src="echarts.js"></script>
  <script src="jquery-1.6.2.js"></script>
  <script type="text/javascript">   
    
    // 路径配置
require.config({
   paths: {
       echarts: 'http://echarts.baidu.com/build/dist'
   }
 });
 require(
    [
       'echarts',
       'echarts/chart/map' // 使用柱状图就加载bar模块，按需加载
     ],   
         
 function (ec) {
	var myChart = ec.init(document.getElementById('main'));
	var ecConfig = require('echarts/config');
	var data_yujing =${yujing_json};
	var data_jinggao =${jinggao_json};
		
		var geoCoordMap = {
		    "北京": [116.46,39.92],
		        "天津": [117.20,39.13],
		        "河北": [114.48,38.03],
		        "山西": [112.53,37.87],
		        "内蒙古": [118.87,42.28],
		        "辽宁": [123.38,41.8],
		        "吉林": [125.35,43.88],
		        "黑龙江": [126.63,45.75],
		        "上海": [121.48,31.22],
		        "江苏": [118.78,32.05],
		        "浙江": [120.19,30.26],
		        "安徽": [117.27,31.86],
		        "福建": [118.10,24.46],
		        "江西": [115.89,28.68],
		        "山东": [117,36.65],
		        "广东": [113.23,23.16],
		        "广西": [110.28,25.29],
		        "海南": [110.35,20.02],
		        "河南": [113.65,34.76],
		        "湖北": [114.31,30.52],
		        "湖南": [113.00,28.21],
		        "重庆": [106.54,29.59],
		        "四川": [104.06,30.67],
		        "贵州": [106.71,26.57],
		        "云南": [102.73,25.04],
		        "西藏":[91.11,29.97],
		        "陕西": [109.47,36.60],
		        "甘肃": [103.73,36.03],
		        "青海": [101.74,36.56],
		        "宁夏": [106.27,38.47],
		        "新疆": [87.68,43.77],
		        "台湾":[121.97,24.08]
		};
		
		var convertData = function (data) {
		    var res = [];
		    for (var i = 0; i < data.length; i++) {
		        if(data[i].value > 0){
			        var geoCoord = geoCoordMap[data[i].name];
			        if (geoCoord) {
			            res.push({
			                name: data[i].name,
			                value: geoCoord.concat(data[i].value)
			            });
			        }
		        }
		        
		    }
		    return res;
		};
		
		var convertData_yujing = function (data) {
		    var res = [];
		    for (var i = 0; i < data.length; i++) {
		        if(data[i].value > 0){
			        var geoCoord = geoCoordMap[data[i].name];
			        if (geoCoord) {
			            res.push({
			                name: data[i].name,
			                value: geoCoord.concat(data[i].value).concat("(预警)")
			            });
			        }
		        }
		        
		    }
		    return res;
		};
		
		var convertData_jinggao = function (data) {
		    var res = [];
		    for (var i = 0; i < data.length; i++) {
		        if(data[i].value > 0){
			        var geoCoord = geoCoordMap[data[i].name];
			        if (geoCoord) {
			            res.push({
			                name: data[i].name,
			                value: geoCoord.concat(data[i].value).concat("(警告)")
			            });
			        }
		        }
		        
		    }
		    return res;
		};
		
		option = {
		    backgroundColor: '#F0F2F5',
		    title: {
		        text: '全国订单监控-${proviceName}',
		        subtext: getNowFormatDate(),
		        sublink: '',
		        x: 'center',
		        textStyle: {
		            color: '#000000'
		        }
		    },
		   tooltip: {
		        trigger: 'item',
		        formatter: function (params) {
		              if(typeof(params.value)[2] == "undefined"){
		              	if(isNaN(params.value) || params.value == 0){
		              		return params.name + ' : 订单正常';
		              	}
		              	return params.name + '(预警+警告) : ' + params.value;
		              }else{
		              	
		              	return params.name +params.value[3]+ ' : ' + params.value[2];
		              }
		            }
		    },
	 series : [
	       {
	        	name: 'onlymap',
	            type: 'map',
	            map: 'china',
	            geoIndex: 0,
	             selectedMode: 'single',
	            label: {
	                normal: {
	                    show: false
	                },
	                emphasis: {
	                    show: false,
	                    textStyle: {
	                        color: '#fff'
	                    }
	                }
	            },
	            roam: false,
	             itemStyle: {
	                normal: {
	                    borderWidth:1,
	                    borderColor:'#111',
	                    color: '#CCCCCC',
	                    label: {
	                        show: true,
	                        textStyle: {
	                            color: '#111'
	                        }
	                    }
	                },
	                emphasis: { 
	                    borderWidth:1,
	                    borderColor:'#fff',
	                    color: '#808080',
	                    label: {
	                        show: true,
	                        textStyle: {
	                            color: '#111'
	                        }
	                    }
	                }
	            },
	            animation: false,
	            data: data_yujing
	        },
	        {
	        	name: 'onlymap',
	            type: 'map',
	            map: 'china',
	            geoIndex: 0,
	             selectedMode: 'single',
	            label: {
	                normal: {
	                    show: false
	                },
	                emphasis: {
	                    show: false,
	                    textStyle: {
	                        color: '#fff'
	                    }
	                }
	            },
	            roam: false,
	             itemStyle: {
	                normal: {
	                    borderWidth:1,
	                    borderColor:'#111',
	                    color: '#CCCCCC',
	                    label: {
	                        show: true,
	                        textStyle: {
	                            color: '#111'
	                        }
	                    }
	                },
	                emphasis: {          
	                    borderWidth:1,
	                    borderColor:'#fff',
	                    color: '#808080',
	                    label: {
	                        show: true,
	                        textStyle: {
	                            color: '#111'
	                        }
	                    }
	                }
	            },
	            animation: false,
	            data: data_jinggao
	        },
	        {
	            name: '预警',
	            type: 'map',
	            mapType: 'china',
	            hoverable: true,
	            roam:false,
	            data : [],
	            markPoint : {
	              symbol:'pin',
	               symbolSize: function (val) {
	                return (val[2]+30)/5;
	               },  
	               label: {
	                normal: {
	                    show: false,
	                    textStyle: {
	                        color: '#111',
	                        fontSize: 9,
	                    }
	                }
	            },
	            itemStyle: {
	                normal: {
	                    color: '#FFD700',
	                    label: {
	                    textStyle: {
	                        color: '#111',
	                        fontSize: 0,//不显示数字
	                    }
	                    }
	                },
	                
	            },
	                data :convertData_yujing(data_yujing),
	            },
	            geoCoord: geoCoordMap
	        },
	        {
	            name: '警告_effort',
	            type: 'map',
	            mapType: 'china',
	            data:[],
	            markPoint : {
	                symbol:'Circle',
	                 symbolSize: function (val) {
	                     return (val[2]+50) / 5;
	            },
	                effect : {
	                    show: true,
	                   type:'scale',
	                    shadowBlur : 0
	                },
	                label: {
	                normal: {
	                    formatter: '{b}',
	                    position: 'right',
	                    show: true,
	                    textStyle: {
	                        color: '#111',
	                        fontSize: 9,
	                    }
	                }
	            },
	            itemStyle: {
	                normal: {
	                	show: false,
	                    color: '#FF4500',
	                    shadowBlur: 10,
	                    shadowColor: '#333'
	                }
	            },
	                data : convertData_jinggao(data_jinggao)
	            }
	        },
	        {
	            name: '警告',
	            type: 'map',
	            mapType: 'china',
	            data:[],
	            markPoint : {
	                symbol:'Circle',
	                symbolSize: function (val) {
	                     return (val[2]+30)/5 ;
	            },
	             label: {
	                normal: {
	                    show: false,
	                    textStyle: {
	                        color: '#111',
	                        fontSize: 9,
	                    }
	                }
	            },
	            itemStyle: {
	                normal: {
	                    color: '#FF4500',
	                    label: {
	                    textStyle: {
	                        color: '#111',
	                        fontSize: 0,//不显示数字
	                    }
	                    }
	                },
	                
	            },
	                data : convertData_jinggao(data_jinggao)
	            }
	        }
	    ]
	};

		myChart.on(ecConfig.EVENT.MAP_SELECTED,function (param) {
                var selected=param.selected;  
               var mapSeries=option.series[0];  
                var data= [];  
                var legendData= [];  
                var name;  
                var haveSelect = false;
                for (var p=0,len=mapSeries.data.length; p<len; p++) {  
                    name=mapSeries.data[p].name;  
                    mapSeries.data[p].selected=selected[name];  
                    if (selected[name]) {
                    	haveSelect = true;
  						window.location.href="monitorWithoutMenu?province="+name;
                    }
                }  
                if(haveSelect == false){
                	window.location.href="monitorWithoutMenu";
                }
            });         
            
		 myChart.setOption(option);  
		
		}

	)
		
		
	 function getNowFormatDate() {
			    var date = new Date();
			    var seperator1 = "-";
			    var seperator2 = ":";
			    var month = date.getMonth() + 1;
			    var strDate = date.getDate();
			    if (month >= 1 && month <= 9) {
			        month = "0" + month;
			    }
			    if (strDate >= 0 && strDate <= 9) {
			        strDate = "0" + strDate;
			    }
			    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
			            + " " + date.getHours() + seperator2 + date.getMinutes()
			            + seperator2 + date.getSeconds();
			    return currentdate;
			}
     
     function refleshOperate(code){
     	document.getElementById("operateLogDiv").style="display:";
     	jQuery("#logtitleDiv").empty();
     	jQuery("#logtitleDiv").append(code+"操作日志");
     	addOperateLog(code)
     }
     
     function refleshOperateByOrderStatus(code,status){
     	document.getElementById("operateLogDiv").style="display:";
     	jQuery("#logtitleDiv").empty();
     	jQuery("#logtitleDiv").append(code+" "+status+" 操作日志");
     	addOperateLogByOrderStatus(code,status)
     }
    
    function addOperateLog(code){
    	jQuery("#operateDataTbody").empty();
        jQuery.ajax({type:'POST',url:'loadOperateLog',dataType:"json",data:{"orderCode":code},
				success:function(json){ 
		            jQuery.each(json, function(index,item){
			        	jQuery("#operateDataTbody")
			        	.append("<tr class='ant-table-row2  ant-table-row-level-0'>"
			        	+"<td width='10%'>"+item.sourceSystem+"</td>"
			        	+"<td width='10%'>"+item.orderCode+"</td>"
			        	+"<td width='10%'>"+item.orderStatus+"</td>"
			        	+"<td width='10%'>"+item.modifyTime+"</td>"
			        	+"<td width='60%'>"+item.message+"</td></tr>");
		           });																												
	            }
			  });
    }
    
    function addOperateLogByOrderStatus(code,status){
    	jQuery("#operateDataTbody").empty();
        jQuery.ajax({type:'POST',url:'loadOperateLog',dataType:"json",data:{"orderCode":code},
				success:function(json){ 
		            jQuery.each(json, function(index,item){
		            	if(status == item.orderStatus){
		            	jQuery("#operateDataTbody")
			        	.append("<tr class='ant-table-row2  ant-table-row-level-0'>"
			        	+"<td width='10%'>"+item.sourceSystem+"</td>"
			        	+"<td width='10%'>"+item.orderCode+"</td>"
			        	+"<td width='10%'>"+item.orderStatus+"</td>"
			        	+"<td width='10%'>"+item.modifyTime+"</td>"
			        	+"<td width='60%'>"+item.message+"</td></tr>");
		            	}
			        	
		           });																												
	            }
			  });
    }
    
    function statusMonitor(province,status){
	    window.location.href = 'monitorWithoutMenu?province='+province+'&status='+status;
    }
    </script>
 
  <style id="erd_scroll_detection_scrollbar_style">/* Created by the element-resize-detector library. */
.erd_scroll_detection_container > div::-webkit-scrollbar { display: none; }
.erd_scroll_detection_container_animation_active { -webkit-animation-duration: 0.1s; animation-duration: 0.1s; -webkit-animation-name: erd_scroll_detection_container_animation; animation-name: erd_scroll_detection_container_animation; }
@-webkit-keyframes erd_scroll_detection_container_animation { 0% { opacity: 1; } 50% { opacity: 0; } 100% { opacity: 1; } }
@keyframes erd_scroll_detection_container_animation { 0% { opacity: 1; } 50% { opacity: 0; } 100% { opacity: 1; } }


</style>
 </head> 
 <body> 
  <div id="root" style="height:90%">
   <div class="screen-xl" style="position: relative;">
    <div class="ant-layout ant-layout-has-sider">
    <div class="ant-layout">
      <div class="ant-layout-content" style="margin: 24px 24px 0px; height: 100%;">
       <div style="min-height: calc(100vh - 260px);">
        <div style="margin: -24px -24px 0px;">
         <div class="pageHeader___29C_9">
          <div class="breadcrumb___2CJdW ant-breadcrumb">
          <div class="detail___ePshf">
           <div class="main___3S8-v">
            <div class="row___2MICT"></div>
            <div class="row___2MICT">
             <div class="content___DJF7M"  align="center">
              <div class="outer-iframe">
               <div class="d-iframe">
			   <!-- <div id="main" style="width: 800px;height:600px;"></div>-->
               
               </div>
              </div>
             </div>
            </div>
           </div>
          </div>
         </div>
         <div class="content___1elKt">
		  
		  <div class="ant-card-head">
            <div class="ant-card-head-wrapper">
             <div class="ant-card-head-title">
              
              <a href="javascript:void(0);" onclick="statusMonitor('${proviceName}','')" style="color:black">各状态订单比例</a>
			
             </div>
            </div>
           </div>
		  <section class="code-box-demo" style="margin-bottom: 24px;">
		   <div class="ant-row">
			<div class="ant-col-xs-${nomarlColm} ant-col-sm-${nomarlColm} ant-col-md-${nomarlColm} ant-col-lg-${nomarlColm} ant-col-xl-${nomarlColm}" style="background: #3CB371;height:25px" align="center" >
            	<a href="javascript:void(0);" onclick="statusMonitor('${proviceName}','NOMARL')" style="color:black">正常(${nomarlNum})${nomarlPercentage}</a>
			</div>
			<div class="ant-col-xs-${yujingColm} ant-col-sm-${yujingColm} ant-col-md-${yujingColm} ant-col-lg-${yujingColm} ant-col-xl-${yujingColm}" style="background:#FFD700;height:25px" align="center" >
				<a href="javascript:void(0);" onclick="statusMonitor('${proviceName}','YUJING')" style="color:black">预警(${yujingNum})${yujingPercentage}</a>
			</div>
			<div class="ant-col-xs-${jinggaoColm} ant-col-sm-${jinggaoColm} ant-col-md-${jinggaoColm} ant-col-lg-${jinggaoColm} ant-col-xl-${jinggaoColm}" style="background:#FF4500;height:25px" align="center" >
				<a href="javascript:void(0);" onclick="statusMonitor('${proviceName}','JINGGAO')" style="color:black">警告(${jinggaoNum})${jinggaoPercentage}</a>
			</div>
		   </div>
		  </section>
 
          <div class="ant-card ant-card-wider-padding ant-card-padding-transition" style="margin-bottom: 24px;">
           <div class="ant-card-head">
            <div class="ant-card-head-wrapper">
             <div class="ant-card-head-title">
              <#if (status == "NOMARL")> 正常  <#elseif (status == "YUJING")>预警  <#elseif (status == "JINGGAO")>警告   </#if>  订单监控
             </div>
            </div>
           </div>
           <div class="ant-card-body">
           
           <#list orderStatusList as orderStatusMonitorDTO>
            <div>
             
             <div class="ant-row">
				<div class="ant-col-xs-8 ant-col-sm-8 ant-col-md-8 ant-col-lg-8 ant-col-xl-8" style="background:#F5F5F5;height:25px" align="center" >
	            	单号：<a href="javascript:void(0);" onclick="refleshOperate('${orderStatusMonitorDTO.orderCode}');" style="color:black">${orderStatusMonitorDTO.orderCode}</a>
				</div>
				<div class="ant-col-xs-8 ant-col-sm-8 ant-col-md-8 ant-col-lg-8 ant-col-xl-8" style="background:#F5F5F5;height:25px" align="center" >
					用户：${orderStatusMonitorDTO.user}
				</div>
				<div class="ant-col-xs-8 ant-col-sm-8 ant-col-md-8 ant-col-lg-8 ant-col-xl-8" style="background:#F5F5F5;height:25px" align="center" >
					总价：${orderStatusMonitorDTO.totalPrice}
				</div>
			</div>
             
             
             <div class="ant-steps ant-steps-horizontal ant-steps-label-vertical ant-steps-dot">
              <div class="ant-steps-item <#if orderStatusMonitorDTO.cancelFlag == 'true'> ant-steps-item-wait</#if><#if orderStatusMonitorDTO.payStatus == '' || orderStatusMonitorDTO.payStatus == 'NOMARL'> ant-steps-item-finish</#if><#if orderStatusMonitorDTO.payStatus == 'YUJING'> ant-steps-item-orderyujing</#if> ">
               <div class="ant-steps-item-tail"></div>
               <div class="ant-steps-item-icon">
                <span class="ant-steps-icon"><span class="ant-steps-icon-dot"></span></span>
               </div>
               <div class="ant-steps-item-content">
                <div class="ant-steps-item-title">
                 <a href="javascript:void(0);" onclick="refleshOperateByOrderStatus('${orderStatusMonitorDTO.orderCode}','Created');" style="color:black">创建</a>
                 <#if orderStatusMonitorDTO.cancelFlag == 'true'>(已取消)</#if>
                </div>
                <div class="ant-steps-item-description">
                 <div class="textSecondary___3ifTy stepDescription___2JfeS">
                  <div>
                   ${orderStatusMonitorDTO.createTime}
                  </div>
                 </div>
                </div>
               </div>
              </div>
              
              <div class="ant-steps-item  <#if orderStatusMonitorDTO.sendStatus == 'NOMARL' > ant-steps-item-finish</#if>
              <#if orderStatusMonitorDTO.payStatus == 'NOMARL' && orderStatusMonitorDTO.sendStatus == ''> ant-steps-item-finish</#if>
              <#if orderStatusMonitorDTO.payStatus == ''|| orderStatusMonitorDTO.payStatus == 'YUJING'> ant-steps-item-wait</#if><#if orderStatusMonitorDTO.sendStatus == 'YUJING' > ant-steps-item-orderyujing</#if><#if orderStatusMonitorDTO.sendStatus == 'JINGGAO' > ant-steps-item-orderjinggao</#if>">
               <div class="ant-steps-item-tail"></div>
               <div class="ant-steps-item-icon">
                <span class="ant-steps-icon"><span class="ant-steps-icon-dot"></span></span>
               </div>
               <div class="ant-steps-item-content">
                <div class="ant-steps-item-title">
               <#if orderStatusMonitorDTO.payStatus == 'NOMARL' >
              	<a href="javascript:void(0);" onclick="refleshOperateByOrderStatus('${orderStatusMonitorDTO.orderCode}','Payed');" style="color:black">已支付</a>
              	</#if>
               <#if orderStatusMonitorDTO.payStatus == 'YUJING' >
              	待支付
              	</#if>
                </div>
                <div class="ant-steps-item-description">
                 <div class="textSecondary___3ifTy stepDescription___2JfeS">
                  <div>
                   ${orderStatusMonitorDTO.payTime}
                  </div>
                 </div>
                </div>
               </div>
              </div>
              
              <div class="ant-steps-item  <#if orderStatusMonitorDTO.receviedStatus == 'NOMARL' > ant-steps-item-finish</#if>
              <#if orderStatusMonitorDTO.sendStatus == 'NOMARL' && orderStatusMonitorDTO.receviedStatus == ''> ant-steps-item-finish</#if>
              <#if orderStatusMonitorDTO.sendStatus == ''|| orderStatusMonitorDTO.sendStatus == 'YUJING' || orderStatusMonitorDTO.sendStatus == 'JINGGAO'> ant-steps-item-wait</#if><#if orderStatusMonitorDTO.receviedStatus == 'YUJING' > ant-steps-item-orderyujing</#if><#if orderStatusMonitorDTO.receviedStatus == 'JINGGAO' > ant-steps-item-orderjinggao</#if>">
               <div class="ant-steps-item-tail"></div>
               <div class="ant-steps-item-icon">
                <span class="ant-steps-icon"><span class="ant-steps-icon-dot"></span></span>
               </div>
               <div class="ant-steps-item-content">
                <div class="ant-steps-item-title">
               <#if orderStatusMonitorDTO.payStatus == 'YUJING' >
              	
               </#if>
               <#if orderStatusMonitorDTO.sendStatus == 'NOMARL' >
              	<a href="javascript:void(0);" onclick="refleshOperateByOrderStatus('${orderStatusMonitorDTO.orderCode}','Sent');" style="color:black">已发货</a>
              </#if>
               <#if orderStatusMonitorDTO.sendStatus == 'YUJING' >
              	待发货
              </#if>
              <#if orderStatusMonitorDTO.sendStatus == 'JINGGAO' >
             	 待发货
              </#if>
                </div>
                <div class="ant-steps-item-description">
                 <div class="textSecondary___3ifTy stepDescription___2JfeS">
                  <div>
                   ${orderStatusMonitorDTO.sendTime}
                  </div>
                 </div>
                </div>
               </div>
              </div>
              
              <div class="ant-steps-item <#if orderStatusMonitorDTO.receviedStatus == 'NOMARL' > ant-steps-item-process<#else> ant-steps-item-wait</#if>">
               <div class="ant-steps-item-tail"></div>
               <div class="ant-steps-item-icon">
                <span class="ant-steps-icon"><span class="ant-steps-icon-dot"></span></span>
               </div>
               <div class="ant-steps-item-content">
                <div class="ant-steps-item-title">
                 
               <#if orderStatusMonitorDTO.payStatus == 'YUJING' >
              	
               </#if>
               <#if orderStatusMonitorDTO.receviedStatus == 'YUJING' >
              	待签收
              </#if>
              <#if orderStatusMonitorDTO.receviedStatus == 'JINGGAO' >
              	待签收
              </#if>
              <#if orderStatusMonitorDTO.receviedStatus == 'NOMARL' >
             	 <a href="javascript:void(0);" onclick="refleshOperateByOrderStatus('${orderStatusMonitorDTO.orderCode}','Recevied');" style="color:black">已签收</a>
              </#if>
                </div>
                <div class="ant-steps-item-description">
                 <div class="textSecondary___3ifTy stepDescription___2JfeS">
                  <div>
                   ${orderStatusMonitorDTO.receviedTime}
                  </div>
                 </div>
                </div>
               </div>
              </div>
             </div>
             
             
             </#list> 
             
             

            </div>
           </div>
          </div>
          
           
          <div id="operateLogDiv" style="display:none" class="ant-card tabsCard___1EbW2 ant-card-wider-padding ant-card-padding-transition">
          <div class="ant-card-head">
            <div class="ant-card-head-wrapper">
             <div id="logtitleDiv" class="ant-card-head-title">
              ordercode操作日志
             </div>
            </div>
           </div>
           <div class="ant-card-body">
            <div>
             <div class="ant-table-wrapper">
              <div class="ant-spin-nested-loading">
               <div class="ant-spin-container">
                <div class="ant-table ant-table-large ant-table-scroll-position-left">
                 <div class="ant-table-content">
                  <div class="ant-table-body">
                   <table id="operateDataTable" class="">
                    <colgroup>
                     <col />
                     <col />
                     <col />
                     <col />
                     <col />
                    </colgroup>
                    <thead class="ant-table-thead">
                     <tr>
                     <th class="" width="10%"><span>来源系统</span></th>
                      <th class="" width="10%"><span>订单编号</span></th>
                      <th class="" width="10%"><span>操作状态</span></th>
                      <th class="" width="10%"><span>操作时间</span></th>
                      <th class="" width="50%"><span>日志信息</span></th>
                     </tr>
                    </thead>
                    <tbody id="operateDataTbody" class="ant-table-tbody">
                     <tr class="ant-table-row  ant-table-row-level-0">
                      <td class="" width="10%"></td>
                      <td class="" width="10%"></td>
                      <td class="" width="10%"></td>
                      <td class="" width="10%"></td>
                      <td class="" width="60%"></td>
                     </tr>
                    
                    </tbody>
                   </table>
                  </div>
                 </div>
                </div>
               </div>
              </div>
             </div>
            </div>
           </div>
          </div>
          
          
         </div>
        </div>
       </div>
       <div class="globalFooter___1cM92">
        <div class="links___P5aS8">
         <a target="_blank" href="#"></a>
         <a target="_blank" href="#"></a>
         <a target="_blank" href="#"></a>
        </div>
        <div class="copyright___1ZP5c">
         <div>
          Copyright 
          <i class="anticon anticon-copyright"></i> 2018 AI Trans-Program A2
         </div>
        </div>
       </div>
      </div>
     </div>
    </div>
    <div class="erd_scroll_detection_container erd_scroll_detection_container_animation_active" style="visibility: hidden; display: inline; width: 0px; height: 0px; z-index: -1; overflow: hidden; margin: 0px; padding: 0px;">
     <div dir="ltr" class="erd_scroll_detection_container" style="position: absolute; flex: 0 0 auto; overflow: hidden; z-index: -1; visibility: hidden; width: 100%; height: 100%; left: 0px; top: 0px;">
      <div class="erd_scroll_detection_container" style="position: absolute; flex: 0 0 auto; overflow: hidden; z-index: -1; visibility: hidden; left: -18px; top: -18px; right: -17px; bottom: -17px;">
       <div style="position: absolute; flex: 0 0 auto; overflow: scroll; z-index: -1; visibility: hidden; width: 100%; height: 100%;">
        <div style="position: absolute; left: 0px; top: 0px; width: 1948px; height: 2235px;"></div>
       </div>
       <div style="position: absolute; flex: 0 0 auto; overflow: scroll; z-index: -1; visibility: hidden; width: 100%; height: 100%;">
        <div style="position: absolute; width: 200%; height: 200%;"></div>
       </div>
      </div>
     </div>
    </div>
   </div>
  </div> 
  <script src="https://gw.alipayobjects.com/as/g/??datavis/g2/2.3.12/index.js,datavis/g-cloud/1.0.2/index.js,datavis/g2-plugin-slider/1.2.1/slider.js"></script>
  <i title="Web Colour Picker" style="display: none;"></i> 
  <script type="text/javascript" src="/index.js"></script> 
  <object id="__symantecPKIClientMessenger" data-supports-flavor-configuration="true" data-extension-version="0.5.0.109" style="display: none;"></object>
  <span id="__symantecPKIClientDetector" style="display: none;">__PRESENT</span>
 </body>
</html>