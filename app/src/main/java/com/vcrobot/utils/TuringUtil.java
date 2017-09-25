package com.vcrobot.utils;

import android.util.Log;

/**
 * Created by Dolphix.J Qing on 2016/5/8.
 */
public class TuringUtil {

    /**
     * 拼接请求url
     * @param info
     * @return
     */
    public static String getRequestUrl(String info){
        //http://www.tuling123.com/openapi/api?key=b1a3d0aaa661812b28170f49919b834e&info=""
        Log.i("dol",EventUtil.TURING_URL+"?key="+ EventUtil.TURING_KEY+"&info="+info);
        return EventUtil.TURING_URL+"?key="+ EventUtil.TURING_KEY+"&info="+info;
    }

}
