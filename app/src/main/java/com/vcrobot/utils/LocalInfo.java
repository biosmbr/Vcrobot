package com.vcrobot.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Dolphix.J Qing on 2016/5/21.
 */
public final class LocalInfo {

    /**
     * 获取本地用户名
     * @param context
     * @return
     */
    public static String getLocalUserName(Context context) {
        //获得sp实例对象
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        //如果登陆过，直接登录(false为默认值，如果EXIST不存在，则false)
        if (sp.getBoolean("EXIST", false)) {
            String email = sp.getString("email", "");
            int end = email.indexOf("@");
            return email.substring(0,end);
        }
        return "vcrobot";
    }

    /**
     * 清空本地数据
     * @param context
     */
    public static void clearLocalData(Context context){
        //获得sp实例对象
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
