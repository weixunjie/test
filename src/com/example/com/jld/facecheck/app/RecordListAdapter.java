package com.example.com.jld.facecheck.app;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.com.jld.facecheck.app.models.RecordInfo;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RecordListAdapter extends BaseAdapter {

	private ArrayList<RecordInfo> mRecordInfo = null;
	private Context mxcContext;

	public RecordListAdapter(Context context, ArrayList<RecordInfo> cursor) {
		mRecordInfo = cursor;
		mxcContext = context;

	}

	@Override
	public int getCount() {

		return mRecordInfo.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RecordItem listItem;
		RecordInfo info = mRecordInfo.get(position);
		if (null == convertView) {

			listItem = new RecordItem(mxcContext, info);
		} else {
			listItem = (RecordItem) convertView;
		}
		
		TextView tvItemRecDate = (TextView) listItem.findViewById(R.id.tvItemRecDate);

		Date tmpDate = new Date(Long.parseLong(info.getRec_date()));

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy年MM月dd日 HH:mm");
		
		tvItemRecDate.setText(simpleDateFormat.format(tmpDate));
		
		TextView tvItemBirthday = (TextView) listItem.findViewById(R.id.tvItemBirthday);
		tvItemBirthday.setText(info.getRec_birthday());

		TextView tvItemIdCad = (TextView) listItem.findViewById(R.id.tvItemIdCad);

		tvItemIdCad.setText(info.getRec_idcard());

		
		TextView tvItemMZ = (TextView) listItem.findViewById(R.id.tvItemMZ);

		tvItemMZ.setText(info.getRec_mz());
		

		TextView tvItemName = (TextView) listItem.findViewById(R.id.tvItemName);

		tvItemName.setText(info.getRec_name());
		
		TextView tvItemSex = (TextView) listItem.findViewById(R.id.tvItemSex);

		tvItemSex.setText(info.getRec_sex());
		
		return listItem;
	}
}