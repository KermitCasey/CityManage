package com.casey.citymanage.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.kymjs.aframe.http.KJHttp;

import com.amap.api.location.AMapLocation;
import com.casey.citymanage.bean.UserBean;

/**
 *  @author kermit  E-mail: kermitcasey@163.com  
 *  @version 创建时间：2014年12月28日 下午8:55:46    类说明  
 *
 */
public class Constants {

	// Dirs
	public static String AppDir = ".CityManage";

	public static Boolean is_debug = true;

	// URL
	public static String BASE_IP = "http://42.96.149.203:8098/";
	public static String BASE_URL = "";

	// HTTP
	public static KJHttp mHttpClient;

	// USRE
	public static UserBean userBean;

	// LOCATION
	public static AMapLocation mLocation;

	// SINGINID
	public static String singinid = null;

	// UUID
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	// PUSHLOCATION_TIME
	public static int spaceTime = 7;

	// JPUSH
	public static int starttime = 8;
	public static int endtime = 18;

	public static Set<Integer> getPushDays() {
		Set<Integer> daySet = new HashSet<Integer>();
		daySet.add(1);
		daySet.add(2);
		daySet.add(3);
		daySet.add(4);
		daySet.add(5);
		daySet.add(6);
		daySet.add(7);
		return daySet;
	}

	public static ArrayList<Integer> notifactionSet = new ArrayList<Integer>();

}
