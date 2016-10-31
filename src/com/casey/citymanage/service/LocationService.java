package com.casey.citymanage.service;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.casey.citymanage.application.MyApplication;
import com.casey.citymanage.utils.Constants;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;

/**
 *  @author kermit  E-mail: kermitcasey@163.com  
 *  @version 创建时间：2014年12月19日 下午5:53:34    类说明  
 *
 */
public class LocationService extends Service {
	private final String TAG = "LocationService";

	private Timer timer = null;
	private TimerTask task = null;

	private PowerManager.WakeLock wakeLock = null;

	private LocationManagerProxy mLocationManagerProxy = null;
	private AMapLocationListener amapLocationListener = new AMapLocationListener() {
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		}

		@Override
		public void onProviderEnabled(String arg0) {
		}

		@Override
		public void onProviderDisabled(String arg0) {
		}

		@Override
		public void onLocationChanged(Location arg0) {
		}

		@Override
		public void onLocationChanged(AMapLocation location) {
			// KJLoger.debug_i(TAG, "经度：" + location.getLatitude() + "纬度:" +
			// location.getLongitude());
			Constants.mLocation = location;
		}
	};

	// private Request request = null;

	@Override
	public void onCreate() {
		super.onCreate();
		acquireWakeLock();
		SetAndStartTimer();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		KJLoger.debug_e(TAG, "Service onStartCommand");
		SetAndStartLocation();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		KJLoger.debug_e(TAG, "Service onBind");
		SetAndStartLocation();
		return null;
	}

	private void SetAndStartTimer() {
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				if (Constants.mLocation != null) {
					double latitude = Constants.mLocation.getLatitude();
					double longitude = Constants.mLocation.getLongitude();
					if (latitude != 0 && longitude != 0 && Constants.userBean != null && checkTimeIsAllow()) {
						submitLocation(latitude + "", longitude + "");
					}
				}
			}
		};
		timer.scheduleAtFixedRate(task, 0, Constants.spaceTime * 1000);
	}

	private void SetAndStartLocation() {
		mLocationManagerProxy = MyApplication.startLocation(amapLocationListener);
		mLocationManagerProxy.setGpsEnable(true);
	}

	private void deactivate() {
		Constants.mLocation = null;
		amapLocationListener = null;
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.removeUpdates(amapLocationListener);
			mLocationManagerProxy = null;
		}
	}

	private void acquireWakeLock() {
		if (wakeLock == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
			wakeLock.acquire();
		}
	}

	private void releaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}
	}

	private synchronized void submitLocation(String lat, String lon) {
		KJStringParams params = new KJStringParams();
		params.put("latitude", lat);
		params.put("longitude", lon);
		params.put("phone", Constants.userBean.getPhone());
		params.put("method", "location");
//		KJLoger.debug_i(TAG, Constants.BASE_IP + "UpLoadData.ashx?" + params.toString());
		Constants.mHttpClient.post(Constants.BASE_IP + "UpLoadData.ashx", params, null);
	}

	private boolean checkTimeIsAllow() {
		Calendar c = Calendar.getInstance();
		int hour = c.getTime().getHours();
		if (hour >= Constants.starttime && hour <= Constants.endtime) {
			return true;
		}
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		releaseWakeLock();

		timer = null;
		task = null;

		deactivate();
	}
}