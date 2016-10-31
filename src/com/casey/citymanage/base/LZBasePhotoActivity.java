package com.casey.citymanage.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import org.kymjs.aframe.utils.ImageCompress;
import org.kymjs.aframe.utils.PhotoUtils;
import org.kymjs.aframe.utils.BitmapFileUtils;

import com.casey.citymanage.bean.Image;
import com.casey.citymanage.utils.Constants;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 *  @author kermit  E-mail: kermitcasey@163.com  
 *  @version 创建时间：2014年12月19日 下午5:53:34    类说明  
 *
 */
public abstract class LZBasePhotoActivity extends LZHeadBaseActivity {
	protected File pictureFile; // 拍照的保存路径
	protected Uri pictureUri; // 拍照保存路径的URi
	protected Bitmap selectBitmap;// 拍照
	protected static final int CAMERA_WITH_DATA = 0x21;
	protected static final int PHOTO_SCAN = 0x22;
	protected static final int SCAN_QCODE = 0x23;
	protected static final int RESULT_OK = -1;
	protected ArrayList<Image> imageLists = new ArrayList<Image>();
	protected Context context;
	public String photoWordType = "";

	protected void doTakePhoto() {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && Environment.getExternalStorageDirectory().exists()) {
				File pictureFileDir = new File(Environment.getExternalStorageDirectory(), Constants.AppDir);
				if (!pictureFileDir.exists()) {
					pictureFileDir.mkdirs();
				}
				Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
				pictureFile = new File(pictureFileDir, System.currentTimeMillis() + ".jpg");
				pictureUri = Uri.fromFile(pictureFile);
				// getImageByCamera.putExtra("camerasensortype", 2); // 调用前置摄像头
				getImageByCamera.putExtra("autofocus", true); // 自动对焦
				// getImageByCamera.putExtra("fullScreen", true); // 全屏
				// getImageByCamera.putExtra("showActionIcons", false);
				getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
				startActivityForResult(getImageByCamera, CAMERA_WITH_DATA);
			} else {
				Toast.makeText(context, "请插入SD卡", Toast.LENGTH_SHORT).show();
			}

		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, "系统错误,无法添加照片", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK && resultCode != PHOTO_SCAN && resultCode != CAMERA_WITH_DATA && resultCode != SCAN_QCODE)
			return;
		switch (requestCode) {

		case CAMERA_WITH_DATA:
			try {
				selectBitmap = ImageCompress.decodeStream(new FileInputStream(pictureFile), new FileInputStream(pictureFile), 600);
				if (android.os.Build.BRAND.equalsIgnoreCase("samsung")) {
					selectBitmap = BitmapFileUtils.rotateBitMap(selectBitmap);
				}
				String latlng = "";
				if (Constants.mLocation == null) {
					latlng = "定位未完成";
				} else {
					latlng = Constants.mLocation.getLatitude() + "," + Constants.mLocation.getLongitude();
				}
				PhotoUtils.convertBitmapToSmall(selectBitmap, latlng, pictureFile.getPath(), photoWordType);
				doTakePhotoCall(pictureFile.getPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// case PHOTO_SCAN:
		// Bundle bundle = data.getExtras();
		// int itemPosition = bundle.getInt("itemPosition");
		// int clickPosition = bundle.getInt("clickPosition");
		// deletePic(itemPosition, clickPosition);
		// Toast.makeText(AddSingleRecord.this, "图片已删除！", 1).show();
		// break;

		}
	}

	protected void doTakePhotoCall(String path) {

		Image image = new Image();
		image.path = path;
		imageLists.add(image);
		// mGridViewAdapter.setList(imageLists);
	}

	protected void showDeleteDialog(final int position) {
		AlertDialog.Builder builder = new Builder(context).setTitle("确定要删除该照片吗？")
				.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						showDeleteDialogOK(position);
					}
				}).setNegativeButton("取消", null);
		builder.create().show();
	}

	public abstract void showDeleteDialogOK(int position);
}
