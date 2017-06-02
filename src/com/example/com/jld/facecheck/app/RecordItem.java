package com.example.com.jld.facecheck.app;

import com.example.com.jld.facecheck.app.models.RecordInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecordItem extends LinearLayout {
	public RecordItem(Context context, RecordInfo info) {
		super(context);

		LayoutInflater factory = LayoutInflater.from(context);
		factory.inflate(R.layout.item, this);
		TextView codeTextView = (TextView) findViewById(R.id.stock_code);

		codeTextView.setText(info.getRec_name());
		TextView symbolTextView = (TextView) findViewById(R.id.stock_symbol);
		symbolTextView.setText(info.getRec_name());

	}
}