package cn.cloudwalk.localsdkdemo.camera;

import com.example.com.jld.facecheck.app.Constants;
import com.example.com.jld.facecheck.app.MainActivity;

import com.example.com.jld.facecheck.app.R;

import cn.cloudwalk.localsdkdemo.util.PreferencesUtils;
import cn.cloudwalk.localsdkdemo.view.FaceView;
import cn.cloudwalk.sdk.FaceInfo;
import cn.cloudwalk.sdk.LocalSDK;
import cn.cloudwalk.sdk.realtime.FaceInfoCallback;
import cn.cloudwalk.sdk.realtime.LocalFaceSDK;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class CaremaFragment extends Fragment implements FaceInfoCallback,
		Delegate {
	static int screenH;
	static int screenW;
	static int layoutId;
	MainActivity activity;
	LocalFaceSDK mLFaceSdk;

	public LocalSDK mCloudwalkSDK;
	cn.cloudwalk.sdk.FaceInfo[] faceInfos;
	int faceNum;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {

				mFaceView.setFaces(
						faceInfos,
						faceNum);

			}
		}
	};

	int initRet;
	LayoutInflater lf;
	FaceView mFaceView;
	CameraPreview mPreview;
	View root;

	private void initCloudwalkFaceSDK() {
		this.mLFaceSdk = LocalFaceSDK.getInstance(this.activity);
		this.mLFaceSdk.cwFaceInfoCallback(this);

		mCloudwalkSDK = LocalSDK.getInstance(this.activity);

		// 鍒涘缓鍙ユ焺锛屽彞鏌勫彧闇�瑕佸垱寤轰竴娆�
		mCloudwalkSDK.cwCreateHandles(Constants.currentKey,
				Constants.faceMinSize, Constants.faceMaxSize,
				Constants.sLicencePath);

		this.initRet = this.mLFaceSdk.cwInit();
		Log.e("123", "initCloudwalkFaceSDK ret=" + Constants.currentKey + ","
				+ this.initRet);

	}

	private void initView() {
		int i = getResources().getConfiguration().orientation;
		this.mPreview = ((CameraPreview) this.root.findViewById(R.id.fccpv));
		this.mPreview.setDelegate(this);
	//	this.mPreview.setScreenOrientation(i);
		this.mPreview.setCaremaId(0);
		this.mFaceView = ((FaceView) this.root.findViewById(R.id.fcfv));
		i = screenH;
		int j = screenH * 640 / 480;
		FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(
				j, i);
		this.mPreview.setLayoutParams(localLayoutParams);
		this.mFaceView.setLayoutParams(localLayoutParams);
		this.mFaceView.setSurfaceWH(j, i, 640, 480);
	}

	public static CaremaFragment newInstance(int paramInt1, int paramInt2,
			int la) {
		screenW = paramInt1;
		screenH = paramInt2;
		layoutId = la;
		return new CaremaFragment();
	}

	public FaceInfo getRealTimeFace() {
		if (this.faceNum != 0) {
			return this.faceInfos[this.faceNum - 1];
		}
		return null;
	}

	public byte[] getRealTimeFaceByte() {
		if (faceNum <= 0) {
			return null;
		}
		// TestLog.netE("yc_CloudwalkSDK", "cwGetRealtimeFace");
		return this.mLFaceSdk.cwGetRealTimeFace();
	}

	@Deprecated
	public void onAttach(Activity paramActivity) {
		this.activity = ((MainActivity) paramActivity);
		super.onAttach(paramActivity);
	}

	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		this.lf = paramLayoutInflater;
		this.root = this.lf.inflate(layoutId, null);
		return this.root;
	}

	public void onDestroy() {
		super.onDestroy();
	}

	public void onFocus(float paramFloat1, float paramFloat2) {
	}

	public void onFocused() {
	}

	public void onOpenCameraError() {
	}

	public void onResume() {
		initCloudwalkFaceSDK();
		this.mPreview.cwStartCamera();
		super.onResume();
	}

	public void onStop() {
		this.mPreview.cwStopCamera();
		this.mLFaceSdk.cwDestory();
		super.onStop();
	}

	public void onViewCreated(View paramView, Bundle paramBundle) {
		initView();
		super.onViewCreated(paramView, paramBundle);
	}

	public void setNoFace() {
		this.faceNum = 0;
		this.mFaceView.setFaces(this.faceInfos, this.faceNum);
	}

	@Override
	public void detectFaceInfo(FaceInfo[] faceInfos, int faceNum) {
		// TODO Auto-generated method stub

		if (faceNum > 0) {
			this.faceInfos = faceInfos;
			this.faceNum = faceNum;
			if (faceNum > 8) {
				Log.e("123", "大于8faceNum=" + faceNum);
			}
		} else {
			this.faceInfos = null;
			this.faceNum = 0;
		}

		this.handler.sendEmptyMessage(1);

	}
}

/*
 * Location: C:\Users\Administrator\Downloads\AndroidKiller_1.3
 * .1_XiaZaiBa\bin\dex2jar\classes-dex2jar.jar
 * 
 * Qualified Name: cn.cloudwalk.cwkepa.CaremaFragment
 * 
 * JD-Core Version: 0.7.0.1
 */