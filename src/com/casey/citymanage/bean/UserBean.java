package com.casey.citymanage.bean;

/**
 *  @author kermit  E-mail: kermitcasey@163.com  
 *  @version 创建时间：2014年12月28日 下午8:55:18    类说明  
 *
 */
public class UserBean {

	private String phone;

	public UserBean() {

	}

	public UserBean(String phone) {
		setPhone(phone);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
