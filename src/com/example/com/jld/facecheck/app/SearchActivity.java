package com.example.com.jld.facecheck.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.com.jld.facecheck.app.models.RecordInfo;
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
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.LauncherActivity.ListItem;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.os.Build;

public class SearchActivity extends Activity {

	private TextView tvStartTime;
	private TextView tvEndTime;
	private EditText etRecName;

	private EditText etRecIdCard;

	private ListView listView;

	private Context mContext;

	private Calendar cal;
	private int year, month, day;

	private TextView tvSearchPopName;
	private TextView tvSearhPopAddress;

	private ImageView ivSearhPopIdCard;

	private ImageView ivSearhRealTime;

	private View mView;
	private ArrayList<RecordInfo> recordInfos = new ArrayList<RecordInfo>();

	AlertDialog.Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/* set it to be full screen */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.search_page);

		LayoutInflater inflater = getLayoutInflater();

		mView = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.searchpopup, null);

		builder = new AlertDialog.Builder(mContext);
		builder.setTitle(null).setView(mView).setNegativeButton("关闭", null);

		etRecName = (EditText) this.findViewById(R.id.etSearchName);

		etRecIdCard = (EditText) this.findViewById(R.id.etSearchIdCard);

		tvStartTime = (TextView) this.findViewById(R.id.tvStartDate);
		tvEndTime = (TextView) this.findViewById(R.id.tvEndDate);

		java.util.Date dt = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		tvStartTime.setText(sdf.format(dt) + " 00:00");

		tvEndTime.setText(sdf.format(dt) + " 23:59");

		Button btnBackButton = (Button) this.findViewById(R.id.btnSearchBack);
		btnBackButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(SearchActivity.this, MainActivity.class);
				startActivity(intent);

				finish();
			}
		});

		tvStartTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				OnDateSetListener listener = new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker arg0, int year, int month,
							int day) {
						tvStartTime.setText(year + "-" + (++month) + "-" + day
								+ " 00:00"); // 将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
					}
				};

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");

				try {
					Date date = sdf.parse(tvStartTime.getText().toString()+":00");

					DatePickerDialog dialog = new DatePickerDialog(
							SearchActivity.this, 0, listener,
							date.getYear() + 1900, date.getMonth(), date
									.getDate());// 后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
					dialog.show();

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO Auto-generated method stub

			}
		});
		;

		tvEndTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				OnDateSetListener listener = new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker arg0, int year, int month,
							int day) {
						tvEndTime.setText(year + "-" + (++month) + "-" + day
								+ " 23:59"); // 将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
					}
				};

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟

				try {
					Date date = sdf.parse(tvEndTime.getText().toString()+":59");

					DatePickerDialog dialog = new DatePickerDialog(
							SearchActivity.this, 0, listener,
							date.getYear() + 1900, date.getMonth(), date
									.getDate());// 后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
					dialog.show();

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// TODO Auto-generated method stub

			}
		});
		;

		listView = (ListView) findViewById(R.id.listView);

		Button btnSearchButton = (Button) findViewById(R.id.btnSearch);

		btnSearchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				RecordDbUtils recordDbUtils = new RecordDbUtils(mContext);

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟

				long starttime = 0;
				try {
					starttime = sdf.parse(tvStartTime.getText().toString()+":00")
							.getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long endtime = 0;

				try {
					endtime = sdf.parse(tvEndTime.getText().toString()+":59")
							.getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Cursor cursor = recordDbUtils.select(starttime, endtime,
						etRecIdCard.getText().toString(), etRecName.getText()
								.toString());

				List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();

				
				recordInfos.clear();
				if (cursor.moveToFirst()) {
					for (int i = 0; i < cursor.getCount(); i++) {

						RecordInfo info = new RecordInfo();
						Map<String, Object> listem = new HashMap<String, Object>();
						info.setRec_name(

							
								
						cursor.getString(
								cursor.getColumnIndex(RecordDbUtils.REC_NAME))
								.toString());

						info.setRec_birthday(cursor.getString(cursor
								.getColumnIndex(RecordDbUtils.REC_BRTH)));

						info.setRec_idcard(cursor.getString(cursor
								.getColumnIndex(RecordDbUtils.REC_IDCARD)));

						info.setRec_idcardimage(cursor.getBlob(cursor
								.getColumnIndex(RecordDbUtils.REC_IDCARDIMG)));

						info.setRec_realtimeimage(cursor.getBlob(cursor
								.getColumnIndex(RecordDbUtils.REC_REALTIMEIMG)));

						info.setRec_date(cursor.getString(cursor
								.getColumnIndex(RecordDbUtils.REC_DATE)));
						
						info.setRec_mz(cursor.getString(cursor
								.getColumnIndex(RecordDbUtils.REC_MZ)));
						
						info.setRec_sex(cursor.getString(cursor
								.getColumnIndex(RecordDbUtils.REC_SEX)));
						info.setRec_address(cursor.getString(cursor
								.getColumnIndex(RecordDbUtils.REC_ADDRESS)));
						recordInfos.add(info);
						cursor.moveToNext();
					}
				}

				// SimpleAdapter adapter = new SimpleAdapter(
				// mContext,
				// listems,
				// R.layout.item,
				// new String[] { RecordDbUtils.REC_NAME,
				// RecordDbUtils.REC_IDCARD, RecordDbUtils.REC_MZ },
				// new int[] { R.id.rec_name, R.id.rec_idcard, R.id.rec_mz });

				RecordListAdapter listViewAdpater = new RecordListAdapter(mContext,
						recordInfos);

				listView.setAdapter(listViewAdpater);
				// 条目点击事件
				listView.setOnItemClickListener(new ItemClickListener());

			}
		});

	}

	private final class ItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ListView listView = (ListView) parent;
			RecordInfo recordInfo= recordInfos
					.get(position);

			mView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.searchpopup, null);
			tvSearchPopName = (TextView) mView
					.findViewById(R.id.tvSearchPopName);

			tvSearhPopAddress = (TextView) mView
					.findViewById(R.id.tvSearhPopAddress);

			ivSearhPopIdCard = (ImageView) mView
					.findViewById(R.id.ivSearhPopIdCard);

			ivSearhRealTime = (ImageView) mView
					.findViewById(R.id.ivSearhRealTime);

			tvSearhPopAddress.setText(recordInfo.getRec_address());

			tvSearchPopName
					.setText(recordInfo.getRec_name());

			byte[] tmpBytes = recordInfo.getRec_idcardimage();

			Bitmap bitmap = BitmapFactory.decodeByteArray(tmpBytes, 0,
					tmpBytes.length);

			ivSearhPopIdCard.setImageBitmap(bitmap);

			tmpBytes = (byte[]) recordInfo.getRec_realtimeimage();

			bitmap = BitmapFactory
					.decodeByteArray(tmpBytes, 0, tmpBytes.length);

			ivSearhRealTime.setImageBitmap(bitmap);

			bitmap = null;

			bitmap = null;

			builder.setView(mView);

			builder.show();

			// Toast.makeText(getApplicationContext(), personid, 1).show();
		}
	}

}