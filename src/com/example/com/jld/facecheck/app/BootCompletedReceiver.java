package com.example.com.jld.facecheck.app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
	public static final String TAG = "BroadcastReceiver";

	@Override
	public void onReceive(final Context context, Intent intent) {
		
		new Handler().postDelayed(new Runnable() {  
		      
		    @Override  
		    public void run() {  
		    	Log.i("BroadcastReceiver", "boot over start service");
				Intent sayHelloIntent=new  Intent(context, MainActivity.class);
				sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(sayHelloIntent);  
		    }  
		}, 3000); 
		
	}
}
