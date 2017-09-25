package com.vcrobot.bean;

import java.util.List;
import com.vcrobot.utils.EventUtil;

public class FaceGroup{

	private static final String GROUP = "https://apicn.faceplusplus.com/v2/group/";
	/**
	 * 创建用户组
	 * https://apicn.faceplusplus.com/v2/group/create?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&tag=created_by_Alice&group_name=Family
	 * @param groupName
	 * @param tag
	 * @return
	 */
	public static String create(String tag,String groupName){
		return GROUP+"create?api_key="+ EventUtil.API_KEY+"&api_secret="+EventUtil.api_secret+"&tag="+tag+"&group_name="+groupName;
	}

	/**
	 * 删除用户组
	 * https://apicn.faceplusplus.com/v2/group/delete?api_secret=YOUR_API_SECRET&api_key=YOUR_API_KEY&group_name=Family
	 * @param groupName
	 * @return
	 */
	public static String delete(String groupName){
		return GROUP+"delete?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&group_name="+groupName;
	}

	/**
	 * 将某人加入用户组
	 * https://apicn.faceplusplus.com/v2/group/add_person?api_secret=YOUR_API_SECRET&api_key=YOUR_API_KEY&person_name=Bob,Alice&group_name=Family
	 * @param personName
	 * @param groupName
	 * @return
	 */
	public static String addPerson(String personName, String groupName){
		return GROUP+"add_person?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&person_name="+personName+"&group_name="+groupName;
	}
	/**
	 * 批量，加入用户组
	 * @param pNames
	 * @param groupName
	 * @return
	 */
	public static String addPerson(List<String> pNames, String groupName){
		StringBuilder personName = new StringBuilder();
		boolean skip = false;
		for (String pN : pNames) {
			if (skip) {
				personName.append(","+pN);
				skip = true;
			}else {
				personName.append(pN);
			}

		}
		return GROUP+"add_person?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&person_name="+personName.toString()+"&group_name="+groupName;
	}
	/**
	 * 将某人移除用户组
	 * https://apicn.faceplusplus.com/v2/group/remove_person?api_secret=YOUR_API_SECRET&api_key=YOUR_API_KEY&person_name=Bob,Alice&group_name=Family
	 * @param personName
	 * @param groupName
	 * @return
	 */
	public static String removePerson(String personName, String groupName){
		return GROUP+"remove_person?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&person_name="+personName+"&group_name="+groupName;
	}
	/**
	 * 批量，移除用户组
	 * @param pNames
	 * @param groupName
	 * @return
	 */
	public static String removePerson(List<String> pNames, String groupName){
		StringBuilder personName = new StringBuilder();
		boolean skip = false;
		for (String pN : pNames) {
			if (skip) {
				personName.append(","+pN);
				skip = true;
			}else {
				personName.append(pN);
			}

		}
		return GROUP+"remove_person?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&person_name="+personName.toString()+"&group_name="+groupName;
	}

	/**
	 * 设置组别信息
	 * https://apicn.faceplusplus.com/v2/group/set_info?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&tag=GroupVersion2.0&group_name=Family
	 * @param tag
	 * @param groupName
	 * @return
	 */
	public static String setInfo(String tag, String groupName){
		return GROUP+"set_info?api_key="+EventUtil.API_KEY+"&api_secret="+EventUtil.api_secret+"&tag="+tag+"&group_name="+groupName;
	}
	/**
	 * 获得组别信息
	 * https://apicn.faceplusplus.com/v2/group/get_info?api_secret=YOUR_API_SECRET&api_key=YOUR_API_KEY&group_name=Family
	 * @param groupName
	 * @return
	 */
	public static String getInfo(String groupName){
		return GROUP+"get_info?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&group_name="+groupName;
	}
}
