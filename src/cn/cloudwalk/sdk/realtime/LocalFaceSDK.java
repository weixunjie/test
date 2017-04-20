/**
 * Project Name:cloudwalk2
 * File Name:CloudwalkLocalFaceSDK.java
 * Package Name:cn.cloudwalk
 * Date:2016-4-27涓嬪崍4:57:54
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd All Rights Reserved.
 */

package cn.cloudwalk.sdk.realtime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;
import cn.cloudwalk.sdk.FaceInfo;
import cn.cloudwalk.sdk.FaceInterface;
import cn.cloudwalk.sdk.FaceInterface.cw_img_angle_t;
import cn.cloudwalk.sdk.FaceInterface.cw_img_mirror_t;
import cn.cloudwalk.sdk.LocalSDK;

/**
 * ClassName: CloudwalkLocalFaceSDK <br/>
 * Description: <br/>
 * date: 2016-4-27 涓嬪崍4:57:54 <br/>
 * 
 * @author 284891377
 * @since JDK 1.7
 */
public class LocalFaceSDK {
	private static final int MAX_NUM = 10;

	private FaceInfoCallback faceInfoCallback;
	static LocalFaceSDK cloudwalkSDK;

	// FaceDetTrack faceDetTrack;
	// static FaceParam param = new FaceParam();

	// private int missFrameSet = 15;
	// private int missFrame;
	LocalSDK mLocalSDK;
	int faceOp = FaceInterface.cw_op_t.CW_OP_TRACK;

	public int getFaceOp() {
		return faceOp;
	}

	public void setFaceOp(int faceOp) {
		this.faceOp = faceOp;
	}

	Context mContext;
	/** 姣忓抚鍥惧儚浜鸿劯鏁伴噺 **/
	int faceNum;
	FaceInfo[] faceInfos;

	/**
	 * 鍗曚緥瀹炰緥鍖�
	 * 
	 * @param mContext
	 * @return CloudwalkLocalFaceSDK
	 */
	public static LocalFaceSDK getInstance(Context mContext) {

		if (null == cloudwalkSDK) {
			cloudwalkSDK = new LocalFaceSDK(mContext);

		}
		return cloudwalkSDK;
	}

	private LocalFaceSDK(Context mContext) {
		this.mContext = mContext;

		faceInfos = new FaceInfo[MAX_NUM];
		for (int i = 0; i < MAX_NUM; i++) {
			faceInfos[i] = new FaceInfo();
		}
	}

	public int cwInit() {
		int ret = 0;
		mLocalSDK = LocalSDK.getInstance(mContext);
		cwStart();

		return ret;
	}

	public int cwDestory() {

		faceInfoCallback = null;

		cwStop();
		return 0;

	}

	/**
	 * cwFaceInfoCallback:璁剧疆浜鸿劯淇℃伅鍥炴帀. <br/>
	 * 
	 * @param faceInfoCallback
	 * @author:284891377 Date: 2016骞�6鏈�16鏃� 涓嬪崍2:35:09
	 * @since JDK 1.7
	 */
	public void cwFaceInfoCallback(FaceInfoCallback faceInfoCallback) {
		this.faceInfoCallback = faceInfoCallback;
	}

	// 绾跨▼
	private Thread videoThread = null;
	private boolean bDetecting = false;
	private byte[] mFrame;
	private Object lockPreview = new Object(); // 瑙嗛棰勮涓�, 鎶撳浘涓庡鐞嗛棿鐨勫悓姝ュ璞�
	// 姣忓抚鍥惧儚
	int frameFormat;
	private int frameW;
	private int frameH;
	private int frameAngle;
	private int frameMirror;

	/**
	 * cwStart:寮�濮嬩汉鑴告娴� <br/>
	 * 
	 * @return
	 * @author:284891377 Date: 2016-4-22 涓嬪崍3:51:17
	 * @since JDK 1.7
	 */
	private int cwStart() {
		bDetecting = true;
		if (null == videoThread) {
			videoThread = new Thread(new VideoFrameRunnable());
			videoThread.start();
		} else {

		}

		return 0;

	}

	/**
	 * cwStart:鍋滄浜鸿劯妫�娴� <br/>
	 * 
	 * @return
	 * @author:284891377 Date: 2016-4-22 涓嬪崍3:51:17
	 * @since JDK 1.7
	 */
	private int cwStop() {
		bDetecting = false;
		if ((null != videoThread) && !bDetecting) {
			try {
				synchronized (lockPreview) {
					lockPreview.notify();
				}
				videoThread.join();
				videoThread = null;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return 0;

	}

	/**
	 * cwPushFrame:Push瑙嗛妗㈡暟鎹� <br/>
	 * 
	 * @param frameData
	 *            鏁版嵁甯�
	 * @param frameW
	 *            鍥剧墖瀹�
	 * @param frameH
	 *            鍥惧儚楂�
	 * @param frameFormat
	 *            鍥惧儚鏍煎紡 {@link cn.cloudwalk.FaceInterface.ImageForm}
	 * @param caremaType
	 *            鎽勫儚澶村拰灞忓箷鏂瑰悜 {@link cn.cloudwalk.FaceInterface.CaremaType}
	 * @author:284891377 Date: 2016-4-22 涓嬪崍4:03:24
	 * @since JDK 1.7
	 */
	public void cwPushFrame(byte[] frameData, int frameW, int frameH,
			int frameFormat, int caremaType) {
		this.frameW = frameW;
		this.frameH = frameH;
		this.frameFormat = frameFormat;


		this.frameAngle = cw_img_angle_t.CW_IMAGE_ANGLE_0;
		this.frameMirror = cw_img_mirror_t.CW_IMAGE_MIRROR_HOR;
		synchronized (lockPreview) {
			this.mFrame = frameData;
			lockPreview.notify();
		}

	}

	private Bitmap yuv2Img(byte[] paramArrayOfByte, int paramInt1,
			int paramInt2, int paramInt3, int paramInt4) {
		long l = System.currentTimeMillis();

		ByteArrayOutputStream localByteArrayOutputStream = null;

		Bitmap bitmap = null;
		try {
			YuvImage yuvImage = new YuvImage(paramArrayOfByte, paramInt1,
					paramInt2, paramInt3, null);

			if (paramArrayOfByte != null) {

				localByteArrayOutputStream = new ByteArrayOutputStream();

				yuvImage.compressToJpeg(new Rect(0, 0, paramInt2, paramInt3),
						paramInt4, localByteArrayOutputStream);

				bitmap = BitmapFactory.decodeByteArray(
						localByteArrayOutputStream.toByteArray(), 0,
						localByteArrayOutputStream.size());

				localByteArrayOutputStream.close();

			}
		} catch (Exception ex) {

			Log.e("yc_CloudwalkSDK", "yuv2Img异常:" + ex.getMessage());

		}
		// TestLog.netE("yc_CloudwalkSDK", "yuv2Img" +
		// (System.currentTimeMillis() - Long.valueOf(l).longValue()));
		return bitmap;
	}

	private Bitmap rotaingImageView(Bitmap paramBitmap) { // Log.e("2222",
															// "rotaingImageView"+this.caremaType
															// );

		if (paramBitmap == null) {
			return null;
		}
		// return paramBitmap;

		Matrix localMatrix = new Matrix();
		localMatrix.postScale(-1.0F, 1.0F);

		// localMatrix.postRotate(90F);

		Bitmap rn = Bitmap.createBitmap(paramBitmap, 0, 0,
				paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix,
				true);

		Log.e("2222", "rotaingImageView");
		return rn;

	}

	private static byte[] bitmapToByte(Bitmap paramBitmap,
			Bitmap.CompressFormat paramCompressFormat) {
		long l = System.currentTimeMillis();
		if (paramBitmap == null) {
			return null;
		}
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		paramBitmap.compress(paramCompressFormat, 80,
				localByteArrayOutputStream);
		byte[] rn = localByteArrayOutputStream.toByteArray();
		Log.e("2222", "bitmapToByte" + (System.currentTimeMillis() - l));
		return rn;
	}

	double getScale(int paramInt1, int paramInt2, int paramInt3,
			double paramDouble) {

	
		while(paramInt1 + paramInt2 * paramDouble > paramInt3)
		{
			paramDouble -= 0.1D;

		}

		return paramDouble;

	}
	


	public byte[] cwGetBestFace() {
		
		if (faceNum<=0)
		{
			return null;
		}

		// TestLog.netE("yc_CloudwalkSDK", "cwGetBestFace");
		long l = System.currentTimeMillis();
		Object localObject = null;
		byte[] rn = null;

		Bitmap localBitmap = rotaingImageView(yuv2Img(this.bestFaceFrame, 17,
				this.frameW, this.frameH, 80));

		int tmpX = 0;
		int tmpY = 0;
		if (this.bestFaceX - this.bestFaceWidth / 4 <= 0) {

		} else {
			tmpX = (int) (this.bestFaceX - this.bestFaceWidth / 4);
		}

		if (this.bestFaceY - this.bestFaceHeight / 2 <= 0) {

		} else {
			tmpY = (int) (this.bestFaceY - this.bestFaceHeight / 2);
		}

		double d1 = getScale(tmpX, this.bestFaceWidth, localBitmap.getWidth(),
				1.5D);
		double d2 = getScale(tmpY, this.bestFaceHeight,
				localBitmap.getHeight(), 2.0D);

		double picwith, piceheitgh;

		
		double tmpWidth,tmpHeigh;
		
		tmpWidth=bestFaceWidth * d1;
		
		tmpHeigh=bestFaceHeight * d2;
		
		/*picwith = tmpWidth >= localBitmap.getWidth() - tmpX ? localBitmap
				.getWidth() - tmpX
				: tmpWidth;

		piceheitgh = tmpHeigh>= localBitmap.getHeight() - tmpY ? localBitmap
				.getHeight() - tmpY
				: tmpHeigh;
		// Bitmap biMap = Bitmap.createBitmap(localBitmap, tmpX, tmpY, (int)
		// picwith, (int)piceheitgh);*/

	//	Log.e("333", tmpX + "-" + tmpY + "-" + (int) picwith + "-"
			//	+ (int) piceheitgh);

		Bitmap biMap = Bitmap.createBitmap(localBitmap, tmpX, tmpY,
				(int) tmpWidth, (int) tmpHeigh);

		// double d1 = getScale(bestFaceWidth, this.bestFaceWidth,
		// localBitmap.getWidth(), 1.5D);
		// double d2 = getScale(bestFaceHeight, this.bestFaceHeight,
		// localBitmap.getHeight(), 2.0D);
		// Bitmap biMap = Bitmap.createBitmap(localBitmap, bestFaceWidth,
		// bestFaceHeight, (int)(this.bestFaceWidth * d1),
		// (int)(this.bestFaceHeight * d2));
		return bitmapToByte(biMap, Bitmap.CompressFormat.JPEG);

		// Log.e("2222", "cwGetBestFace" + (System.currentTimeMillis() - l));
		// return bitmapToByte(localObject, Bitmap.CompressFormat.JPEG);
		// return rn;

	}

	// 瑙嗛甯у鐞嗙嚎绋�
	class VideoFrameRunnable implements Runnable {

		public void run() {
			android.os.Process
					.setThreadPriority(android.os.Process.THREAD_PRIORITY_LOWEST);
			while (bDetecting) {
				synchronized (lockPreview) {
					try {

						lockPreview.wait();

					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					processVideoFrame(mFrame);
				}
			}

			if (null != faceInfoCallback) {
				faceInfoCallback.detectFaceInfo(faceInfos, 0);
			}
			videoThread.interrupt();

			// int ret = cwReleaseDetector();

		}
	}

	public int cwFaceDetectTrack(byte[] data, // ???????
			int width, // ??
			int height, // ??
			int format, // ?????
			int angle, // 0 90 180 270
			int mirror) {
		int faceNum = 0;
		faceNum = mLocalSDK.cwFaceDetect(data, width, height, format, angle,
				mirror, faceOp, faceInfos);
		if (faceNum > 0 && faceNum < LocalSDK.ERRCODE_MIN) {

			faceInfoCallback.detectFaceInfo(faceInfos, faceNum);

		} else {
			faceInfoCallback.detectFaceInfo(null, 0);
			faceInfoCallback.detectFaceInfo(faceInfos, 0);

		}

		return faceNum;
	}

	/**
	 * 澶勭悊姣忎竴甯у浘鍍�
	 * 
	 * @param yuv_data
	 */
	private void processVideoFrame(byte[] yuv_data) {
		if (yuv_data == null || !bDetecting)
		{
			faceNum=0;
			
			return;
		}		
		
		faceNum = cwFaceDetectTrack(yuv_data, frameW, frameH, frameFormat,
				frameAngle, frameMirror);
		// Long startTime = System.currentTimeMillis();
		doBestFace();
	}

	private float bestFacScore = 0;
	private byte[] bestFaceFrame;
	float bestFaceX = 0;
	float bestFaceY = 0;
	int bestFaceWidth;
	int bestFaceHeight;

	private void doBestFace() {
		boolean isBest = true;
		// TestLog.netE("yc_CloudwalkSDK", "doBestFace:" +
		// faceInfos[0].mouthness + ";" + faceInfos[0].eyeLeft + ";" +
		// faceInfos[0].eyeRight + ";" + faceInfos[0].yaw + ";" +
		// faceInfos[0].pitch);
	
		if (this.faceNum > 0) {
			this.bestFacScore = faceInfos[0].keyptScore;
			this.bestFaceFrame = this.mFrame;
			this.bestFaceX = faceInfos[0].x;
			this.bestFaceY = faceInfos[0].y;
			this.bestFaceWidth = faceInfos[0].width;
			this.bestFaceHeight = faceInfos[0].height;
		}

	}

}
