package com.vcrobot.utils;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dolphix.J Qing on 2016/5/5.
 */
public class NetTask extends AsyncTask<String, Void, String>{

    private HttpClient httpClient;
    private HttpGet httpGet;
    private HttpResponse httpResponse;
    private HttpEntity httpEntity;
    private InputStream inputStream;
    private HttpGetListenerr httpGetListenerr;

    private String url;

    public NetTask(String url, HttpGetListenerr httpGetListenerr) {
        this.url = url;
        this.httpGetListenerr = httpGetListenerr;
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            httpClient = new DefaultHttpClient();
            httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while(null != (line = bufferedReader.readLine())){
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        }catch (Exception e){e.printStackTrace();}

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        httpGetListenerr.getUrlData(s);
        super.onPostExecute(s);
    }


    /**
     * 数据回调接口
     */
    public interface HttpGetListenerr{
        void getUrlData(String data);
    }


    /**
     * GET,发送url返回浏览器Response
     * @param url
     * @return
     */
    public static String doGet(String url, List<String> key, List<String> value) {
        StringBuffer data = new StringBuffer();
        for(int i = 0; i < key.size(); ++i){
            //MD5加密
            if (0 == i){
                data.append(url+"?"+key.get(i)+"="+ParseMD5.parseStr2MD5(value.get(i)));
            }else{
                data.append("&"+key.get(i)+"="+ParseMD5.parseStr2MD5(value.get(i)));
            }
        }
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(data.toString());
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while(null != (line = bufferedReader.readLine())){
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        }catch (Exception e){e.printStackTrace();}

        return null;
    }

    /**
     * POST,发送url返回浏览器Response
     * @param key
     * @param value
     * @return
     */
    public static String doPost(String url, List<String> key, List<String> value) {

        List<NameValuePair> pairList = new ArrayList<NameValuePair>();

        for (int i = 0; i < key.size(); ++i){
            //MD5加密
            pairList.add(new BasicNameValuePair(key.get(i), ParseMD5.parseStr2MD5(value.get(i))));
        }

        try{
            // URL使用基本URL即可，其中不需要加参数
            HttpPost httpPost = new HttpPost(url);
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
            // 将请求体内容加入请求中
            httpPost.setEntity(requestHttpEntity);
            // 需要客户端对象来发送请求
            HttpClient httpClient = new DefaultHttpClient();
            // 发送请求
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while(null != (line = bufferedReader.readLine())){
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        }catch (Exception e){e.printStackTrace();}

        return null;
    }
}
