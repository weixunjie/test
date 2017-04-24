package com.example.com.jld.facecheck.app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
	public static final String TAG = "BroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("BroadcastReceiver", "boot over start service");
		Intent sayHelloIntent=new  Intent(context, MainActivity.class);
		sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(sayHelloIntent);
	}
}
