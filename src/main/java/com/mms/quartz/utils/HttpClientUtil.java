package com.mms.quartz.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

    /**
     * httpclient get请求
     * 
     * @throws Exception
     */
    public static String httpGet(String url,Map<String,String> para) throws Exception {
    	
        //创建一个httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();

        //创建URIBuilder
        URIBuilder uri = new URIBuilder(url);

        //设置参数
        if(para != null){
        	for(String key : para.keySet()){
            	uri.addParameter(key, para.get(key));
            }
        }

        //创建httpGet对象
        HttpGet hg = new HttpGet(uri.build());

        //设置请求的报文头部的编码
        hg.setHeader(
            new BasicHeader("Content-Type", "application/json;charset=UTF-8"));

        //设置期望服务端返回的编码
        hg.setHeader(new BasicHeader("Accept", "application/json;charset=UTF-8"));

        //请求服务
        CloseableHttpResponse response = client.execute(hg);

        //获取响应码
        int statusCode = response.getStatusLine().getStatusCode();
        String resStr = "";
        if (statusCode == 200) {

            //获取返回实例entity
            HttpEntity entity = response.getEntity();

            //通过EntityUtils的一个工具方法获取返回内容
            resStr = EntityUtils.toString(entity, "UTF-8");

            //输出
            System.out.println("请求成功,请求返回内容为: " + resStr);
            
        } else {

            //输出
            System.out.println("请求失败,错误码为: " + statusCode);
            
        }

        //关闭response和client
        response.close();
        client.close();
        
        return resStr;
    }

    public String httpPost(String url,Map<String,String> para) throws Exception {

        //创建一个httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();

        //创建一个post对象
        HttpPost post = new HttpPost(url);

        //创建一个Entity，模拟表单数据
        List<NameValuePair> formList = new ArrayList<>();

        //添加表单数据
        for(String key : para.keySet()){
        	formList.add(new BasicNameValuePair(key, para.get(key)));
        }

        //包装成一个Entity对象
        StringEntity entity = new UrlEncodedFormEntity(formList, "utf-8");

        //设置请求的内容
        post.setEntity(entity);

        //设置请求的报文头部的编码
        post.setHeader(
            new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));

        //设置期望服务端返回的编码
        post.setHeader(new BasicHeader("Accept", "application/json;charset=utf-8"));

        //执行post请求
        CloseableHttpResponse response = client.execute(post);

        //获取响应码
        int statusCode = response.getStatusLine().getStatusCode();
        String resStr = "";
        if (statusCode == 200) {

            //获取数据
            resStr = EntityUtils.toString(response.getEntity());

            //输出
            System.out.println("请求成功,请求返回内容为: " + resStr);
            
        } else {

            //输出
            System.out.println("请求失败,错误码为: " + statusCode);
        }

        //关闭response和client
        response.close();
        client.close();
        return resStr;
    }
    
    public static void main(String[] args) throws Exception {
    	Map<String,String> para  = new HashMap<String,String>();
    	para.put("tracknum", "00144010");
        httpGet("http://localhost:8080/api/queryByTracknum",para);
}
}
