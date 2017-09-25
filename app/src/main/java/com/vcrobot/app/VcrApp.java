package com.vcrobot.app;

import android.app.Application;
import com.iflytek.cloud.SpeechUtility;
import com.vcrobot.utils.EventUtil;

/**
 * Created by Dolphix.J Qing on 2016/5/13.
 */
public class VcrApp extends Application{
    @Override
    public void onCreate() {
        //科大讯飞语音云识别
        SpeechUtility.createUtility(VcrApp.this, "appid="+ EventUtil.APIID);
        super.onCreate();
    }
}
