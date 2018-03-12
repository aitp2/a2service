<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>countryMonitor</title>
    <!-- 引入 echarts.js -->
    <script src="echarts.js"></script>
</head>
<body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 1500px;height:900px;"></div>
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
		        text: '全国订单监控',
		        subtext: getNowFormatDate(),
		        sublink: 'http://localhost:8088/countryMonitor',
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
	                return (val[2]+30) / 10;
	               },  
	               label: {
	                normal: {
	                    show: true,
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
	                        fontSize: 9,
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
	                     return (val[2]+50) / 10;
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
	                    show: true
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
	                     return (val[2]+30) / 10;
	            },
	                effect : {
	                    show: false,
	                   type:'scale',
	                    shadowBlur : 0
	                },
	                label: {
	                normal: {
	                    formatter: '{b}',
	                    position: 'right',
	                    show: true
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
	        }
	    ]
	};

		myChart.on(ecConfig.EVENT.MAP_SELECTED,function (param) {  
                var selected=param.selected;  
               var mapSeries=option.series[0];  
                var data= [];  
                var legendData= [];  
                var name;  
                for (var p=0,len=mapSeries.data.length; p<len; p++) {  
                    name=mapSeries.data[p].name;  
                    mapSeries.data[p].selected=selected[name];  
                    if (selected[name]) {  
                        alert("todo 变更订单状态监控省份为"+name); //这里只是简单的做一个事例说明，弹出用户所选的省，如需做其他的扩展，可以在这里边添加相应的操作   
  
                    }  
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
     
    </script>
</body>
</html>