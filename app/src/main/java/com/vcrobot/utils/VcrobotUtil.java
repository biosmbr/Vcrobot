package com.vcrobot.utils;

/**
 * Created by Dolphix.J Qing on 2016/5/24.
 */
public class VcrobotUtil {

    /**
     * 拼接请求url
     * @return
     */
    public static String getRequestUrl(String keywords){
        String url = EventUtil.URL_VCROBOT+"?keywords="+keywords;
        return url;
    }


    /**
     * 拼接请求url
     * @return
     */
    public static String getQAUrl(String rid,int op){
        String url = EventUtil.URL_QA+"?rid="+rid+"&op="+op;
        return url;
    }
}
