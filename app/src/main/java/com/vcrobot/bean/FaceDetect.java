package com.vcrobot.bean;

import android.graphics.Bitmap;

import com.vcrobot.utils.EventUtil;

public class FaceDetect {

	public static final String DETECT = "http://apicn.faceplusplus.com/v2/detection/detect";

	/**
	 * 检测人脸信息
	 * http://apicn.faceplusplus.com/v2/detection/detect?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&url=http%3A%2F%2Ffaceplusplus.com%2Fstatic%2Fimg%2Fdemo%2F1.jpg&attribute=glass,pose,gender,age,race,smiling
	 * @param img  (二进制数据< 1M)
	 * @return
	 */
	public static String detect(Bitmap img){
		return DETECT+"?api_key="+ EventUtil.API_KEY+"&api_secret="+EventUtil.api_secret+"&img="+img+"&attribute=glass,pose,gender,age,race,smiling";
	}
	/**
	 * 检测人脸信息
	 * @param url
	 * @return
	 */
	public static String detect(String url){
		return DETECT+"?api_key="+EventUtil.API_KEY+"&api_secret="+EventUtil.api_secret+"&url="+url+"&attribute=glass,pose,gender,age,race,smiling";
	}

	/**
	 * 检测给定人脸(Face)相应的面部轮廓，五官等关键点的位置，包括25点和83点两种模式。
	 * http://api.faceplusplus.com/detection/landmark?api_secret=DEMO_SECRET&api_key=DEMO_KEY&face_id=a4bae295cc205bbc5b65e3522164c6d9&type=83p
	 * @param faceID
	 * @param type 25点，83点
	 * @return
	 */
	public static String landmark(String faceID, String type){
		return DETECT+"?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"face_id="+faceID+"&type="+type;
	}
}
