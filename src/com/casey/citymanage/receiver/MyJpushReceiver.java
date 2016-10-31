package com.casey.citymanage.receiver;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.KJLoger;

import com.amap.api.location.c;
import com.casey.citymanage.HomeActivity;
import com.casey.citymanage.utils.Constants;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyJpushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			printBundle(bundle);
			getSinginID(bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			// 打开自定义的Activity
			Intent i = new Intent(context, HomeActivity.class);
			i.putExtras(bundle);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {

		} else {

		}
	}

	// 打印所有的 intent extra 数据
	private static void getSinginID(Bundle bundle) {
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				try {
					JSONObject json = new JSONObject(bundle.getString(key));
					Constants.singinid = json.optString("ID");
					break;
				} catch (Exception e) {
					e.printStackTrace();
					Constants.singinid = null;
				}
			}
		}
		KJLoger.debug_i(TAG, Constants.singinid);
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));

			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				sb.append("\nextra key" + ", value" + bundle.getString(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		KJLoger.debug_i(TAG, sb.toString());
		return sb.toString();
	}
}
