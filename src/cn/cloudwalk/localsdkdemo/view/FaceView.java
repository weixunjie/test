package cn.cloudwalk.localsdkdemo.view;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import cn.cloudwalk.localsdkdemo.util.DisplayUtil;

import cn.cloudwalk.sdk.FaceInfo;

public class FaceView extends View {
	private static final String TAG = "FaceView";

	Context mContext;

	Paint mLinePaint, mTextPaint, mPointPaint;
	FaceInfo[] faceInfos;
	private int faceNum;
	int textSize = 22;
	private int surfaceW, surfaceH;
	double scale;
	int frameWidth = 480;
	int frameHight = 640;

	public void setSurfaceWH(int surfaceW, int surfaceH, int frameWidth, int frameHight) {
		this.surfaceW = surfaceW;
		this.surfaceH = surfaceH;
		this.frameWidth = frameWidth;
		this.frameHight = frameHight;
	    this.scale = (surfaceW * 1.0D / frameWidth);

	}

	/**
	 * setFaces:璁剧疆浜鸿劯妗嗗拰浜鸿劯鍏抽敭鐐�. <br/>
	 * 
	 * @author:284891377 Date: 2016-4-29 涓婂崍9:45:19
	 * @param faceInfos
	 * @param faceNum
	 * @since JDK 1.7
	 */
	public void setFaces(FaceInfo[] faceInfos, int faceNum) {
		this.faceInfos = faceInfos;
		this.faceNum = faceNum;

		invalidate();
	}

	public FaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		initPaint();

	}

	private void initPaint() {
		textSize = DisplayUtil.dip2px(mContext, 16);
		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// int color = Color.rgb(0, 150, 255);
		int color = Color.rgb(98, 212, 68);
		mLinePaint.setColor(color);
		mLinePaint.setStyle(Style.STROKE);
		mLinePaint.setStrokeWidth(5f);
		mLinePaint.setAlpha(180);

		mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPointPaint.setColor(color);
		mPointPaint.setStrokeWidth(10f);
		mPointPaint.setAlpha(180);

		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setColor(color);
		mTextPaint.setTextSize(textSize);
		mTextPaint.setAlpha(180);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		//if (faceInfos!=null)
		//{
		//Log.e("22", "faceInfos"+faceNum);
		//}
		if (faceInfos != null && faceNum>0 ) {

			//for (int i = 0; i < faceNum; i++) {

				FaceInfo faceInfo = faceInfos[0];
				// 浜鸿劯鍧愭爣杞崲

				int left = (int) (faceInfo.x * scale);
				int top = (int) (faceInfo.y * scale);

				int right = (int) ((faceInfo.width+faceInfo.x )* scale);
				int bottom = (int) ((faceInfo.height+faceInfo.y) * scale);
				//StringBuilder sb=new StringBuilder();
				//sb.append("trackId: " + faceInfo.trackId);

				//canvas.drawText(sb.toString(), left, top - textSize, mTextPaint);
				//canvas.drawPoint(left, top, mTextPaint);
				//canvas.drawPoint(right, right, mTextPaint);
				canvas.drawRect(new Rect(left, top, right, bottom), mLinePaint);

				
				  
			//}

		}
		else
		{
			//canvas.drawRect(new Rect(0, 0, 0, 0), mLinePaint);

		}
	}

}
