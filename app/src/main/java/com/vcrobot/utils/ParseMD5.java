package com.vcrobot.utils;

/**
 * MD5加密
 * @author qingdujun
 *
 */
public class ParseMD5 extends Encrypt{

	/**
	 * 32位MD5
	 * @param str
	 * @return
	 */

	public static String parseStr2MD5(String str) {
		return encryt(str, MD5);
	}
	
	public static String parseStr2UpperMD5(String str){
		return parseStr2MD5(str).toUpperCase();
	}
	/**
	 * 16位MD5
	 * @param str
	 * @return
	 */
	public static String parseStr216MD5 (String str) {
		return parseStr2MD5(str).substring(8, 24);
	}

	public static String parseStr2Upper16MD5 (String str) {
		return parseStr2UpperMD5(str).substring(8, 24);
	}
}
