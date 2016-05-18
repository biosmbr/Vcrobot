package com.vcrobot.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Dolphix.J Qing on 2016/5/8.
 */
public class JsonUtil {

    /**
     * 解析Json字符串，返回Key所对应的Value
     * @param jsonStr
     * @param key
     * @return
     */
    public static String parseJson2KeyStr(String jsonStr,String key){
        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            return jsonObject.getString(key);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    /**
     * 科大讯飞云语音识别json转换
     * @param json
     * @return
     */
    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}
