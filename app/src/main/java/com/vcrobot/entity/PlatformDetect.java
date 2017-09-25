package com.vcrobot.entity;

import android.graphics.Bitmap;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.vcrobot.bean.FaceDetect;
import com.vcrobot.utils.EventUtil;
import com.vcrobot.utils.NetTask;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dolphix.J Qing on 2016/5/19.
 */
public class PlatformDetect {

    private static final String TAG = "Dolphix.J Qing";
    private static DetectCallback callback = null;
//    private RequestQueue queue;

    public static void setDetectCallback(DetectCallback detectCallback) {
        callback = detectCallback;
    }


//    /**
//     * 构造HashMap串
//     * @return
//     */
//    private Map<String, String> getMapParams(Bitmap bitmap)  {
//        Map<String,String> map = new HashMap<String, String>();
//        map.put("api_key",EventUtil.API_KEY);
//        map.put("api_secret",EventUtil.api_secret);
//        map.put("img", NetTask.convertBitmap2String(bitmap));
//        map.put("attribute","glass,pose,gender,age,race,smiling");
//        return map;
//    }


    /**
     * 检测人脸
     * @param b
     */
    public static void detect(final File b) {

        //发送不采用MD5加密
        String result = NetTask.doPost(FaceDetect.DETECT, getKey(),getValue(), "img", b);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        callback.detectResult(jsonObject);
    }
    /**
     * 检测完毕，回调
     */
    public interface DetectCallback {
        void detectResult(JSONObject rst);
    }

    /**
     * 构造并获取key
     * @return
     */
    private static List<String> getKey(){
        List<String> key = new ArrayList<String>();
        key.add("api_key");
        key.add("api_secret");
        key.add("attribute");
        return key;
    }

    /**
     * 构造并获取value
     * @return
     */
    private static List<String> getValue(){
        List<String> value = new ArrayList<String>();
        value.add(EventUtil.API_KEY);
        value.add(EventUtil.api_secret);
        value.add("glass,pose,gender,age,race,smiling");
        return value;
    }
//        queue = Volley.newRequestQueue(context);
//        this.bitmap = bitmap;
//        JSONObject jsonObject = null;
//        try{
//            jsonObject = new JSONObject(mapToJson(getMapParams()));
//        }catch (Exception e){e.printStackTrace();}
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FaceDetect.DETECT, null/*jsonObject*/,
//            new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject jsonObject) {
//                    callback.detectResult(jsonObject);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    Log.i(TAG, "onErrorResponse: "+volleyError.toString());
//                }
//        });
//        queue.add(jsonObjectRequest);
//    }



//    /**
//     * 构造HashMap串
//     * @return
//     */
//    private Map<String, String> getMapParams()  {
//        Map<String,String> map = new HashMap<String, String>();
//        map.put("api_key",EventUtil.API_KEY);
//        map.put("api_secret=",EventUtil.api_secret);
//        map.put("img",convertBitmap2String(bitmap));
//        map.put("attribute","glass,pose,gender,age,race,smiling");
//        return map;
//    }
//
//
//    /**
//     * map转换为json字符串
//     * @param map
//     * @return
//     */
//    public static String mapToJson(Map map) {
//        String string = "{";
//        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
//            Map.Entry e = (Map.Entry) it.next();
//            string += "'" + e.getKey() + "':";
//            string += "'" + e.getValue() + "',";
//        }
//        string = string.substring(0, string.lastIndexOf(","));
//        string += "}";
//        return string;
//    }
//    /**
//     * 转换Bitmap为String，必须Base64格式String
//     * @param bitmap
//     * @return
//     */
//    public static String convertBitmap2String(Bitmap bitmap)
//    {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();// outputstream
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] appicon = stream.toByteArray();// 转为byte数组
//        return Base64.encodeToString(appicon, Base64.DEFAULT);
//
//    }
//
//    /**
//     * 转换Bitmap为byte[]
//     * @param bitmap
//     * @return
//     */
//    public static byte[] convertBitmap2ByteArr(Bitmap bitmap){
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] array = stream.toByteArray();
//        return array;
//    }


    //    public void detect(final Bitmap image) {
//
//        new Thread(new Runnable() {
//
//            public void run() {
//                HttpRequests httpRequests = new HttpRequests(EventUtil.API_KEY, EventUtil.api_secret, true, false);
//
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                float scale = Math.min(1, Math.min(600f / image.getWidth(), 600f / image.getHeight()));
//                Matrix matrix = new Matrix();
//                matrix.postScale(scale, scale);
//
//                Bitmap imgSmall = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
//
//                imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] array = stream.toByteArray();
//
//                try {
//                    //人脸检测
//                    JSONObject result = httpRequests.detectionDetect(new PostParameters().setImg(array));
//                    //检测完成
//                    if (callback != null) {
//                        Log.i(TAG, "run: "+result.toString());
//                        callback.detectResult(result);
//                    }
//                } catch (FaceppParseException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//    }
}
