package com.casey.citymanage.base;

import org.kymjs.aframe.widget.TransparentDialog;

import cn.jpush.android.api.InstrumentedActivity;

import com.casey.citymanage.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
/**
 *  @author kermit  E-mail: kermitcasey@163.com  
 *  @version 创建时间：2014年12月19日 下午5:53:34    类说明  
 *
 */

public class LZBaseActivity extends InstrumentedActivity {

	public TransparentDialog transparentDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无title
		transparentDialog = new TransparentDialog(this, R.style.TransparentDialog, R.drawable.transparentdialog2);

	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	public void skipActivity(Activity aty, Class<?> cls) {
		showActivity(aty, cls);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	public void skipActivity(Activity aty, Intent it) {
		showActivity(aty, it);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
		showActivity(aty, cls, extras);
		aty.finish();
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	public void showActivity(Activity aty, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	public void showActivity(Activity aty, Intent it) {
		aty.startActivity(it);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
		Intent intent = new Intent();
		intent.putExtras(extras);
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

}
