package com.vcrobot.utils;

import android.graphics.Bitmap;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.util.HttpURLConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Dolphix.J Qing on 2016/5/5.
 */
public class NetTask extends AsyncTask<String, Void, String>{

    private static final String TAG = "Dolphix.J Qing";
    private HttpClient httpClient;
    private HttpGet httpGet;
    private HttpResponse httpResponse;
    private HttpEntity httpEntity;
    private InputStream inputStream;
    private HttpGetListenerr httpGetListenerr;

    private String url;

    public NetTask(String url, HttpGetListenerr httpGetListenerr) {
        Log.i(TAG, "NetTask: "+url);
        this.url = url;
        this.httpGetListenerr = httpGetListenerr;
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            httpClient = new DefaultHttpClient();
            httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while (null != (line = bufferedReader.readLine())) {
                    stringBuffer.append(line);
                }
                return stringBuffer.toString();
            }
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
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while (null != (line = bufferedReader.readLine())) {
                    stringBuffer.append(line);
                }
                return stringBuffer.toString();
            }
        }catch (Exception e){e.printStackTrace();}

        return null;
    }

    /**
     * doGet发送网络请求
     * @param url
     * @return
     */
    public static String doGet(String url) {
        Log.i(TAG, "doGet: "+url);
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
//            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while (null != (line = bufferedReader.readLine())) {
                    stringBuffer.append(line);
                }
                return stringBuffer.toString();
//            }
        }catch (Exception e){e.printStackTrace();}

        return null;
    }

    /**
     * POST,发送url返回浏览器Response
     * @param key
     * @param value
     * @return
     */
    public static String doPost(String url, List<String> key, List<String> value, boolean isMD5) {

        List<NameValuePair> pairList = new ArrayList<NameValuePair>();

        for (int i = 0; i < key.size(); ++i){
            if (isMD5){
                //MD5加密
                pairList.add(new BasicNameValuePair(key.get(i), ParseMD5.parseStr2MD5(value.get(i))));
            }else{
                pairList.add(new BasicNameValuePair(key.get(i), value.get(i)));
                Log.i(TAG, "doPost: "+pairList.toString());
            }

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
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while (null != (line = bufferedReader.readLine())) {
                    stringBuffer.append(line);
                }
                return stringBuffer.toString();
            }
        }catch (Exception e){e.printStackTrace();}

        return null;
    }

    /**
     * 提交文字、图片，主要用于Face++
     * @param url
     * @param key  除了img以外所有
     * @param value  同上
     * @param bkey   "img"
     * @param b
     * @return
     */
    public static String doPost(String url, List<String> key, List<String> value, String bkey, File b){
        PostMethod postMethod = null;
        try {
//            //将文件存储于"注册目录"中，70%压缩
//            String bPath = saveBitmap(bitmap);
//            File b = new File(bPath);
            postMethod = new PostMethod(url);
            Part[] part = new Part[key.size()+1];
            //构造字符参数
            for (int i = 0; i < key.size(); ++i){
                part[i] = new StringPart(key.get(i),value.get(i));
            }
            //构造图片，文件
            part[part.length-1] = new FilePart(bkey,b);

            Log.i(TAG, "doPost: "+part.toString());

            MultipartRequestEntity mrp = new MultipartRequestEntity(part, postMethod.getParams());
            postMethod.setRequestEntity(mrp);
            org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
            client.getParams().setContentCharset("utf-8");
            client.executeMethod(postMethod);
            //接受返回信息
            InputStream inputStream = postMethod.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String str = "";
            while ((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != postMethod) {
                postMethod.releaseConnection();
            }
        }
        return null;
    }

    /**
     * map转换为json字符串
     * @param map
     * @return
     */
    public static String mapToJson(Map map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            string += "'" + e.getKey() + "':";
            string += "'" + e.getValue() + "',";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }
    /**
     * 转换Bitmap为String，必须Base64格式String
     * @param bitmap
     * @return
     */
    public static String convertBitmap2String(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] appicon = stream.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    /**
     * 转换Bitmap为byte[]
     * @param bitmap
     * @return
     */
    public static byte[] convertBitmap2ByteArr(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] array = stream.toByteArray();
        return array;
    }

    /**
     * 保存Bitmap到sdcard
     * @param b
     * @return
     */
    public static String saveBitmap(Bitmap b){

        String absImagePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/face_pp_detect/";
        //不存在，则创建
        File f = new File(absImagePath);
        if(!f.exists()){
            f.mkdirs();
        }
        String jpegName = absImagePath +"tmp.jpg";
        Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            //70%压缩率
            b.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "saveBitmap success");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "saveBitmap fail");
            e.printStackTrace();
        }
        return jpegName;
    }
}

//    /**
//     * POST,发送url返回浏览器Response
//     * @param url
//     * @param map
//     * @return
//     */
//    public static String doPost(String url, Map<String,String> map) {
//
//        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
//
//        for (int i = 0; i < map.size(); ++i){
//            pairList.add(new BasicNameValuePair(map.get(), map.get(i)));
//            Log.i(TAG, "doPost: "+pairList.toString());
//        }
//
//        try{
//            // URL使用基本URL即可，其中不需要加参数
//            HttpPost httpPost = new HttpPost(url);
//            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
//            // 将请求体内容加入请求中
//            httpPost.setEntity(requestHttpEntity);
//            // 需要客户端对象来发送请求
//            HttpClient httpClient = new DefaultHttpClient();
//            // 发送请求
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            InputStream inputStream = httpEntity.getContent();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            String line = null;
//            StringBuffer stringBuffer = new StringBuffer();
//            while(null != (line = bufferedReader.readLine())){
//                stringBuffer.append(line);
//            }
//            return stringBuffer.toString();
//        }catch (Exception e){e.printStackTrace();}
//
//        return null;
//    }

