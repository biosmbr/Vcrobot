package com.vcrobot.bean;

import java.util.List;
import com.vcrobot.utils.EventUtil;

public class FacePerson {

	private static final String PERSON = "https://apicn.faceplusplus.com/v2/person/";

	/**
	 * 创建一个人
	 * https://apicn.faceplusplus.com/v2/person/create?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&tag=demotest&person_name=NicolasCage&group_name=SuperStars
	 * @param tag
	 * @param personName
	 * @param groupName
	 * @return
	 */
	public static String create(String tag, String personName, String groupName){
		return PERSON+"create?api_key="+EventUtil.API_KEY+"&api_secret="+EventUtil.api_secret+"&tag="+tag+"&person_name="+personName+"&group_name="+groupName;
	}
	/**删除一个人
	 * https://apicn.faceplusplus.com/v2/person/delete?api_secret=YOUR_API_SECRET&api_key=YOUR_API_KEY&person_name=NicolasCage
	 * @param personName
	 * @return
	 */
	public static String delete(String personName){
		return PERSON+"delete?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&person_name="+personName;
	}

	/**
	 * 加入一张人脸
	 * https://apicn.faceplusplus.com/v2/person/add_face?api_secret=YOUR_API_SECRET&face_id=169a5&api_key=YOUR_API_KEY&person_name=NicolasCage
	 * @param faceID
	 * @param personName
	 * @return
	 */
	public static String addFace(String faceID, String personName){
		return PERSON+"add_face?api_secret="+EventUtil.api_secret+"&face_id="+faceID+"&api_key="+EventUtil.API_KEY+"&person_name="+personName;
	}
	/**
	 * 批量，加入多张人脸
	 * @param faceIDs
	 * @param personName
	 * @return
	 */
	public static String addFace(List<String> faceIDs, String personName){
		StringBuilder faceID = new StringBuilder();
		boolean skip = false;
		for (String fID : faceIDs) {
			if (skip) {
				faceID.append(","+fID);
				skip = true;
			}else {
				faceID.append(fID);
			}
		}
		return PERSON+"add_face?api_secret="+EventUtil.api_secret+"&face_id="+faceID.toString()+"&api_key="+EventUtil.API_KEY+"&person_name="+personName;
	}
	/**
	 * 删除一张人脸
	 * https://apicn.faceplusplus.com/v2/person/remove_face?api_secret=YOUR_API_SECRET&face_id=169a167&api_key=YOUR_API_KEY&person_name=NicolasCage
	 * @param faceID
	 * @param personName
	 * @return
	 */
	public static String delete(String faceID, String personName){
		return PERSON+"remove_face?api_secret="+EventUtil.api_secret+"&face_id="+faceID+"&api_key="+EventUtil.API_KEY+"&person_name="+personName;
	}
	/**
	 * 批量，删除多张人脸
	 * @param faceIDs
	 * @param personName
	 * @return
	 */
	public static String delete(List<String> faceIDs, String personName){
		StringBuilder faceID = new StringBuilder();
		boolean skip = false;
		for (String fID : faceIDs) {
			if (skip) {
				faceID.append(","+fID);
				skip = true;
			}else {
				faceID.append(fID);
			}
		}
		return PERSON+"remove_face?api_secret="+ EventUtil.api_secret+"&face_id="+faceID.toString()+"&api_key="+EventUtil.API_KEY+"&person_name="+personName;
	}
	/**
	 * 设置某人信息
	 * https://apicn.faceplusplus.com/v2/person/set_info?api_key=YOUR_API_KEY&tag=new_tag_for_Cage&person_name=NicolasCage&api_secret=YOUR_API_SECRET
	 * @param tag
	 * @param personName
	 * @return
	 */
	public String setInfo(String tag, String personName){
		return PERSON+"set_info?api_key="+EventUtil.API_KEY+"&tag="+tag+"&person_name="+personName+"&api_secret="+EventUtil.api_secret;
	}
	/**
	 * 获取一个人信息
	 * https://apicn.faceplusplus.com/v2/person/get_info?api_secret=YOUR_API_SECRET&api_key=YOUR_API_KEY&person_name=NicolasCage
	 * @param personName
	 * @return
	 */
	public static String getInfo(String personName){
		return PERSON+"get_info?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&person_name="+personName;
	}
}
