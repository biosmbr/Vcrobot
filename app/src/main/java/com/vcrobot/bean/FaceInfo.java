package com.vcrobot.bean;

import android.util.Log;

/**
 * Created by Dolphix.J Qing on 2016/5/21.
 */
public final class FaceInfo {
    private static final String TAG = "Dolphix.J Qing";
    public static String face_id;
    //-----------------------------
    public static int age_value;
    public static String gender_value;
    public static String glass_value;
    public static String race_value;
    //-----------------------------
    public static float center_x;
    public static float center_y;
    public static float eye_left_x;
    public static float eye_left_y;
    public static float eye_right_x;
    public static float eye_right_y;
    public static float face_height;
    public static float face_width;
    public static float mouth_left_x;
    public static float mouth_left_y;
    public static float mouth_right_x;
    public static float mouth_right_y;
    public static float nose_x;
    public static float nose_y;


    public static void changePercentToValue(float width, float height){
        width /= 100f;
        height /= 100f;

        center_x *= width;
        center_y *= height;
        eye_left_x *= width;
        eye_left_y *= height;
        eye_right_x *= width;
        eye_right_y *= height;
        face_width *= width;
        face_height *= height;
        mouth_left_x *= width;
        mouth_left_y *= height;
        mouth_right_x *= width;
        mouth_right_y *= height;
        nose_x *= width;
        nose_y *= height;
        Log.i(TAG, "changePercentToValue: "+center_x+" "+center_y +" "+eye_left_x+" "+eye_left_y+" "+eye_right_x+
        " "+eye_right_y+" "+face_width+" "+face_height+" "+mouth_left_x+" "+mouth_left_y+" "+mouth_right_x+
        " "+mouth_right_y+" "+nose_x+" "+nose_y);
    }

}
