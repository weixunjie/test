package cn.cloudwalk.localsdkdemo.camera;

import com.example.com.jld.facecheck.app.Constants;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import cn.cloudwalk.sdk.FaceInterface;
import cn.cloudwalk.sdk.realtime.LocalFaceSDK;
import cn.cloudwalk.sdk.realtime.CaremaType;

/**
 * 瀹炴椂棰勮甯� setPreviewCallback
 * 
 * @author yusr
 *
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, PreviewCallback {

	private static final String TAG ="CameraPreview";

	private Camera mCamera;

	Delegate mDelegate;
	private int orientation;
	/**
	 * 璁剧疆灞忓箷鏂瑰悜
	 *
	 * @param orientation
	 *            Configuration.ORIENTATION_LANDSCAPE 鎴栬��
	 *            Configuration.ORIENTATION_PORTRAIT
	 */
	public void setScreenOrientation(int orientation) {
		this.orientation = orientation;

	}

	//鎽勫儚澶磇d锛屽墠缃繕鏄悗缃紝榛樿鍚庣疆
	int caremaId=Camera.CameraInfo.CAMERA_FACING_FRONT;
	public int getCaremaId() {
		return caremaId;
	}
	public void setCaremaId(int caremaId) {
		this.caremaId = caremaId;
	}

	private boolean mPreviewing = true;
	private boolean mSurfaceCreated = false;
	private CameraConfigurationManager mCameraConfigurationManager;
	Context context;
    /**
     * setReqPrevWH:璁剧疆甯屾湜鐨勯瑙堝垎杈ㄧ巼. <br/>
     * @author:284891377   Date: 2016/10/25 0025 10:50
     *
     * @since JDK 1.7
     */
	public void setReqPrevWH(int reqPrevW,int reqPrevH) {
		this.reqPrevW = reqPrevW;
		this. reqPrevH= reqPrevH;
	}

	int reqPrevW=Constants.PREVIEW_W,reqPrevH=Constants.PREVIEW_H;


	public CameraPreview(Context context) {
		super(context);
		this.context = context;
	}

	public CameraPreview(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
		this.context = context;
	}

	public CameraPreview(Context context, AttributeSet attrs) {

		super(context, attrs);
		this.context = context;
	}

	public void setCamera(Camera camera) {
		mCamera = camera;
		if (mCamera != null) {
			mCameraConfigurationManager = new CameraConfigurationManager(getContext());

			getHolder().addCallback(this);
			if (mPreviewing) {
				requestLayout();
			} else {
				showCameraPreview();
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		mSurfaceCreated = true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
		if (surfaceHolder.getSurface() == null) {
			return;
		}
		stopCameraPreview();
		showCameraPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		mSurfaceCreated = false;
		stopCameraPreview();
	}

	public void showCameraPreview() {
		if (mCamera != null) {
			try {
				mPreviewing = true;
				mCamera.setPreviewDisplay(getHolder());

				mCameraConfigurationManager.setCameraParametersForPreviewCallBack(mCamera, caremaId, reqPrevW,
						reqPrevH);
				mCamera.startPreview();
				mCamera.setPreviewCallback(CameraPreview.this);
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			}
		}
	}

	public void stopCameraPreview() {
		if (mCamera != null) {
			try {

				mPreviewing = false;
				mCamera.cancelAutoFocus();
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			}
		}
	}

	public void openFlashlight() {
		if (flashLightAvaliable()) {
			mCameraConfigurationManager.openFlashlight(mCamera);
		}
	}

	public void closeFlashlight() {
		if (flashLightAvaliable()) {
			mCameraConfigurationManager.closeFlashlight(mCamera);
		}
	}

	private boolean flashLightAvaliable() {
		return mCamera != null && mPreviewing && mSurfaceCreated
				&& getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}

	/******************************************************************/
	public Size getPreviewSize() {
		Camera.Parameters parameters = mCamera.getParameters();
		return parameters.getPreviewSize();
	}

	public void setDelegate(Delegate mDelegate) {
		this.mDelegate = mDelegate;
	}

	/**
	 * 鎵撳紑鎽勫儚澶村紑濮嬮瑙堬紝浣嗘槸骞舵湭寮�濮嬭瘑鍒�
	 */
	public void cwStartCamera() {
		if (mCamera != null) {
			return;
		}

		try {
			mCamera = Camera.open(caremaId);
		} catch (Exception e) {
			if (mDelegate != null) {
				mDelegate.onOpenCameraError();
			}
		}
		setCamera(mCamera);
	}

	/**
	 * 鍏抽棴鎽勫儚澶撮瑙堬紝骞朵笖闅愯棌鎵弿妗�
	 */
	public void cwStopCamera() {
		if (mCamera != null) {
			stopCameraPreview();

			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {

		LocalFaceSDK.getInstance(context).cwPushFrame(data, reqPrevW, reqPrevH,
				FaceInterface.cw_img_form_t.CW_IMAGE_NV21, CaremaType.FRONT_LANDSCAPE);
		
		/*if (caremaId == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 鍓嶇疆
			if (Configuration.ORIENTATION_PORTRAIT == orientation) {// 绔栧睆
																	// 姘村钩闀滃儚+鏃嬭浆90
				LocalFaceSDK.getInstance(context).cwPushFrame(data, reqPrevW, reqPrevH,
						FaceInterface.cw_img_form_t.CW_IMAGE_NV21, CaremaType.FRONT_PORTRAIT);
			} else {// 妯睆 姘村钩闀滃儚

				LocalFaceSDK.getInstance(context).cwPushFrame(data, reqPrevW, reqPrevH,
						FaceInterface.cw_img_form_t.CW_IMAGE_NV21, CaremaType.FRONT_LANDSCAPE);
			}

		} else {// 鍚庣疆
			if (Configuration.ORIENTATION_PORTRAIT == orientation) {// 绔栧睆 鏃嬭浆90
				LocalFaceSDK.getInstance(context).cwPushFrame(data, reqPrevW, reqPrevH,
						FaceInterface.cw_img_form_t.CW_IMAGE_NV21, CaremaType.BACK_PORTRAIT);

			} else {
				// 妯睆涓嶅仛澶勭悊
				LocalFaceSDK.getInstance(context).cwPushFrame(data, reqPrevW, reqPrevH,
						FaceInterface.cw_img_form_t.CW_IMAGE_NV21, CaremaType.FRONT_LANDSCAPE);
			}

		}*/

	}


	/**
	 * 鍒囨崲鎽勫儚澶�
	 */
	public int switchCarema() {
		cwStopCamera();
		if (caremaId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			caremaId = Camera.CameraInfo.CAMERA_FACING_BACK;
		} else {
			caremaId = Camera.CameraInfo.CAMERA_FACING_FRONT;
		}
		cwStartCamera();
		return caremaId;
	}
}