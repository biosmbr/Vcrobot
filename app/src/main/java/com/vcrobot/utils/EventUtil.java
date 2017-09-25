package com.vcrobot.utils;

/**
 * Created by Dolphix.J Qing on 2016/5/8.
 */
public final class EventUtil {
    //科大讯飞平台
    public static final String APIID = "5739b3ed";
    //图灵机器人平台
    public static final String TURING_URL = "http://www.tuling123.com/openapi/api";
    public static final String TURING_KEY = "b1a3d0aaa661812b28170f49919b834e";
    //Face++平台
    public static final String API_KEY = "77f528216d0b06dbbb77b848ff2fc71c";
    public static final String api_secret = "Q2_wX1Gza1LTxESR3l9XkVThhphfJ96s";
    //服务器地址
    public static final String URL_LOGIN = "http://10.100.58.51:8080/NetRobot/servlet/Login";
    public static final String URL_REGISTER = "http://10.100.58.51:8080/NetRobot/servlet/Register";
    public static final String URL_VCROBOT = "http://10.100.58.51:8080/NetRobot/servlet/Vcrobot";
    public static final String URL_QA = "http://10.100.58.51:8080/NetRobot/servlet/EvalQA";

    //所有自定义常量
    public static final int SEND = 1;
    public static final int RECEIVE = 2;
    public static final int LOAD_FINISH = 3;
    public static final int TYPE_TEXT = 4;
    public static final int TYPE_VOICE = 5;
    public static final int MSG_LOGIN = 6;
    public static final int MSG_LOGIN_ERR = 7;
    public static final int MSG_LOGIN_SUCCESS = 8;
    public static final int MSG_REG_SUCCESS = 9;
    public static final int SERVICE_ERR = 10;
    public static final int NET_ERR = 11;
    public static final int NET_OK = 12;
    public static final int CAMERA_HAS_STARTED_PREVIEW = 13;
    public static final int HIDE_FACE = 14;
    public static final int SHOW_FACE = 15;
    public static final int CAMERA_DOTAKE = 16;
    public static final int NOT_EXIST_FACE = 17;
    public static final int FACE_TRAIN_FINISH = 18;
    public static final int FACE_TRAIN_ERR = 19;
    public static final int FACE_CAN_TRAIN = 20;
    public static final int FACE_VERIFY_SUCCESS = 21;
    public static final int FACE_VERIFY_FAIL = 22;
    public static final int FACE_DETECT_START = 23;
    public static final int ENV_ERR = 24;
    public static final int SYS_CAMERA = 25;
    public static final int FACE_DRAW_DETECT_DATA = 26;
    public static final int FACE_ADD_SUCCESS = 27;
    public static final int FACE_ADD_FAIL = 28;
}
