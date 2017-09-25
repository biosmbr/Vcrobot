package com.vcrobot.bean;

import android.graphics.Bitmap;

import com.vcrobot.utils.EventUtil;

public class FaceRecognition {

	private static final String RECOGNITION = "http://apicn.faceplusplus.com/v2/recognition/";


	/**
	 * 计算两个Face的相似性以及五官相似度
	 * eg.https://apicn.faceplusplus.com/v2/recognition/compare?api_secret=YOUR_API_SECRET&api_key=YOUR_API_KEY&face_id2=b8a697&face_id1=f3114d
	 * @param faceID2
	 * @param faceID1
	 * @return
	 */
	public static String compare(String faceID2, String faceID1) {
		return RECOGNITION+"compare?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&face_id2="+faceID2+"&face_id1="+faceID1;
	}

	/**
	 * 给定一个Face和一个Person，返回是否是同一个人的判断以及置信度。
	 * 注意，当Person中的信息被修改之后（增加，删除了Face等），为了保证结果与最新数据一致，Person应当被重新train。 见/train/verify 。
	 * 否则调用此API时将使用最后一次train时的数据。
	 * eg.https://apicn.faceplusplus.com/v2/recognition/verify?api_secret=YOUR_API_SECRET&face_id=7fa7e02&api_key=YOUR_API_KEY&person_name=FanBingBing
	 * @param faceID
	 * @param personName
	 * @return
	 */
	public static String verify(String faceID, String personName) {
		return RECOGNITION+"verify?api_secret="+EventUtil.api_secret+"&face_id="+faceID+"&api_key="+EventUtil.API_KEY+"&person_name="+personName;
	}
	/**
	 * 对于一个待查询的Face列表（或者对于给定的Image中所有的Face），在一个Group中查询最相似的Person。
	 * 注意，当Group中的信息被修改之后（增加了Person, Face等），为了保证结果与最新数据一致，Group应当被重新train. 见/train/identify 。
	 * 否则调用此API时将使用最后一次train时的数据。
	 * eg.https://apicn.faceplusplus.com/v2/recognition/identify?url=http%3A%2F%2Ffaceplusplus.com%2Fstatic%2Fimg%2Fdemo%2F1.jpg&api_secret=YOUR_API_SECRET&api_key=YOUR_API_KEY&group_name=DemoGroup
	 * @param url
	 * @param groupName
	 * @return
	 */
	public static String identify(String url, String groupName) {
		return RECOGNITION+"identify?url="+url+"&api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&group_name="+groupName;
	}
	/**
	 * 对于一个待查询的Face列表（或者对于给定的Image中所有的Face），在一个Group中查询最相似的Person。
	 * @param img POST<1M
	 * @param groupName
	 * @return
	 */
	public static String identify(Bitmap img, String groupName) {
		return RECOGNITION+"identify?img="+img+"&api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&group_name="+groupName;
	}
}
