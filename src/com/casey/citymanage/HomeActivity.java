package com.casey.citymanage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.http.KJFileParams;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.utils.ImageLoadutils;

import cn.jpush.android.api.JPushInterface;

import com.casey.citymanage.base.LZBasePhotoActivity;
import com.casey.citymanage.utils.Constants;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 *  @author kermit  E-mail: kermitcasey@163.com  
 *  @version 创建时间：2014年12月19日 下午5:53:34    类说明  
 *
 */
public class HomeActivity extends LZBasePhotoActivity {

	ImageView mSinginPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		context = this;
		try {
			ImageLoadutils.initImageLoader(this);
		} catch (Exception e) {

		}
		initView();

	}

	@Override
	protected void initView() {

		super.initView();
		HideBack();
		mSinginPhoto = (ImageView) findViewById(R.id.mSingInPhoto);

		mSinginPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				refershAddress();
				doTakePhoto();
			}
		});
		mSinginPhoto.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				showDeleteDialog(0);
				return true;
			}
		});
		mSinginPhoto.setLongClickable(false);
	}

	@Override
	public void mOnClickHeadMenu() {
		showActivity(this, UserLoginActivity.class);
	}

	public void mSingIn(View v) {
		if (Constants.userBean == null) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			return;
		}
		if (imageLists.size() <= 0) {
			Toast.makeText(this, "请拍照后签到", Toast.LENGTH_SHORT).show();
			return;
		}
		transparentDialog.show();
		try {
			KJFileParams params = new KJFileParams();
			params.put("latitude", String.valueOf(Constants.mLocation.getLatitude()));
			params.put("longitude", String.valueOf(Constants.mLocation.getLongitude()));
			params.put("phone", Constants.userBean.getPhone());
			params.put("method", "uploadImage");
			if (!TextUtils.isEmpty(Constants.singinid)) {
				params.put("id", Constants.singinid);
			}
			params.put(new File(imageLists.get(0).path));
			Constants.mHttpClient.post(Constants.BASE_IP + "UpLoadData.ashx", params, new StringCallBack() {

				@Override
				public void onSuccess(String content) {
					KJLoger.debug_e("HomeActivity", content);
					if (transparentDialog.isShowing()) {
						transparentDialog.dismiss();
					}
					Constants.singinid = null;
					refershView(0);
					Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					KJLoger.debug_e("HomeActivity", strMsg);
					if (transparentDialog.isShowing()) {
						transparentDialog.dismiss();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 刷新界面
	 */
	private void refershView(int position) {
		ImageLoadutils.imageLoader.displayImage("drawable://" + R.drawable.please_photo, mSinginPhoto);
		mSinginPhoto.setLongClickable(false);
		File file = new File(imageLists.get(position).path);
		if (file.exists()) {
			file.delete();
		}
		imageLists.clear();
	}

	/**
	 * 刷新地址
	 */
	private void refershAddress() {
		if (Constants.mLocation != null) {
			photoWordType = Constants.mLocation.getAddress();
		} else {
			photoWordType = "未定位成功";
		}
	}

	@Override
	public void showDeleteDialogOK(int position) {
		refershView(position);
		Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void doTakePhotoCall(String path) {
		imageLists.clear();
		super.doTakePhotoCall(path);
		mSinginPhoto.setLongClickable(true);
		ImageLoadutils.imageLoader.displayImage("file://" + path, mSinginPhoto);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
