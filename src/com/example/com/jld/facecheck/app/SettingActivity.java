package com.example.com.jld.facecheck.app;

import com.sam.sdticreader.WltDec;
import com.sdt.Common;
import com.sdt.Sdtapi;

import cn.cloudwalk.localsdkdemo.camera.CaremaFragment;
import cn.cloudwalk.localsdkdemo.util.ImgUtil;
import cn.cloudwalk.sdk.AttributeBean;
import cn.cloudwalk.sdk.FaceInfo;
import cn.cloudwalk.sdk.FeatureBean;
import cn.cloudwalk.sdk.VerifyBean;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class SettingActivity extends Activity {

	private EditText etSettingLicence;
	private CheckBox cbetSettingIsUpload;
	private EditText etSettingCompareValue;
	
	private SharedPreferences sharedPreferences = getSharedPreferences(
			"jdlfaceapp", Context.MODE_PRIVATE); // 私有数据
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting);

		
		etSettingLicence = (EditText) this.findViewById(R.id.etSettingLicence);
		
		
		cbetSettingIsUpload = (CheckBox) this.findViewById(R.id.cbetSettingIsUpload);
		
		
		 etSettingCompareValue = (EditText) this.findViewById(R.id.etSettingCompareValue);
		
		


		String isUpload = sharedPreferences.getString(Constants.IsUploadToNetSPName, "0");
		
		if (!TextUtils.isEmpty(isUpload)&& isUpload.equals("1"))
		{
			
			cbetSettingIsUpload.setChecked(true);
		}
		else
		{
			cbetSettingIsUpload.setChecked(false);
		}

		
		String strCompareVale = sharedPreferences.getString(Constants.CompareValueSPName, "0.7");
		etSettingCompareValue.setText(strCompareVale);
		
		
		String key = sharedPreferences.getString(Constants.LicenseKeySPName, "NDkxNzExNTZhNmY2OGM3NDk5MDYyYWRjYjU0OWVlZTZmOTI5Yzdjd2F1dGhvcml6Zd7n4ubk5+fi3+fg5efm5Of+5+bk4Obg5Yjm5uvl5ubrkeXm5uvl5uai6+Xm5uvl5uTm6+Xm5uDm6uvn6+fr5+DV5+vn6+fr593n5+bm5uQ=");
		etSettingLicence.setText(key);
		
		Button btnSettingSave = (Button) this.findViewById(R.id.btnSettingSave);
		btnSettingSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String strCbCheck=cbetSettingIsUpload.isChecked()?"1":"0";
				
				Editor editor = sharedPreferences.edit();
				editor.putString(Constants.IsUploadToNetSPName, strCbCheck);
				
				
				editor.putString(Constants.CompareValueSPName, etSettingCompareValue.getText().toString());
				
				
				editor.putString(Constants.LicenseKeySPName, etSettingCompareValue.getText().toString());
				
				
				editor.commit();
				
			}
		});

	}
}