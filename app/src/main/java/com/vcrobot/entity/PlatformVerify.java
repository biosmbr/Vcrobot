package com.vcrobot.entity;

import android.content.Context;

import com.vcrobot.bean.FaceRecognition;
import com.vcrobot.bean.FaceTrain;
import com.vcrobot.utils.LocalInfo;
import com.vcrobot.utils.NetTask;

import org.json.JSONObject;

/**
 * Created by Dolphix.J Qing on 2016/5/21.
 */
public class PlatformVerify {
    private static VerifyCallback callback;
    public static void setVerifyCallback(VerifyCallback verifyCallback){
        callback = verifyCallback;
    }


    public static void verify( String faceID, String user) {

        //发送不采用MD5加密
        String result = NetTask.doGet(FaceRecognition.verify(faceID, user));

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        callback.verifyResult(jsonObject);
    }

    public interface VerifyCallback{
        void verifyResult(JSONObject rst);
    }
}
