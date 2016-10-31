package com.casey.citymanage;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.widget.ClearEditText;

import cn.jpush.android.api.JPushInterface;

import com.casey.citymanage.base.LZHeadBaseActivity;
import com.casey.citymanage.bean.UserBean;
import com.casey.citymanage.utils.Constants;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  @author kermit  E-mail: kermitcasey@163.com  
 *  @version 创建时间：2014年12月19日 下午5:53:34    类说明  
 *
 */
public class UserLoginActivity extends LZHeadBaseActivity {

	ClearEditText username, userpassword;
	Context context;
	LinearLayout inputLayout;
	TextView loginAlready;
	Button loginSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login);
		context = this;
		initView();
	}

	@Override
	protected void initView() {
		super.initView();
		setHeadTitle(getResources().getString(R.string.user_center));
		changeMenuBg(false, 0);
		loginSubmit = (Button) findViewById(R.id.userSubmit);
		inputLayout = (LinearLayout) findViewById(R.id.input_formlayout);
		loginAlready = (TextView) findViewById(R.id.login_already);
		username = (ClearEditText) findViewById(R.id.username);
		userpassword = (ClearEditText) findViewById(R.id.userpassword);
		loginSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Constants.userBean == null) {
					isAllowLogin();
				} else {
					LoginOut();
				}
			}
		});

		if (Constants.userBean == null) {
			username.setText("15871751277");
			userpassword.setText("123");
			loginAlready.setVisibility(View.GONE);
			inputLayout.setVisibility(View.VISIBLE);
			loginSubmit.setText(getResources().getString(R.string.user_login));
		} else {
			loginAlready.setVisibility(View.VISIBLE);
			inputLayout.setVisibility(View.GONE);
			loginSubmit.setText(getResources().getString(R.string.user_loginout));
		}
		
		
	}

	@Override
	public void mOnClickHeadMenu() {

	}

	/**
	 * 判断条件是否满足
	 */
	private void isAllowLogin() {
		if (TextUtils.isEmpty(username.getText())) {
			Toast.makeText(this, getResources().getString(R.string.username_nonull), Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(userpassword.getText())) {
			Toast.makeText(this, getResources().getString(R.string.userpassword_nonull), Toast.LENGTH_SHORT).show();
		} else {
			LoginSubmit();
		}
	}

	/**
	 * 登录
	 */
	private void LoginSubmit() {
		transparentDialog.show();
		KJStringParams params = new KJStringParams();
		params.put("phone", username.getText().toString().trim());
		// params.put("pass",
		// MD5.get32MD5(userpassword.getText().toString().trim()));
		params.put("method", "login");
		params.put("pass", userpassword.getText().toString().trim());
		KJLoger.debug_i("login", Constants.BASE_IP + "CheckUser.ashx?" + params.toString());
		Constants.mHttpClient.post(Constants.BASE_IP + "CheckUser.ashx", params, new StringCallBack() {

			@Override
			public void onSuccess(String content) {
				if (transparentDialog.isShowing()) {
					transparentDialog.dismiss();
				}
				KJLoger.debug_i("login", content);
				if ("success".equals(content)) {
					Constants.userBean = new UserBean(username.getText().toString().trim());
					JPushInterface.setAliasAndTags(context, Constants.userBean.getPhone(), null, null);
					onBackPressed();
					finish();
					Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "请检查用户名和密码！", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				KJLoger.debug_i("login", strMsg);
				if (transparentDialog.isShowing()) {
					transparentDialog.dismiss();
				}
				Toast.makeText(context, "请检查网络！", Toast.LENGTH_SHORT).show();
			}

		});
	}

	/*
	 * 用户登出
	 */
	private void LoginOut() {
		transparentDialog.show();
		KJStringParams params = new KJStringParams();
		params.put("method", "logout");
		Constants.mHttpClient.post(Constants.BASE_IP + "CheckUser.ashx", params, new StringCallBack() {

			@Override
			public void onSuccess(String content) {
				if (transparentDialog.isShowing()) {
					transparentDialog.dismiss();
				}
				KJLoger.debug_i("login", content);
				if ("success".equals(content)) {
					Constants.userBean = null;
					JPushInterface.setAliasAndTags(context, "", null, null);
					Toast.makeText(context, "账户注销成功", Toast.LENGTH_SHORT).show();
					onBackPressed();
					finish();
				} else {
					Toast.makeText(context, "账户注销失败", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				if (transparentDialog.isShowing()) {
					transparentDialog.dismiss();
				}
				KJLoger.debug_i("logout", strMsg);
				Toast.makeText(context, "请检查网络！", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();

			}
		});
	}
}
