package com.example.com.jld.facecheck.app;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.example.com.jld.facecheck.app.models.RecordInfo;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class StockListAdapter extends BaseAdapter {

	private ArrayList<RecordInfo> mRecordInfo = null;
	private Context mxcContext;

	public StockListAdapter(Context context, ArrayList<RecordInfo> cursor) {
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
		RecordInfo rInfo = mRecordInfo.get(position);
		if (null == convertView) {

			listItem = new RecordItem(mxcContext, rInfo);
		} else {
			listItem = (RecordItem) convertView;
		}
		return listItem;
	}
}