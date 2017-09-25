package com.vcrobot.entity;

import android.content.Context;
import android.graphics.Bitmap;

import com.vcrobot.bean.FaceDetect;
import com.vcrobot.bean.FaceTrain;
import com.vcrobot.utils.LocalInfo;
import com.vcrobot.utils.NetTask;

import org.json.JSONObject;

/**
 * Created by Edchel on 2016/5/21.
 */
public class PlatformTrain {


    private static TrainCallback callback;
    public static void setTrainCallback(TrainCallback trainCallback){
        callback = trainCallback;
    }


    public static void train(String user) {

        //发送不采用MD5加密
        String result = NetTask.doGet(FaceTrain.verify(user));

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        callback.trainResult(jsonObject);
    }

    public interface TrainCallback{
        void trainResult(JSONObject rst);
    }
}
