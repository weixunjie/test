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
import android.view.Window;
import android.view.WindowManager;
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

	private EditText etSettingUploadCode;


	private EditText etSettingAmindPwd;

	private SharedPreferences sharedPreferences = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/* set it to be full screen */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_setting);

		sharedPreferences = getSharedPreferences("jdlfaceapp",
				Context.MODE_PRIVATE); // 私有数据

		etSettingLicence = (EditText) this.findViewById(R.id.etSettingLicence);

		etSettingAmindPwd = (EditText) this
				.findViewById(R.id.etSettingAdminPwd);

		etSettingUploadCode = (EditText) this
				.findViewById(R.id.etSettingUPloadCode);

		etSettingAmindPwd = (EditText) this
				.findViewById(R.id.etSettingAdminPwd);

		cbetSettingIsUpload = (CheckBox) this
				.findViewById(R.id.cbetSettingIsUpload);

		etSettingCompareValue = (EditText) this
				.findViewById(R.id.etSettingCompareValue);

		String isUpload = sharedPreferences.getString(
				Constants.SPIsUploadToNetSPName, "0");

		if (!TextUtils.isEmpty(isUpload) && isUpload.equals("1")) {

			cbetSettingIsUpload.setChecked(true);
		} else {
			cbetSettingIsUpload.setChecked(false);
		}

		String strCompareVale = sharedPreferences.getString(
				Constants.SPCompareValueSPName, "0.7");
		etSettingCompareValue.setText(strCompareVale);

		String tmp = sharedPreferences.getString(Constants.SPUploadUserCode,
				"SEQ65290120170402278742");
		etSettingUploadCode.setText(tmp);
		
		
		 tmp = sharedPreferences.getString(Constants.SPAdminPassword,
				"123");
		etSettingAmindPwd.setText(tmp);
		
		

		String key = sharedPreferences.getString(Constants.SPLicenseKeySPName,
				Constants.DefaultKey);
		etSettingLicence.setText(key);

		Button btnSettingSave = (Button) this.findViewById(R.id.btnSettingSave);
		btnSettingSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String strCbCheck = cbetSettingIsUpload.isChecked() ? "1" : "0";

				Editor editor = sharedPreferences.edit();
				editor.putString(Constants.SPIsUploadToNetSPName, strCbCheck);

				editor.putString(Constants.SPCompareValueSPName,
						etSettingCompareValue.getText().toString());

				editor.putString(Constants.SPLicenseKeySPName, etSettingLicence
						.getText().toString());
				
				

				editor.putString(Constants.SPUploadUserCode, etSettingUploadCode
						.getText().toString());

				

				editor.putString(Constants.SPAdminPassword, etSettingAmindPwd
						.getText().toString());

				

				Constants.currentKey = etSettingLicence.getText().toString();
				
				Constants.currentAdminPassword = etSettingAmindPwd.getText().toString();
				
				Constants.currentUploadUserKeyString = etSettingUploadCode.getText().toString();

				editor.commit();

				Intent intent = new Intent();
				intent.setClass(SettingActivity.this, MainActivity.class);
				startActivity(intent);

				finish();

			}
		});

	}
}