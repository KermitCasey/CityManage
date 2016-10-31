package com.casey.citymanage.application;

import java.io.File;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.http.KJHttp;

import android.content.ComponentName;
import android.content.Intent;
import android.app.Application;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import cn.jpush.android.api.JPushInterface;

import com.casey.citymanage.utils.Constants;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.casey.citymanage.service.LocationService;

/**
 *  @author kermit  E-mail: kermitcasey@163.com  
 *  @version 创建时间：2014年12月28日 下午8:41:50    类说明  
 *
 */

public class MyApplication extends Application {
	private static MyApplication instance;
	public static boolean hasNetwork = true;

	private static LocationManagerProxy mLocationManagerProxy = null;
	private static final int LCATION_TIME = 2 * 1000;
	private static final int LCATION_DISTANCE = 5;

	public MyApplication() {
		instance = this;
	}

	public synchronized static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.setDebugMode(Constants.is_debug); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		JPushInterface.setLatestNotificationNumber(getApplicationContext(), 1);
		// JPushInterface.setPushTime(getApplicationContext(),
		// Constants.getPushDays(), Constants.starttime, Constants.endtime);

		// 初始化网络
		Constants.mHttpClient = new KJHttp();
		Constants.mHttpClient.setTimeout(60 * 1000);
		// 调试开关
		KJLoger.IS_DEBUG = Constants.is_debug;
		// 创建图片保存文件夹
		createCacheImageDirs();
		// 定位管理
		mLocationManagerProxy = LocationManagerProxy.getInstance(getApplicationContext());

		ServiceConnection conn = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
			}

			@Override
			public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			}
		};
		Intent gps = new Intent(this, LocationService.class);
		bindService(gps, conn, BIND_AUTO_CREATE);
	}

	public static LocationManagerProxy startLocation(AMapLocationListener listener) {
		mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, LCATION_TIME, LCATION_DISTANCE, listener);
		return mLocationManagerProxy;
	}

	public static void removeUpdates(AMapLocationListener listener) {
		mLocationManagerProxy.removeUpdates(listener);
	}

	private void createCacheImageDirs() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + Constants.AppDir);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}