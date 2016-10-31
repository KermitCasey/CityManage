package com.casey.citymanage.base;

import com.casey.citymanage.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 *  @author kermit  E-mail: kermitcasey@163.com  
 *  @version 创建时间：2014年12月27日 下午10:43:28    类说明  
 *
 */

public abstract class LZHeadBaseActivity extends LZBaseActivity {

	protected TextView headTitle;
	protected RadioButton headMenu;

	protected void initView() {
		headTitle = (TextView) findViewById(R.id.whole_headtitle);
		headMenu = (RadioButton) findViewById(R.id.whole_headmenu);
		findViewById(R.id.whole_headback).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
				finish();
			}
		});
		headMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOnClickHeadMenu();
			}
		});
	}

	public void HideBack() {
		findViewById(R.id.whole_headback).setVisibility(View.GONE);
	}

	public void changeMenuText(String str) {
		// headMenu.setButtonDrawable();
		headMenu.setText(str);
	}

	public void changeMenuBg(boolean isShow, int resId) {
		if (isShow) {
			headMenu.setText("");
			headMenu.setBackgroundResource(resId);
		} else {
			headMenu.setVisibility(View.GONE);
		}

	}

	public abstract void mOnClickHeadMenu();

	public void setHeadTitle(String title) {
		headTitle.setText(title);
	}
}
