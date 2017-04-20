package cn.cloudwalk.localsdkdemo.camera;

import java.io.File;

import android.os.Environment;

public class ConStant {

	public static String publicFilePath;
	// 妯″瀷璺緞
	public static String sLicencePath=Environment.getExternalStorageDirectory() + File.separator + "CWModels";
	// 鎺堟潈鐮� 

	// 浜鸿劯妫�娴嬫渶灏忔渶澶т汉鑴�
	public static int faceMinSize=50, faceMaxSize=500;
}
