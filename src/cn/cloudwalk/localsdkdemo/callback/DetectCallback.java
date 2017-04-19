package cn.cloudwalk.localsdkdemo.callback;

import android.graphics.Rect;

public interface DetectCallback {
	public void onDriverAnalyDetect(int result);
	public void onFaceDetect(Rect rect);

}
