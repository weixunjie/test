package com.example.com.jld.facecheck.app;

import java.io.File;

import android.os.Environment;



public class Constants  {

	
	public static final int PREVIEW_W = 640;
	public static final int PREVIEW_H = 480;

	public static String publicFilePath;
	// 妯″瀷璺緞
	public static String sLicencePath=Environment.getExternalStorageDirectory() + File.separator + "CWModels";
	// 鎺堟潈鐮� 

	
	public static String fileePath=Environment.getExternalStorageDirectory() + File.separator + "JLDFILE"+ File.separator;
	// 鎺堟潈鐮� 
	
	// 浜鸿劯妫�娴嬫渶灏忔渶澶т汉鑴�
	public static int faceMinSize=20, faceMaxSize=800;
	
	
	public static String LicenseKeySPName="license";
	
	public static String CompareValueSPName="compareValue";
	
	public static String IsUploadToNetSPName="isUploadNet";
	
//	public static String DefaultKey="NDkxNzExNTZhNmY2OGM3NDk5MDYyYWRjYjU0OWVlZTZmOTI5Yzdjd2F1dGhvcml6Zd7n4ubk5+fi3+fg5efm5Of+5+bk4Obg5Yjm5uvl5ubrkeXm5uvl5uai6+Xm5uvl5uTm6+Xm5uDm6uvn6+fr5+DV5+vn6+fr593n5+bm5uQ=";

	
	
	public static String DefaultKey ="NDMxNjE1YzRhMTQwM2M0OTY0OTZkNzI4MThiNjU1MzAxNmRiYzFjd2F1dGhvcml6Zd7n5Obi5+fi3+fg5efm5Of+5+bi4Obg5Yjm5uvl5ubrkeXm5uvl5uai6+Xm5uvl5uTm6+Xm5uDm1efr5+vn6+er4Ofr5+vn66vn5+fm5ubl";  //jld
		
	public static String currentKey="";
	public static double currentCompareValue=0.7;
	
	public static boolean currentIsUploadData=true;
}