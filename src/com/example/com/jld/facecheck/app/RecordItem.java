package com.example.com.jld.facecheck.app;

import java.text.SimpleDateFormat;
import java.util.Date;

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
		
		
		
	
		
	}
}