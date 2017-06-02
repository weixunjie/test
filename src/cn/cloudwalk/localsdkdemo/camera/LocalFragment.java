package cn.cloudwalk.localsdkdemo.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.cloudwalk.localsdkdemo.view.LocalFaceView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.hardware.Camera.PreviewCallback;
import android.media.FaceDetector;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class LocalFragment implements Callback {

	protected Camera mCameraDevice = null;// 摄像头对象实例
	private long mScanBeginTime = 0; // 扫描开始时间
	private long mScanEndTime = 0; // 扫描结束时间
	private long mSpecPreviewTime = 0; // 扫描持续时间
	private int orientionOfCamera; // 前置摄像头layout角度
	int numberOfFaceDetected; // 最终识别人脸数目

	long mSpecCameraTime = 0;
	long mSpecStopTime = 0;

	private String TAG = "s";

	private SurfaceHolder hodler;
	
	private LocalFaceView faiFaceView;

	public void initView(SurfaceHolder yy,LocalFaceView ss) {

		hodler = yy;
		hodler.addCallback(this);
		faiFaceView=ss;

	}

	public void startFaceDetection() {
		try {
			mCameraDevice = Camera.open(1); // 打开前置

			if (mCameraDevice != null)
				Log.i(TAG, "open cameradevice success! ");
		} catch (Exception e) { // Exception代替很多具体的异常
			mCameraDevice = null;
			Log.w(TAG, "open cameraFail");
			// mHandler.postDelayed(r, 5000); // 如果摄像头被占用，人眼识别每5秒检测看有没有释放前置
			return;
		}

		Log.i(TAG, "startFaceDetection");
		Camera.Parameters parameters = mCameraDevice.getParameters();
		setCameraDisplayOrientation(1, mCameraDevice); // 设置预览方向

		mCameraDevice.setPreviewCallback(new PreviewCallback() {
			public void onPreviewFrame(byte[] data, Camera camera) {

				mScanEndTime = System.currentTimeMillis(); // 记录摄像头返回数据的时间
				mSpecPreviewTime = mScanEndTime - mScanBeginTime; // 从onPreviewFrame获取摄像头数据的时间
				Log.i(TAG,
						"onPreviewFrame and mSpecPreviewTime = "
								+ String.valueOf(mSpecPreviewTime));
				Camera.Size localSize = camera.getParameters().getPreviewSize(); // 获得预览分辨率
				YuvImage localYuvImage = new YuvImage(data, 17,
						localSize.width, localSize.height, null);
				ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
				localYuvImage.compressToJpeg(new Rect(0, 0, localSize.width,
						localSize.height), 80, localByteArrayOutputStream); // 把摄像头回调数据转成YUV，再按图像尺寸压缩成JPEG，从输出流中转成数组
				byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
				// CameraRelease(); // 及早释放camera资源，避免影响camera设备的正常调用
				StoreByteImage(arrayOfByte);
			}
		});

		try {
			mCameraDevice.setPreviewDisplay(hodler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mCameraDevice.startPreview(); // 该语句可放在回调后面，当执行到这里，调用前面的setPreviewCallback
		mScanBeginTime = System.currentTimeMillis();// 记录下系统开始扫描的时间
	}

	public void setCameraDisplayOrientation(int paramInt, Camera paramCamera) {
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(paramInt, info);

		int degrees = 0;

		orientionOfCamera = info.orientation; // 获得摄像头的安装旋转角度
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		paramCamera.setDisplayOrientation(result); // 注意前后置的处理，前置是映象画面，该段是SDK文档的标准DEMO
	}

	public void StoreByteImage(byte[] paramArrayOfByte) {
		mSpecStopTime = System.currentTimeMillis();
		mSpecCameraTime = mSpecStopTime - mScanBeginTime;

		Log.i(TAG,
				"StoreByteImage and mSpecCameraTime is "
						+ String.valueOf(mSpecCameraTime));

		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		Bitmap localBitmap1 = BitmapFactory.decodeByteArray(paramArrayOfByte,
				0, paramArrayOfByte.length, localOptions);
		int i = localBitmap1.getWidth();
		int j = localBitmap1.getHeight(); // 从上步解出的JPEG数组中接出BMP，即RAW->JPEG->BMP
		Matrix localMatrix = new Matrix();
		// int k = cameraResOr;
		Bitmap localBitmap2 = null;
		FaceDetector localFaceDetector = null;

		switch (orientionOfCamera) { // 根据前置安装旋转的角度来重新构造BMP
		case 0:
			localFaceDetector = new FaceDetector(i, j, 1);
			localMatrix.postRotate(0.0F, i / 2, j / 2);
			localBitmap2 = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
			break;
		case 90:
			localFaceDetector = new FaceDetector(j, i, 1); // 长宽互换
			localMatrix.postRotate(-270.0F, j / 2, i / 2); // 正90度的话就反方向转270度，一样效果
			localBitmap2 = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
			break;
		case 180:
			localFaceDetector = new FaceDetector(i, j, 1);
			localMatrix.postRotate(-180.0F, i / 2, j / 2);
			localBitmap2 = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
			break;
		case 270:
			localFaceDetector = new FaceDetector(j, i, 1);
			localMatrix.postRotate(-90.0F, j / 2, i / 2);
			localBitmap2 = Bitmap.createBitmap(j, i, Bitmap.Config.RGB_565); // localBitmap2应是没有数据的
			break;
		}

		FaceDetector.Face[] arrayOfFace = new FaceDetector.Face[1];
		Paint localPaint1 = new Paint();
		Paint localPaint2 = new Paint();
		localPaint1.setDither(true);
		localPaint2.setColor(-65536);
		localPaint2.setStyle(Paint.Style.STROKE);
		localPaint2.setStrokeWidth(2.0F);
		Canvas localCanvas = new Canvas();
		localCanvas.setBitmap(localBitmap2);
		localCanvas.setMatrix(localMatrix);
		localCanvas.drawBitmap(localBitmap1, 0.0F, 0.0F, localPaint1); // 该处将localBitmap1和localBitmap2关联（可不要？）

		numberOfFaceDetected = localFaceDetector.findFaces(localBitmap2,
				arrayOfFace); // 返回识脸的结果
		localBitmap2.recycle();
		localBitmap1.recycle(); // 释放位图资源

		if (arrayOfFace.length >= 1) {
		
			Message msgMessage = new Message();

			msgMessage.obj = arrayOfFace[0];

			myHandler.sendMessage(msgMessage);
		}
		else
			
		{
			Message msgMessage = new Message();

			msgMessage.obj = null;

			myHandler.sendMessage(msgMessage);
		}
		// FaceDetectDeal(numberOfFaceDetected);
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			{
		

				android.media.FaceDetector.Face face = (android.media.FaceDetector.Face) msg.obj;
			
				faiFaceView.setFaces(face,1);
				
			}
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

		startFaceDetection();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}
}
