package com.vcrobot.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	private static final  String TAG = "FileUtil";
	private static final File parentPath = Environment.getExternalStorageDirectory();
	private static final String DST_FOLDER_NAME_REG = "vcr/face/reg";
	private static final String DST_FOLDER_NAME_LOGIN = "vcr/face/login";
	private static String storagePath = "";

	/**初始化保存路径
	 * @return
	 */
	private static String initPath(boolean login){
		if(storagePath.equals("")){
			if (login){
				storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME_LOGIN;
			}else{
				storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME_REG;
			}
			Log.i(TAG, "initPath: "+storagePath);
			File f = new File(storagePath);
			if(!f.exists()){
				f.mkdirs();
			}
		}
		return storagePath;
	}

	/**
	 * 保存Bitmap到sdcard
	 * @param b
	 * @param type
     * @return
     */
	public static String saveBitmap(Bitmap b,boolean type){
		String path = initPath(type);
		long dataTake = System.currentTimeMillis();
		String jpegName = path + "/" + dataTake +".jpg";
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveBitmap成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
		}
		return jpegName;
	}

	/**
	 * 递归创建文件夹、图片
	 * @param absPath
	 * @return 文件夹绝对路径
     */
	public static String createPath(String absPath){
		File tmp = new File(absPath);
		if (!tmp.exists()){
			tmp.mkdirs();
		}
		return absPath;
	}

	/**
	 * 保存Bitmap到sdcard
	 * @param b
	 * @return 图片绝对路径
	 */
	public static String saveBitmap(Bitmap b, String jpegName){
		File tmp = new File(jpegName);
		if (tmp.exists()){
			tmp.delete();
		}
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			//压缩50%
			b.compress(Bitmap.CompressFormat.JPEG, 50, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jpegName;
	}

	/**
	 * 压缩图片尺寸
	 * @param pathName
	 * @param targetWidth
	 * @param targetHeight
     * @return
     */
	public static Bitmap compressBySize(String pathName, int targetWidth,int targetHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
		// 得到图片的宽度、高度；
		float imgWidth = opts.outWidth;
		float imgHeight = opts.outHeight;
		// 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
		int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
		int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
		opts.inSampleSize = 1;
		if (widthRatio > 1 || heightRatio > 1) {
			if (widthRatio > heightRatio) {
				opts.inSampleSize = widthRatio;
			} else {
				opts.inSampleSize = heightRatio;
			}
		}
		//设置好缩放比例后，加载图片进内容；
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(pathName, opts);
		return bitmap;
	}
}
