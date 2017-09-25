package com.vcrobot.bean;

import com.vcrobot.utils.EventUtil;

public class FaceTrain {

	private static final String TRAIN = "http://apicn.faceplusplus.com/v2/train/";

	/**
	 * 针对identify功能对一个Group进行训练。请注意:
	 * 在一个Group内进行identify之前，必须先对该Group进行Train
	 * 当一个Group内的数据被修改后(例如增删Person, 增删Person相关的Face等)，为使这些修改生效，Group应当被重新Train
	 * Train所花费的时间较长, 因此该调用是异步的，仅返回session_id。
	 * Train时需要保证group内的所有person均非空。
	 * 训练的结果可以通过/info/get_session查询。当训练完成时，返回值中将包含{"success": true}
	 * eg.https://apicn.faceplusplus.com/v2/train/identify?api_secret=YOUR_API_SECRET&api_key=YOUR_API_KEY&group_name=DemoGroup
	 * @param groupName
	 * @return
	 */
	public static String identify(String groupName){
		return TRAIN+"identify?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&group_name="+groupName;
	}

	/**
	 * 针对verify功能对一个person进行训练。请注意:
	 * 在一个person内进行verify之前，必须先对该person进行Train
	 * 当一个person内的数据被修改后(例如增删Person相关的Face等)，为使这些修改生效，person应当被重新Train
	 * Train所花费的时间较长, 因此该调用是异步的，仅返回session_id。
	 * 训练的结果可以通过/info/get_session查询。当训练完成时，返回值中将包含{"success": true}
	 * eg.https://apicn.faceplusplus.com/v2/train/verify?api_secret=YOUR_API_SECRET&api_key=YOUR_API_KEY&person_name=Demoperson
	 * @param personName
	 * @return
	 */
	public static String verify(String personName){
		return TRAIN+"verify?api_secret="+EventUtil.api_secret+"&api_key="+EventUtil.API_KEY+"&person_name="+personName;
	}


}
