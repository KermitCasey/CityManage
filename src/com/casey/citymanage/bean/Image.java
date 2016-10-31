package com.casey.citymanage.bean;

import java.io.Serializable;

import com.casey.citymanage.utils.Constants;
/**
 *  @author kermit  E-mail: kermitcasey@163.com  
 *  @version 创建时间：2014年12月19日 下午5:53:34    类说明  
 *
 */
public class Image implements Serializable {

	private static final long serialVersionUID = 1L;
	public String name;
	public String path;
	public String type;

	public Image() {
		name = Constants.getUUID();
		type = ".jpg";
	}
}
