package com.example.com.jld.facecheck.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.com.jld.facecheck.app.models.RecordInfo;
import com.sam.sdticreader.WltDec;
import com.sdt.Common;
import com.sdt.Sdtapi;

import cn.cloudwalk.localsdkdemo.camera.CaremaFragment;
import cn.cloudwalk.localsdkdemo.util.FileUtil;
import cn.cloudwalk.localsdkdemo.util.ImgUtil;
import cn.cloudwalk.localsdkdemo.util.UnzipFromAssets;
import cn.cloudwalk.sdk.AttributeBean;
import cn.cloudwalk.sdk.FaceInfo;
import cn.cloudwalk.sdk.FeatureBean;
import cn.cloudwalk.sdk.VerifyBean;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

class IdCardMsg {
	public String name;
	public String sex;
	public String nation_str;

	public String birth_year;
	public String birth_month;
	public String birth_day;
	public String address;
	public String id_num;
	public String sign_office;

	public String useful_s_date_year;
	public String useful_s_date_month;
	public String useful_s_date_day;

	public String useful_e_date_year;
	public String useful_e_date_month;
	public String useful_e_date_day;
	public byte[] ptoto;

}

public class MainActivity extends Activity {

	CaremaFragment mCaremaFragment;
	int screenWidth;
	int screenHeight;

	byte[] bestFaceData = null;

	private Context mContext;
	byte[] IdCarImg = null;

	String TAG = "MainActive";
	static final int MSG_COMPARE_IMAGE = 1;

	//

	ImageView image;
	Common common; // common对象，存储一些需要的参数
	Thread t2;
	/* 民族列表 */
	String[] nation = { "汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮", "布依", "朝鲜",
			"满", "侗", "瑶", "白", "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳", "佤", "畲",
			"高山", "拉祜", "水", "东乡", "纳西", "景颇", "克尔克孜", "土", "达斡尔", "仫佬", "羌",
			"布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米", "塔吉克", "怒", "乌兹别克",
			"俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔", "独龙", "鄂伦春", "赫哲",
			"门巴", "珞巴", "基诺" };

	public boolean findloop = true;
	Sdtapi sdta;

	private TextView tvRn;
	private ImageView ivIdCard;
	private ImageView ivImageNow;
	
	private TextView tvName;
	private TextView tvIdCar;
	
	
	private TextView tvSex;
	private TextView tvBrithday;
	private TextView tvMz;

	private TextView tvAddress;


	/*
	 * private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
	 * 
	 * public void onReceive(Context context, Intent intent) { String action =
	 * intent.getAction();
	 * 
	 * // USB设备拔出广播 if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
	 * UsbDevice device = intent .getParcelableExtra(UsbManager.EXTRA_DEVICE);
	 * String deviceName = device.getDeviceName(); if (device != null &&
	 * device.equals(deviceName)) { Message msg = new Message(); msg.what = 2;
	 * msg.obj = "USB设备拔出，应用程序即将关闭。"; // MyHandler.sendMessage(msg);
	 * 
	 * }
	 * 
	 * } else if (common.ACTION_USB_PERMISSION.equals(action)) {//
	 * USB设备未授权，从SDTAPI中发出的广播 Message msg = new Message(); msg.what = 3; msg.obj
	 * = "USB设备无权限"; // MyHandler.sendMessage(msg); }
	 * 
	 * } };
	 */

	MediaPlayer mediaPlayer01;

	private void playVoice(int fileName) {

		//Log.e("pv", fileName);
		if (mediaPlayer01 != null) {
			mediaPlayer01.reset();
			mediaPlayer01.release();
		} 
			mediaPlayer01 = MediaPlayer.create(mContext,fileName );
		
		mediaPlayer01.start();

	}


	
	private RecordInfo currentReadInfo=new RecordInfo();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// IntentFilter filter = new IntentFilter();// ��ͼ������
		// filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);// USB�豸�γ�
		// filter.addAction(common.ACTION_USB_PERMISSION);// �Զ����USB�豸������Ȩ
		// registerReceiver(mUsbReceiver, filter);

		
		

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/* set it to be full screen */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);



		setContentView(R.layout.activity_main);

		
		SharedPreferences sharedPreferences = getSharedPreferences(
				"jdlfaceapp", Context.MODE_PRIVATE); // 私有数据

		// String isSave=sharedPreferences.getString(, "")

		Log.e("222", "on main created");
		mContext = this;

		String tmpString = sharedPreferences.getString(
				Constants.SPLicenseKeySPName, "");

		if (TextUtils.isEmpty(tmpString)) {
			Constants.currentKey = Constants.DefaultKey;

			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SettingActivity.class);
			startActivity(intent);

			finish();
			return;
		} else {
			String isUpload = sharedPreferences.getString(
					Constants.SPIsUploadToNetSPName, "0");

			if (!TextUtils.isEmpty(isUpload) && isUpload.equals("1")) {

				Constants.currentIsUploadData = true;
			} else {
				Constants.currentIsUploadData = false;
			}

			String strCompareVale = sharedPreferences.getString(
					Constants.SPCompareValueSPName, "0.7");
			Constants.currentKey = tmpString;

			Constants.currentCompareValue = 0.7;
			

			String tmp = sharedPreferences.getString(
					Constants.SPAdminPassword, "123");
			
			Constants.currentAdminPassword=tmp;
			
			tmp = sharedPreferences.getString(
					Constants.SPUploadUserCode, "adin");
			
			Constants.currentUploadUserKeyString=tmp;
			
			
			try {

				Constants.currentCompareValue = Double.valueOf(strCompareVale);
			} catch (Exception e) {
				// TODO: handle exception
			}

			Log.e("111", Constants.currentCompareValue + ","
					+ Constants.currentKey + ","
					+ Constants.currentIsUploadData);

		}

		this.loadFile();
		

		

		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		screenWidth = localDisplayMetrics.widthPixels;
		screenHeight = localDisplayMetrics.heightPixels;

		common = new Common();
		mCaremaFragment = CaremaFragment.newInstance(screenWidth, screenHeight,
				R.layout.fragment_carema);
		getFragmentManager().beginTransaction()
				.replace(R.id.cameraPreview, mCaremaFragment).commit();


	

		findloop = true;

		try {
			sdta = new Sdtapi(this);

		} catch (Exception e1) {// 捕获异常，

			Log.e("LW", "Sdtapi" + e1.getCause());

			try {

				Thread.sleep(3000);

				sdta = new Sdtapi(this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * if(e1.getCause()==null) //USB设备异常或无连接，应用程序即将关闭。 {
			 * 
			 * new Thread(){ public void run() { Message msg = new Message();
			 * msg.what=2; msg.obj = "USB设备异常或无连接，应用程序即将关闭。";
			 * //MyHandler.sendMessage(msg); } }.start(); } else
			 * //USB设备未授权，需要确认授权 {
			 * 
			 * setallunclick("USB设备未授权，弹出请求授权窗口后，请点击\"确定\"继续");
			 * 
			 * }
			 */

		}

		tvRn = (TextView) this.findViewById(R.id.tvRn);
		tvRn.setText(R.string.tip_idcard);
		ivIdCard = (ImageView) this.findViewById(R.id.ivIdCard);
		ivImageNow = (ImageView) this.findViewById(R.id.ivImageNow);
		
		tvName = (TextView) this.findViewById(R.id.tvName);
		
		tvIdCar=(TextView) this.findViewById(R.id.tvIdCard);
		
		
tvAddress=(TextView) this.findViewById(R.id.tvAdresss);
		
		
		tvMz=(TextView) this.findViewById(R.id.tvMz);
		
		
		tvBrithday=(TextView) this.findViewById(R.id.tvBirthday);
		
		
		
		tvSex=(TextView) this.findViewById(R.id.tvSex);
		
		


		 RecordInfo recordInfo=new RecordInfo();
		 recordInfo.setRec_address("33");
		 recordInfo.setRec_birthday("1203-222-3");
		 recordInfo.setRec_date(String.valueOf(System.currentTimeMillis()));

		 recordInfo.setRec_idcard("452243425344534522");
		 recordInfo.setRec_mz("岽组");
		 recordInfo.setRec_name("魏巽灯");
		 recordInfo.setRec_sex("女");
		 
		 recordInfo.setRec_address("方江要要有尖有硝尖肖323灌顶地水士大夫械2342332342343242");
		 
		 Resources res = getResources();
	  Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.logo_big);
		 
	  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
	    
	  recordInfo.setRec_idcardimage(baos.toByteArray());	 
	  recordInfo.setRec_realtimeimage(baos.toByteArray());
		 
		 RecordDbUtils
		 ss=new RecordDbUtils(mContext);
		
		 
		 ss.insert(recordInfo);
		
		 
		 ImageView imageView1=(ImageView)findViewById(R.id.imageView1);
		 imageView1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				inputTitleDialog();
			}
		});
		 
		//readTread.start();
	}

	
	private void inputTitleDialog() {

        final EditText inputServer = new EditText(this);
       
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入管理员密码:").setView(inputServer).setNegativeButton(
                "取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                       if (inputName.equals(Constants.currentAdminPassword))
                        {  
                        	Intent intent = new Intent();
                			intent.setClass(MainActivity.this, SearchActivity.class);
                			startActivity(intent);

                			finish();
                       }
                    }
                });
        builder.show();
    }
	
	
	private void fileOfater()
	{
		
	}
	public void loadFile() {

		final ProgressDialog dialog;
		// 需要对解压包是否已经存在进行判断
		File file = new File(

		Constants.sLicencePath);
		if (!file.exists()) {
			dialog = ProgressDialog.show(MainActivity.this, "",
					"正在努力加载数据，请稍等...", true);
			// 此处将assets文件夹下的CWModels.zip文件解压到了sd卡的根目录下面
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {

						String zipFilePath = Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ "CWModels.zip";
						if (!new File(zipFilePath).exists()) {
							assetsDataToSD(zipFilePath);// 从assets复制到根目录
						}

						UnzipFromAssets.UnZipFolder(
								Environment.getExternalStorageDirectory()
										+ File.separator + "CWModels.zip",
								Environment.getExternalStorageDirectory() + "");// 解压

						if (!new File(Constants.sLicencePath).exists()) {
							myHandler.sendEmptyMessage(1);
						}					
						
				
						
						
						myHandler.sendEmptyMessage(2);

						dialog.dismiss();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		else
		{
			fileOfater();
		}
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 1) {
				new AlertDialog.Builder(MainActivity.this)
						.setMessage("模型解压异常,请手工导入!")
						.setNegativeButton("确定",
								new AlertDialog.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										arg0.dismiss();
										finish();
									}
								}).show();
			}
			
			if (msg.what==2)
			{
				fileOfater();
			}

			super.handleMessage(msg);
		}
	};

	private void assetsDataToSD(String fileName) throws IOException {
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(fileName);
		myInput = this.getAssets().open("CWModels.zip");
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();
	}

	public class ThreadExtendsThread extends Thread {
		// static int count =10;
		public void run() {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// playVoice("s");
			// playVoice("pass.wav");
			uploadRec(currentReadInfo.getRec_name(), currentReadInfo.getRec_sex(), currentReadInfo.getRec_idcard(), currentReadInfo.getRec_birthday());
		}

	}

	private void uploadRec(String xm, String xb, String idNumber, String csny) {
		// 命名空间
		String nameSpace = "http://mobilewebservice.infomgr.xjpuhui.com";
		// 调用的方法名称
		String methodName = "saveOldVisitor";
		// EndPoint
		String endPoint = "http://124.117.209.131:38203/services/IServiceMrg";

		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date now = new Date();

		rpc.addProperty("xm", xm);

		rpc.addProperty("xb", xb);

		rpc.addProperty("mz", "汉");

		rpc.addProperty("csrq", csny);

		rpc.addProperty("zz", "");

		rpc.addProperty("sfz", idNumber);

		rpc.addProperty("qfjg", "");

		rpc.addProperty("photo", "");

		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
		rpc.addProperty("intime", myFmt2.format(now));

		rpc.addProperty("carid", "");
		rpc.addProperty("yxq_s", "");
		rpc.addProperty("yxq_e", "");

		rpc.addProperty("inout", "");

		rpc.addProperty("customTransferCode", Constants.currentUploadUserKeyString);

		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.bodyOut = rpc;

		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		try {
			// 调用WebService
			transport.call(null, envelope, null);
		} catch (Exception e) {

			Log.e("2222222", "upload date err:");

			Message msg = new Message();

			msg.obj = "";

			handlerResultUploadMsg.sendMessage(msg);
			e.printStackTrace();
			return;

		}

		// 获取返回的数据
		SoapObject object = (SoapObject) envelope.bodyIn;
		// 获取返回的结果
		String result = object.getProperty(0).toString();

		Message msg = new Message();

		msg.obj = result;

	
		handlerResultUploadMsg.sendMessage(msg);

		Log.e("222222", result);
		// 将WebService返回的结果显示在TextView中
		// resultView.setText(result);
	}

	public Handler handlerResultUploadMsg = new Handler() {
		public void handleMessage(Message msg) {

			String rn = msg.obj.toString();

			if (!TextUtils.isEmpty(rn)) {
				if (rn.indexOf("成功") >= 0) {
					tvRn.setText("比对成功,数据已上传.");
				} else {
					tvRn.setText("比对成功,数据上传失败.");
				}
			} else

			{
				tvRn.setText("比对成功,数据上传超时.");

			}

			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Resources res = getResources();

			Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.touxiang);

			ivIdCard.setImageBitmap(bmp);

			ivImageNow.setImageBitmap(bmp);
			
			setTvValues();
   
			

		}
	};

	private void setTvValues()
	{

		tvName.setText("");
		tvIdCar.setText("");
		
		tvName.setText("");
		tvIdCar.setText("");
		tvAddress.setText("");
		
		tvSex.setText("");
		tvBrithday.setText("");
		
		tvMz.setText("");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		/*
		 * if (mUsbReceiver!=null) { unregisterReceiver(mUsbReceiver); }
		 */
	}

	Thread readTread = new Thread() {
		public void run() {

			while (findloop) {
				try {
					int ret = sdta.SDT_StartFindIDCard();
					String show;
					if (ret == 0x9f)// 找卡成功
					{
						ret = sdta.SDT_SelectIDCard();
						if (ret == 0x90) // 选卡成功
						{
							IdCardMsg cardmsg = new IdCardMsg();

							ret = ReadBaseMsgToStr(cardmsg);

							if (ret == 0x90) {
								show = "姓名:" + cardmsg.name + '\n' + "性别:"
										+ cardmsg.sex + '\n' + "民族:"
										+ cardmsg.nation_str + "族" + '\n'
										+ "出生日期:" + cardmsg.birth_year + "-"
										+ cardmsg.birth_month + "-"
										+ cardmsg.birth_day + '\n' + "住址:"
										+ cardmsg.address + '\n' + "身份证号码:"
										+ cardmsg.id_num + '\n' + "签发机关:"
										+ cardmsg.sign_office + '\n'
										+ "有效期起始日期:"
										+ cardmsg.useful_s_date_year + "-"
										+ cardmsg.useful_s_date_month + "-"
										+ cardmsg.useful_s_date_day + '\n'
										+ "有效期截止日期:"
										+ cardmsg.useful_e_date_year + "-"
										+ cardmsg.useful_e_date_month + "-"
										+ cardmsg.useful_e_date_day + '\n';

								
								currentReadInfo=new RecordInfo();
								currentReadInfo.setRec_idcard( cardmsg.id_num);
								currentReadInfo.setRec_name(cardmsg.name);
								currentReadInfo.setRec_sex(cardmsg.sex);
								currentReadInfo.setRec_sex( cardmsg.birth_year + cardmsg.birth_month
										+ cardmsg.birth_day);
								
								currentReadInfo.setRec_address(cardmsg.address);
								
								currentReadInfo.setRec_mz(cardmsg.nation_str);
								
								
								
								byte[] bm = cardmsg.ptoto;
								WltDec dec = new WltDec();
								byte[] bip = dec.decodeToBitmap(bm);

								Message msg = new Message();
								msg.obj = bip;

								handlerReadIdCardDone.sendMessage(msg);

							} else {
								show = "读基本信息失败:"
										+ String.format("0x%02x", ret);

								// findloop=false;

							}

						}// end if 找卡成功

					}// end while

					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	};

	byte[] byteIdCard;
	byte[] byteFace;

	public Handler handlerReadIdCardDone = new Handler() {
		public void handleMessage(Message msg) {

			byteFace = mCaremaFragment.getRealTimeFaceByte();

			byteIdCard = (byte[]) msg.obj;

			Bitmap bp = BitmapFactory.decodeByteArray(byteIdCard, 0,
					byteIdCard.length);
			ivIdCard.setImageBitmap(bmp(bp));

			tvName.setText(currentReadInfo.getRec_name());
			tvIdCar.setText(currentReadInfo.getRec_idcard());
			tvAddress.setText(currentReadInfo.getRec_address());
			
			tvSex.setText(currentReadInfo.getRec_sex());
			tvBrithday.setText(currentReadInfo.getRec_birthday());
			
			tvMz.setText(currentReadInfo.getRec_mz());
			
			if (byteFace != null) {

				try {

					Log.e("ssss", "byteFace" + byteFace.length);
					Bitmap bt = BitmapFactory.decodeByteArray(byteFace, 0,
							byteFace.length);
					ivImageNow.setImageBitmap(bt);

					Log.e("ssss", "byteFac1e" + byteFace.length);
				} catch (Exception ex) {
					Log.e("ssss", "haha no face");
					playVoice(R.raw.noface);
					tvRn.setText("未检测到人脸,请重试!");
				}
			} else {

				playVoice(R.raw.noface);
				tvRn.setText("未检测到人脸,请重试!");
				return;
			}
			// playVoice("nopass.wav");
			faceCompare();

		}
	};

	public void faceCompare() {
		/*
		 * long l = System.currentTimeMillis();
		 * 
		 * Log.e("123", "faceFeature:1");
		 * 
		 * FeatureBean faceFeature = mCaremaFragment.mCloudwalkSDK
		 * .cwGetFeature(byteFace); Log.e("123", "faceFeature:2"); FeatureBean
		 * idCardFeature = mCaremaFragment.mCloudwalkSDK
		 * .cwGetFeature(byteIdCard); Log.e("123", "faceFeature:3");
		 * Log.e("123", "faceFeature:" + faceFeature.ret + ",idCardFeature" +
		 * idCardFeature.ret);
		 * 
		 * if (idCardFeature.ret == 0 && faceFeature.ret == 0) { VerifyBean
		 * rVerifyBean = mCaremaFragment.mCloudwalkSDK.cwVerify(
		 * faceFeature.btFeature, idCardFeature.btFeature);
		 * 
		 * if (rVerifyBean.ret == 0) {
		 * 
		 * Log.e("123", "sssscore:" + rVerifyBean.score);
		 * 
		 * if (rVerifyBean.score > Constants.currentCompareValue) {
		 * 
		 * if (Constants.currentIsUploadData) { tvRn.setText("比对成功,数据上传中"); new
		 * ThreadExtendsThread().start();
		 * 
		 * } else
		 * 
		 * { tvRn.setText("比对成功!");
		 * 
		 * emptyImgBox(); } byteFace = null; byteIdCard = null; return; } else {
		 * // return false; tvRn.setText("比对失败!"); emptyImgBox(); return; } } }
		 * 
		 * tvRn.setText("比对发生错误，请检查配置!");
		 * 
		 * emptyImgBox();
		 */
		long l = System.currentTimeMillis();

		Log.e("123", "faceFeature:1");

		FeatureBean faceFeature = null;

		faceFeature = mCaremaFragment.mCloudwalkSDK.GetFeatureFromImgData(
				byteFace, true);

		Log.e("123", "faceFeature:2");
		FeatureBean idCardFeature = null;

		idCardFeature = mCaremaFragment.mCloudwalkSDK.GetFeatureFromImgData(
				byteIdCard, false);

		Log.e("123", "faceFeature:3");
		Log.e("123", "faceFeature:" + faceFeature.ret + ",idCardFeature"
				+ idCardFeature.ret);

		if (idCardFeature.ret == 0 && faceFeature.ret == 0) {
			VerifyBean rVerifyBean = mCaremaFragment.mCloudwalkSDK.cwVerify(
					faceFeature.btFeature, idCardFeature.btFeature);

			if (rVerifyBean.ret == 0) {

				Log.e("123", "sssscore:" + rVerifyBean.score);

				if (rVerifyBean.score > Constants.currentCompareValue) {

					if (Constants.currentIsUploadData) {
						
						playVoice(R.raw.pass);
						tvRn.setText("比对成功,数据上传中...");
						new ThreadExtendsThread().start();

					} else

					{
						tvRn.setText("比对成功!");
						playVoice(R.raw.pass);
						emptyImgBox();
					}
			
					
					RecordDbUtils recordDbUtils=new RecordDbUtils(mContext);
					
			
					currentReadInfo.setRec_date(String.valueOf(System.currentTimeMillis()));
					
				
					currentReadInfo.setRec_idcardimage(byteIdCard);
				
					
					currentReadInfo.setRec_realtimeimage(byteFace);
			
					
					recordDbUtils.insert(currentReadInfo);
					
					byteFace = null;
					byteIdCard = null;
					
					
					return;
				} else {
					// return false;
					tvRn.setText("比对失败!");
					playVoice(R.raw.nopass);
					emptyImgBox();
					return;
				}
			}
		}

		tvRn.setText("比对发生错误，请检查配置!");
		playVoice(R.raw.nopass);
		emptyImgBox();

	}

	private void emptyImgBox() {
		Resources res = getResources();
		Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.touxiang);

		// ivIdCard.setImageBitmap(bmp);

		// ivImageNow.setImageBitmap(bmp);;
	}

	/*
	 * MainActivity.this.handler.sendMessage(paramVarArgs); label285:
	 * MainActivity.this.idcardPath = (MainActivity.this.publicFilePath + "/" +
	 * MainActivity.this.cardInfo.getName() + System.currentTimeMillis() +
	 * "idcard.png"); MainActivity.this.facePath =
	 * (MainActivity.this.publicFilePath + "/" +
	 * MainActivity.this.cardInfo.getName() + System.currentTimeMillis() +
	 * "face.png");
	 * ImgUtil.saveJPGE_After(MainActivity.this.cardInfo.getPhoto(),
	 * MainActivity.this.idcardPath, 80);
	 * ImgUtil.saveJPGE_After(MainActivity.this.bestFace,
	 * MainActivity.this.facePath, 80); if (MainActivity.this.db == null) {
	 * MainActivity.this.db = x.getDb(MainActivity.this.daoConfig); }
	 * paramVarArgs = new VerifyLog(); paramVarArgs.name =
	 * MainActivity.this.cardInfo.getName(); paramVarArgs.idNo =
	 * MainActivity.this.cardInfo.getNumber(); paramVarArgs.score =
	 * (MainActivity.this.score + ""); paramVarArgs.time =
	 * (System.currentTimeMillis() + ""); paramVarArgs.faceJpgPath =
	 * MainActivity.this.facePath; paramVarArgs.idJpgPath =
	 * MainActivity.this.idcardPath; try {
	 * MainActivity.this.db.saveBindingId(paramVarArgs);
	 * MainActivity.this.handler.sendEmptyMessage(16); return null;
	 * paramVarArgs.what = 4; continue; paramVarArgs.what = 5;
	 * MainActivity.this.handler.sendMessage(paramVarArgs); break label285;
	 * paramVarArgs.what = 5;
	 * MainActivity.this.handler.sendMessage(paramVarArgs); Log.e("123",
	 * "璇佷欢鐓ф瘮瀵归敊璇爜:" + MainActivity.this.score); break label285;
	 * paramVarArgs.what = 5;
	 * MainActivity.this.handler.sendMessage(paramVarArgs); Log.e("123",
	 * "璇佷欢鐓�/鏈�浣充汉鑴哥壒寰侀敊璇爜:" + localFeatureBean.ret + "/" +
	 * ((FeatureBean)localObject).ret); } catch (DbException paramVarArgs) { for
	 * (;;) { Log.e("123", "dbsave閿欒:" + paramVarArgs.getMessage());
	 * paramVarArgs.printStackTrace(); } } } }
	 */

	// 字节解码函数
	void DecodeByte(byte[] msg, char[] msg_str) throws Exception {
		byte[] newmsg = new byte[msg.length + 2];

		newmsg[0] = (byte) 0xff;
		newmsg[1] = (byte) 0xfe;

		for (int i = 0; i < msg.length; i++)
			newmsg[i + 2] = msg[i];

		String s = new String(newmsg, "UTF-16");
		for (int i = 0; i < s.toCharArray().length; i++)
			msg_str[i] = s.toCharArray()[i];

	}

	// 读取身份证中的文字信息（可阅读格式的）
	public int ReadBaseMsgToStr(IdCardMsg msg) {
		int ret;
		int[] puiCHMsgLen = new int[1];
		int[] puiPHMsgLen = new int[1];
		byte[] pucCHMsg = new byte[256];
		byte[] pucPHMsg = new byte[1024];
		// sdtapi中标准接口，输出字节格式的信息。
		ret = sdta
				.SDT_ReadBaseMsg(pucCHMsg, puiCHMsgLen, pucPHMsg, puiPHMsgLen);
		if (ret == 0x90) {
			try {
				char[] pucCHMsgStr = new char[128];
				DecodeByte(pucCHMsg, pucCHMsgStr);// 将读取的身份证中的信息字节，解码成可阅读的文字
				PareseItem(pucCHMsgStr, msg); // 将信息解析到msg中
				msg.ptoto = pucPHMsg;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return ret;

	}

	// 分段信息提取
	void PareseItem(char[] pucCHMsgStr, IdCardMsg msg) {
		msg.name = String.copyValueOf(pucCHMsgStr, 0, 15);
		String sex_code = String.copyValueOf(pucCHMsgStr, 15, 1);

		if (sex_code.equals("1"))
			msg.sex = "男";
		else if (sex_code.equals("2"))
			msg.sex = "女";
		else if (sex_code.equals("0"))
			msg.sex = "未知";
		else if (sex_code.equals("9"))
			msg.sex = "未说明";

		String nation_code = String.copyValueOf(pucCHMsgStr, 16, 2);
		msg.nation_str = nation[Integer.valueOf(nation_code) - 1];

		msg.birth_year = String.copyValueOf(pucCHMsgStr, 18, 4);
		msg.birth_month = String.copyValueOf(pucCHMsgStr, 22, 2);
		msg.birth_day = String.copyValueOf(pucCHMsgStr, 24, 2);
		msg.address = String.copyValueOf(pucCHMsgStr, 26, 35);
		msg.id_num = String.copyValueOf(pucCHMsgStr, 61, 18);
		msg.sign_office = String.copyValueOf(pucCHMsgStr, 79, 15);

		msg.useful_s_date_year = String.copyValueOf(pucCHMsgStr, 94, 4);
		msg.useful_s_date_month = String.copyValueOf(pucCHMsgStr, 98, 2);
		msg.useful_s_date_day = String.copyValueOf(pucCHMsgStr, 100, 2);

		msg.useful_e_date_year = String.copyValueOf(pucCHMsgStr, 102, 4);
		msg.useful_e_date_month = String.copyValueOf(pucCHMsgStr, 106, 2);
		msg.useful_e_date_day = String.copyValueOf(pucCHMsgStr, 108, 2);

	}

	private Bitmap bmp(Bitmap bp) {
		float scaleWidth = 1;
		float scaleHeight = 1;
		int bmpWidth = bp.getWidth();
		int bmpHeight = bp.getHeight();
		/* 设置图片放大的比例 */
		double scale = 2;
		/* 计算这次要放大的比例 */
		scaleWidth = (float) (scaleWidth * scale);
		scaleHeight = (float) (scaleHeight * scale);

		/* 产生reSize后的Bitmap对象 */
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizeBmp = Bitmap.createBitmap(bp, 0, 0, bmpWidth, bmpHeight,
				matrix, true);
		return resizeBmp;
	}
}