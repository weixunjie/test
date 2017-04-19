package cn.cloudwalk.localsdkdemo.camera;

import java.io.File;

import android.os.Environment;

public class ConStant {

	public static String publicFilePath;
	// 模型路径
	public static String sLicencePath=Environment.getExternalStorageDirectory() + File.separator + "CWModels";
	// 授权码 
	public static String sLicence="MjIyNDE0bm9kZXZpY2Vjd2F1dGhvcml6Zf/l5ebl5+bk3+fg5efm5Of/5ubl4Obg5Yjm5uvl5ubrkeXm5uvl5uai6+Xm5uvl5uTm6+Xm5ufm9ebn5ec=";
	
	// 人脸检测最小最大人脸
	public static int faceMinSize=50, faceMaxSize=500;
}
