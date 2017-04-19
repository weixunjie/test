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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

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

	private TextView tb;
	private ImageView iv;
	private ImageView ivId;
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// USB设备拔出广播
			if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				UsbDevice device = intent
						.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				String deviceName = device.getDeviceName();
				if (device != null && device.equals(deviceName)) {
					Message msg = new Message();
					msg.what = 2;
					msg.obj = "USB设备拔出，应用程序即将关闭。";
					// MyHandler.sendMessage(msg);

				}

			} else if (common.ACTION_USB_PERMISSION.equals(action)) {// USB设备未授权，从SDTAPI中发出的广播
				Message msg = new Message();
				msg.what = 3;
				msg.obj = "USB设备无权限";
				// MyHandler.sendMessage(msg);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		screenWidth = localDisplayMetrics.widthPixels;
		screenHeight = localDisplayMetrics.heightPixels;

		common = new Common();
		mCaremaFragment = CaremaFragment.newInstance(screenWidth, screenHeight,
				R.layout.fragment_carema);
		getFragmentManager().beginTransaction()
				.replace(R.id.cameraPreview, mCaremaFragment).commit();

		IntentFilter filter = new IntentFilter();// ��ͼ������
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);// USB�豸�γ�
		filter.addAction(common.ACTION_USB_PERMISSION);// �Զ����USB�豸������Ȩ
		registerReceiver(mUsbReceiver, filter);
		findloop = true;

		try {
			sdta = new Sdtapi(this);

		} catch (Exception e1) {// 捕获异常，

			Log.e("LW", "Sdtapi" + e1.getCause());
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

		tb = (TextView) this.findViewById(R.id.textView1);
		iv = (ImageView) this.findViewById(R.id.ivView);
		ivId = (ImageView) this.findViewById(R.id.imgIdCard);
		readTread.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mUsbReceiver);
	}

	Thread readTread = new Thread() {
		public void run() {
			while (findloop) {
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
									+ cardmsg.nation_str + "族" + '\n' + "出生日期:"
									+ cardmsg.birth_year + "-"
									+ cardmsg.birth_month + "-"
									+ cardmsg.birth_day + '\n' + "住址:"
									+ cardmsg.address + '\n' + "身份证号码:"
									+ cardmsg.id_num + '\n' + "签发机关:"
									+ cardmsg.sign_office + '\n' + "有效期起始日期:"
									+ cardmsg.useful_s_date_year + "-"
									+ cardmsg.useful_s_date_month + "-"
									+ cardmsg.useful_s_date_day + '\n'
									+ "有效期截止日期:" + cardmsg.useful_e_date_year
									+ "-" + cardmsg.useful_e_date_month + "-"
									+ cardmsg.useful_e_date_day + '\n';
							
							
							byte[] bm = cardmsg.ptoto;
							WltDec dec = new WltDec();
							byte[] bip = dec.decodeToBitmap(bm);
							
							Message msg = new Message();
							msg.obj = bip;

							handlerUI.sendMessage(msg);

							

						} else {
							show = "读基本信息失败:" + String.format("0x%02x", ret);

							// findloop=false;

						}

					
					}// end if 找卡成功

				}// end while

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	};
	
	byte[] bip;
	byte[] byteFace;
	public Handler handlerUI = new Handler() {
		public void handleMessage(Message msg) {

			tb.setText(msg.obj.toString());

			 byteFace=mCaremaFragment.getRealTimeFaceByte();
			//bestFaceData = ();
			
			
			//AttributeBean b= mCaremaFragment.mCloudwalkSDK
				//.cwGetAttriFromImgData(bestFaceData);
			 
			//Log.e("123", ""+ i.alignedData.length);
			//Log.e("123",  ""+i.alignedH+"w"+i.height);
		
			 bip=(byte[] )msg.obj;
		
			
				Bitmap bp = BitmapFactory.decodeByteArray(bip, 0, bip.length);
				ivId.setImageBitmap(bmp(bp));
				    
		
			
			
			if(byteFace!=null)
			{
			Bitmap bt = BitmapFactory.decodeByteArray(byteFace, 0,
					byteFace.length);
			iv.setImageBitmap(bt);
			}
			
			faceCompare();
			

			// mCaremaFragment.mCloudwalkSDK.get
		}
	};

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_COMPARE_IMAGE:
				faceCompare();
				break;

			default:
				break;
			}
		}
	};

	public void faceCompare() {
		new FeatureAsynTask().execute(new String[0]);
	}

	private class FeatureAsynTask extends AsyncTask<String, Integer, Void> {
		private FeatureAsynTask() {
		}

		@Override
		protected Void doInBackground(String... arg0) {
			{
				long l = System.currentTimeMillis();
				Object localObject = mCaremaFragment.mCloudwalkSDK
						.cwGetAttriFromImgData(byteFace);

				
				FeatureBean idCardFeature = mCaremaFragment.mCloudwalkSDK
						.GetFeatureFromImgData(bip, true);
	

				if ((idCardFeature.ret == 0)
						&& (((FeatureBean) localObject).ret == 0)) {
					VerifyBean rVerifyBean = mCaremaFragment.mCloudwalkSDK
							.cwVerify(((FeatureBean) localObject).btFeature,
									idCardFeature.btFeature);
					Log.e("123", "浜鸿劯鐗瑰緛鑰楁椂:"
							+ (System.currentTimeMillis() - l));
					if (((VerifyBean) localObject).ret == 0) {

						Log.e("123", "sssscore:"
								+ ((VerifyBean) localObject).score);
					}
				}
				/*
				 * MainActivity.this.handler.sendMessage(paramVarArgs);
				 * label285: MainActivity.this.idcardPath =
				 * (MainActivity.this.publicFilePath + "/" +
				 * MainActivity.this.cardInfo.getName() +
				 * System.currentTimeMillis() + "idcard.png");
				 * MainActivity.this.facePath =
				 * (MainActivity.this.publicFilePath + "/" +
				 * MainActivity.this.cardInfo.getName() +
				 * System.currentTimeMillis() + "face.png");
				 * ImgUtil.saveJPGE_After(MainActivity.this.cardInfo.getPhoto(),
				 * MainActivity.this.idcardPath, 80);
				 * ImgUtil.saveJPGE_After(MainActivity.this.bestFace,
				 * MainActivity.this.facePath, 80); if (MainActivity.this.db ==
				 * null) { MainActivity.this.db =
				 * x.getDb(MainActivity.this.daoConfig); } paramVarArgs = new
				 * VerifyLog(); paramVarArgs.name =
				 * MainActivity.this.cardInfo.getName(); paramVarArgs.idNo =
				 * MainActivity.this.cardInfo.getNumber(); paramVarArgs.score =
				 * (MainActivity.this.score + ""); paramVarArgs.time =
				 * (System.currentTimeMillis() + ""); paramVarArgs.faceJpgPath =
				 * MainActivity.this.facePath; paramVarArgs.idJpgPath =
				 * MainActivity.this.idcardPath; try {
				 * MainActivity.this.db.saveBindingId(paramVarArgs);
				 * MainActivity.this.handler.sendEmptyMessage(16); return null;
				 * paramVarArgs.what = 4; continue; paramVarArgs.what = 5;
				 * MainActivity.this.handler.sendMessage(paramVarArgs); break
				 * label285; paramVarArgs.what = 5;
				 * MainActivity.this.handler.sendMessage(paramVarArgs);
				 * Log.e("123", "璇佷欢鐓ф瘮瀵归敊璇爜:" + MainActivity.this.score);
				 * break label285; paramVarArgs.what = 5;
				 * MainActivity.this.handler.sendMessage(paramVarArgs);
				 * Log.e("123", "璇佷欢鐓�/鏈�浣充汉鑴哥壒寰侀敊璇爜:" + localFeatureBean.ret +
				 * "/" + ((FeatureBean)localObject).ret); } catch (DbException
				 * paramVarArgs) { for (;;) { Log.e("123", "dbsave閿欒:" +
				 * paramVarArgs.getMessage()); paramVarArgs.printStackTrace(); }
				 * } } }
				 */

			}
			return null;
		}
	}

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