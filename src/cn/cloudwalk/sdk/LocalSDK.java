/**
 * Project Name:cloudwalk2
 * File Name:CloudwalkSDK.java
 * Package Name:cn.cloudwalk
 * Date:2016-4-27涓嬪崍4:57:54
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd All Rights Reserved.
 */

package cn.cloudwalk.sdk;

import java.io.File;

import android.content.Context;

/**
 * ClassName: CloudwalkSDK <br/>
 * Description: <br/>
 * date: 2016-4-27 涓嬪崍4:57:54 <br/>
 *
 * @author 284891377
 * @since JDK 1.7
 */
public class LocalSDK {
	public static final int ERRCODE_MIN = FaceInterface.cw_errcode_t.CW_EMPTY_FRAME_ERR;
	static LocalSDK cloudwalkSDK;
	int faceMinSize, faceMaxSize;
	String pLicence;
	String sModelPath;

	/**
	 * 鍗曚緥瀹炰緥鍖�
	 *
	 * @param mContext
	 * @return CloudwalkSDK
	 */
	public static LocalSDK getInstance(Context mContext) {

		if (null == cloudwalkSDK) {
			cloudwalkSDK = new LocalSDK();

		}
		return cloudwalkSDK;
	}

	private LocalSDK() {

	}

	/**
	 * cwInit:鍒濆鍖�. <br/>
	 *
	 * @param pLicence
	 *            鎺堟潈鐮�
	 * @param faceMinSize
	 *            璁剧疆妫�娴嬩汉鑴哥殑鏈�灏忓昂瀵� 灏忎簬鏈�灏忓昂瀵哥殑浜鸿劯灏嗘棤娉曡绠楁硶妫�娴嬪埌
	 * @param faceMaxSize
	 *            璁剧疆妫�娴嬩汉鑴哥殑鏈�澶у昂瀵� 澶т簬鏈�澶у昂瀵哥殑浜鸿劯灏嗘棤娉曡绠楁硶妫�娴嬪埌
	 * @param sModelPath
	 *            妯″瀷sd鍗¤矾寰� 濡�:/sdcard/CWModels锛岃鍏堣В鍘嬫ā鍨嬫枃浠跺埌sdcard
	 * @author:284891377 Date: 2016-4-22 涓嬪崍3:46:41
	 * @since JDK 1.7
	 */
	

	public void cwCreateHandles(String pLicence, int faceMinSize, int faceMaxSize, String sModelPath) {
		this.pLicence = pLicence;
		this.faceMinSize = faceMinSize;
		this.faceMaxSize = faceMaxSize;
		this.sModelPath = sModelPath;

		cwCreateDetChannel();
		cwCreateRecogHandle();
		cwCreateAttriHandle();
	}

	/**
	 * cwDestory:璧勬簮閲婃斁 <br/>
	 *
	 * @author:284891377 Date: 2016-4-27 涓嬪崍5:33:14
	 * @since JDK 1.7
	 */
	public void cwDestoryHandles() {

		// 绋嬪簭閫�鍑烘椂鍐嶉攢姣佸彞鏌勶紝涓嶈鍦ㄧ▼搴忎娇鐢ㄨ繃绋嬩腑棰戠箒鍒涘缓锛岄攢姣佸彞鏌�
		cwDestoryFaceDetect();
		cwDestoryFeature();
		cwDestoryAttriHandle();

	}

	// 浜鸿劯妫�娴�---------------------------------------
	// 浜鸿劯妫�娴�
	FaceDetTrack mFaceDetTrack;
	int detHandle = -1;
	FaceParam mFaceParam;

	/**
	 * 
	 * cwCreateDetTrackChannel:鍒涘缓妫�娴嬮�氶亾. <br/>
	 * 妫�娴嬮�氶亾寤鸿鍒涘缓涓�娆�<br/>
	 * 
	 * @author:284891377 Date: 2016骞�9鏈�30鏃� 涓婂崍11:30:30
	 * @return 0鎴愬姛 鍏跺畠澶辫触
	 * @since JDK 1.7
	 */
	public int cwCreateDetChannel() {
		int ret = 0;
		if (mFaceParam == null)
			mFaceParam = new FaceParam();
		mFaceDetTrack = FaceDetTrack.getInstance();
		detHandle = mFaceDetTrack.cwCreateDetHandleFromMem(pLicence);
		if (detHandle >= ERRCODE_MIN)
			ret = detHandle;
		mFaceDetTrack.cwGetFaceParam(detHandle, mFaceParam);
		mFaceParam.maxSize = faceMaxSize;
		mFaceParam.minSize = faceMinSize;
		mFaceDetTrack.cwSetFaceParam(detHandle, mFaceParam);
//		TestLog.netE(TAG, "妫�娴嬮�氶亾鑰楁椂:" + (System.currentTimeMillis() - start));
		return ret;
	}

	/**
	 * cwFaceDetect:浜鸿劯妫�娴� <br/>
	 * 
	 * @author:284891377 Date: 2016骞�9鏈�21鏃� 涓婂崍10:32:39
	 * @param imgData
	 *            鍥剧墖byte鏁版嵁
	 * @return DetectBean @see cn.cloudwalk.sdk.DetectBean
	 * @since JDK 1.7
	 */
	public DetectBean cwFaceDetect(byte[] data, int width, int height, int format, int angle, int mirror, int faceOp) {
		DetectBean detectBean = new DetectBean();

		// 妫�娴嬮�氶亾寤鸿鍒涘缓涓�娆�
		if (detHandle == -1) {

			int ret = cwCreateDetChannel();

			if (ret >= ERRCODE_MIN) {
				detectBean.ret = ret;
				return detectBean;
			}
		}
		

		int faceNum = mFaceDetTrack.cwFaceDetection(detHandle, data, width, height, format, angle, mirror, faceOp,
				detectBean.faceInfos);
		if (faceNum >= ERRCODE_MIN) {
			detectBean.ret = faceNum;   // 妫�娴嬪紓甯�
		} else {
			detectBean.faceNum = faceNum;  // detectBean.faceNum涓�0琛ㄧず鏈娴嬪埌浜鸿劯
		}
		return detectBean;
	}
    /**
     * 
     * @param data
     * @param width
     * @param height
     * @param format
     * @param angle
     * @param mirror
     * @param faceOp
     * @param faceInfos
     * @return faceNum  faceNum>ERRCODE_MIN 閿欒鐮�
     */
	public int cwFaceDetect(byte[] data, int width, int height, int format, int angle, int mirror, int faceOp,
			FaceInfo[] faceInfos) {
		// 妫�娴嬮�氶亾寤鸿鍒涘缓涓�娆�
		if (detHandle == -1) {
			int ret = cwCreateDetChannel();
			if (ret >= ERRCODE_MIN) {
				return ret;
			}
		}
		// 浜鸿劯妫�娴�
		return mFaceDetTrack.cwFaceDetection(detHandle, data, width, height, format, angle, mirror, faceOp,
				faceInfos);
	}

	/**
	 * 
	 * cwDestoryFaceDetect:鍏抽棴浜鸿劯璺熻釜妫�娴嬶紝鍦ㄥ姛鑳戒笉浣跨敤鍚庡簲鍏抽棴 <br/>
	 * 寤鸿鍦╫nStop鎴栬�卭nDestory璋冪敤<br/>
	 * 
	 * @author:284891377 Date: 2016骞�9鏈�21鏃� 涓婂崍10:30:01
	 * @since JDK 1.7
	 */
	public int cwDestoryFaceDetect() {
		int ret = 0;
		if (detHandle != -1) {
			ret = mFaceDetTrack.cwReleaseDetHandle(detHandle);
			if (ret == FaceInterface.cw_errcode_t.CW_OK)
				detHandle = -1;
		}
		return ret;
	}
	// 浜鸿劯妫�娴�---------------------------------------

	
	// 鐗瑰緛鎻愬彇---------------------------------------

	FaceRecog cwRecog = null;
	int iRecogHandle = -1;
	public int iFeaLen = -1;

	/**
	 * 
	 * cwCreateFeatureChannel:鍒涘缓鐗瑰緛閫氶亾. <br/>
	 * 鐗瑰緛閫氶亾寤鸿鍒涘缓涓�娆�<br/>
	 * 
	 * @author:284891377 Date: 2016骞�9鏈�30鏃� 涓婂崍11:30:30
	 * @return 0鎴愬姛 鍏跺畠澶辫触
	 * @since JDK 1.7
	 */
	public int cwCreateRecogHandle() {
//		long start = System.currentTimeMillis();
		cwRecog = FaceRecog.getInstance();
		int ret = 0;
		// 鐗瑰緛閫氶亾寤鸿鍒涘缓涓�娆�
		String sRecogModelPath = new StringBuilder(sModelPath).append(File.separator).append("CWR_Config_1_1.xml")
				.toString();
		if (iRecogHandle == -1) {
			iRecogHandle = cwRecog.cwCreateRecogHandle(sRecogModelPath, pLicence, 0, -1);
		}
		
		if (iRecogHandle >= ERRCODE_MIN) {
			ret = iRecogHandle;
		}
		else {
			iFeaLen = cwRecog.cwGetFeatureLength(iRecogHandle);
		}	

//		TestLog.netE(TAG, "鐗瑰緛閫氶亾鑰楁椂:" + (System.currentTimeMillis() - start));
		return ret;
	}
	
	public FeatureBean GetFeatureFromImgData(byte[] imgData, boolean bFiled)
    {  				
		int iRet = -1;
		// 鐗瑰緛閫氶亾寤鸿鍒涘缓涓�娆�
		if (iRecogHandle == -1) {
			iRet = cwCreateRecogHandle();
		}
		
		FeatureBean featureBean = new FeatureBean(iFeaLen, bFiled);
		if (iRet >= ERRCODE_MIN) {
			featureBean.ret = iRet;
			return featureBean;
		}

    	FaceInfo[] pFaceBuffer = new FaceInfo[3]; 
    	for (int i = 0; i < 3; i++)
    	{
    		pFaceBuffer[i] = new FaceInfo();
    	}
    	iRet = cwFaceDetect(imgData, 0, 0, FaceInterface.cw_img_form_t.CW_IMAGE_BINARY, 0, 0,
    			FaceInterface.cw_op_t.CW_OP_DET | FaceInterface.cw_op_t.CW_OP_ALIGN, pFaceBuffer);
    	if (iRet >= FaceInterface.cw_errcode_t.CW_EMPTY_FRAME_ERR) {
    		
    		featureBean.ret = iRet;
    	}
    	else if (iRet < 1) {
    		
    		featureBean.ret = -1;
    	}
    	else {
    		// 鎻愬彇filed鐗瑰緛
        	if (bFiled)
        	{
        		// 鎻愬彇妫�娴嬪埌鐨勭涓�寮犱汉鑴革紝涓烘渶澶т汉鑴�
        		iRet = cwRecog.cwGetFiledFeature(iRecogHandle, pFaceBuffer[0].alignedData, pFaceBuffer[0].alignedW,  
        				pFaceBuffer[0].alignedH, pFaceBuffer[0].nChannels, 1, featureBean.btFeature, iFeaLen);
        	}
        	else  // 鎻愬彇probe鐗瑰緛
        	{
        		iRet = cwRecog.cwGetProbeFeature(iRecogHandle, pFaceBuffer[0].alignedData, pFaceBuffer[0].alignedW,  
        				pFaceBuffer[0].alignedH, pFaceBuffer[0].nChannels, 1, featureBean.btFeature, iFeaLen);
        	}   	
        	
        	featureBean.ret = iRet;    	
    	}
    	
    	return featureBean;
    }
	
	
	// 1锛�1浜鸿劯姣斿
	public VerifyBean cwVerify(byte[] btFeaProbe, byte[] btFeaFiled) {
		
		int iRet = -1;
		// 鐗瑰緛閫氶亾寤鸿鍒涘缓涓�娆�
		if (iRecogHandle == -1) {
			iRet = cwCreateRecogHandle();
		}
		
		VerifyBean verifyBean = new VerifyBean();
		if (iRet >= ERRCODE_MIN) {
			verifyBean.ret = iRet;
			return verifyBean;
		}

		// 浜鸿劯姣斿
		float[] pScores = new float[1];
		// 1:1
		iRet = cwRecog.cwComputeMatchScore(iRecogHandle, btFeaProbe, iFeaLen, 1, btFeaFiled, iFeaLen, 1, pScores);
		verifyBean.score = pScores[0];
		verifyBean.ret = iRet;

		return verifyBean;
	}
	
	// 1锛歂浜鸿劯妫�绱�
		public RecogBean cwRecog(byte[] btFeaProbe, byte[] btFeaFiled, int iFiledNum) {
			
			int iRet = -1;
			// 鐗瑰緛閫氶亾寤鸿鍒涘缓涓�娆�
			if (iRecogHandle == -1) {
				iRet = cwCreateRecogHandle();
			}
			
			RecogBean recogBean = new RecogBean(iFiledNum);
			if (iRet >= ERRCODE_MIN) {
				recogBean.ret = iRet;
				return recogBean;
			}

			// 1:n
			iRet = cwRecog.cwComputeMatchScore(iRecogHandle, btFeaProbe, iFeaLen, 1, btFeaFiled, iFeaLen, iFiledNum, recogBean.scores);
			recogBean.ret = iRet;

			return recogBean;
		}

	/**
	 * 
	 * cwDestoryFeature:鍏抽棴鐗瑰緛鎻愬彇锛屽湪鍔熻兘涓嶄娇鐢ㄥ悗搴斿叧闂� <br/>
	 * 寤鸿鍦╫nStop鎴栬�卭nDestory璋冪敤<br/>
	 * 
	 * @author:284891377 Date: 2016骞�9鏈�21鏃� 涓婂崍10:30:01
	 * @since JDK 1.7
	 */
	public int cwDestoryFeature() {
		int ret = 0;
		if (iRecogHandle != -1) {
			ret = cwRecog.cwReleaseRecogHandle(iRecogHandle);
			if (ret == FaceInterface.cw_errcode_t.CW_OK)
				iRecogHandle = -1;
		}
		return ret;
	}



	// 灞炴�у垎鏋�---------------------------------------
	FaceAttribute cwAttri = null;
	int attributeChannel = -1;
	
	/**
	 * 
	 * cwCreateAttriHandle:鍒涘缓灞炴�ц瘑鍒�氶亾. <br/>
	 * 灞炴�ц瘑鍒缓璁垱寤轰竴娆�<br/>
	 * 
	 * @author:284891377 Date: 2016骞�9鏈�30鏃� 涓婂崍11:30:30
	 * @return 0鎴愬姛 鍏跺畠澶辫触
	 * @since JDK 1.7
	 */
	public int cwCreateAttriHandle() {
		cwAttri = FaceAttribute.getInstance();

		int ret = 0;
		// 灞炴�ч�氶亾寤鸿鍒涘缓涓�娆�
		String sAttriModelPath = new StringBuilder(sModelPath).append(File.separator).append("CWR_Config_attri.xml")
				.toString();
		if (attributeChannel == -1) {
			attributeChannel = cwAttri.cwCreateAttributeHandle(sAttriModelPath, pLicence, -1);
		}
		
		if (attributeChannel >= ERRCODE_MIN) {
			ret = attributeChannel;
		}

		return ret;
	}
	
	
	/**
	 * 
	 * cwGetAttriFromImgData:鑾峰彇灞炴�т俊鎭紝鍖呮嫭骞撮緞鍜屾�у埆. <br/>
	 * 
	 * @author:284891377 Date: 2016骞�9鏈�30鏃� 涓婂崍11:30:30
	 * @return AttributeBean
	 * @since JDK 1.7
	 */
	public AttributeBean cwGetAttriFromImgData(byte[] imgData) {
		
		int iRet = -1;
		// 灞炴�ч�氶亾寤鸿鍒涘缓涓�娆�
		if (attributeChannel == -1) {
			iRet =cwCreateAttriHandle();
		}
		
		AttributeBean attriBean = new AttributeBean();
		if (iRet >= ERRCODE_MIN) {
			attriBean.ret = iRet;
			return attriBean;
		}

    	FaceInfo[] pFaceBuffer = new FaceInfo[3]; 
    	for (int i = 0; i < 3; i++)
    	{
    		pFaceBuffer[i] = new FaceInfo();
    	}
    	iRet = cwFaceDetect(imgData, 0, 0, FaceInterface.cw_img_form_t.CW_IMAGE_BINARY, 0, 0,
    			FaceInterface.cw_op_t.CW_OP_DET | FaceInterface.cw_op_t.CW_OP_ALIGN, pFaceBuffer);
    	if (iRet >= FaceInterface.cw_errcode_t.CW_EMPTY_FRAME_ERR) {
    		
    		attriBean.ret = iRet;
    	}
    	else if (iRet < 1) {
    		
    		attriBean.ret = -1;
    	}
    	else {
    		FaceAttrRet attr = new FaceAttrRet();
    		iRet = cwAttri.cwGetAgeGenderEval(attributeChannel, pFaceBuffer[0].alignedData, pFaceBuffer[0].alignedW,  
    				pFaceBuffer[0].alignedH, pFaceBuffer[0].nChannels, attr);
    		
    		attriBean.ret = iRet;
    		if (iRet == 0) {
    			attriBean.age = attr.m_iAge;
    			attriBean.gender = attr.m_iGender;
    		}
    	}
    	
		return attriBean;
	}
	
	
	/**
	 * 
	 * cwDestoryAttriHandle:閿�姣佸睘鎬ц瘑鍒�氶亾. <br/>
	 * 
	 * @author:284891377 Date: 2016骞�9鏈�30鏃� 涓婂崍11:30:30
	 * @return 0鎴愬姛 鍏跺畠澶辫触
	 * @since JDK 1.7
	 */
	public int cwDestoryAttriHandle() {
		int ret = 0;
		if (attributeChannel != -1) {
			ret = cwAttri.cwReleaseAttributeHandle(attributeChannel);
			if (ret == FaceInterface.cw_errcode_t.CW_OK)
				attributeChannel = -1;
		}
		return ret;
	}
	
	
	
	 /**
	 * 鑾峰彇閿欒鐮佽鏄�
	 */
	public static String getErrMsg(int errCode) {
		String errMsg = "鎴愬姛";
		switch (errCode) {
		case FaceInterface.cw_errcode_t.CW_OK: // 鎴愬姛
			errMsg = "鎴愬姛";
			break;
		//
		case FaceInterface.cw_errcode_t.CW_EMPTY_FRAME_ERR:
			errMsg = "绌哄浘鍍�";
			break; // 绌哄浘鍍�
		case FaceInterface.cw_errcode_t.CW_UNSUPPORT_FORMAT_ERR:
			errMsg = "鍥惧儚鏍煎紡涓嶆敮鎸�";
			break; // 鍥惧儚鏍煎紡涓嶆敮鎸�
		case FaceInterface.cw_errcode_t.CW_ROI_ERR:
			errMsg = "ROI璁剧疆澶辫触";
			break; // ROI璁剧疆澶辫触
		case FaceInterface.cw_errcode_t.CW_MINMAX_ERR:
			errMsg = "鏈�灏忔渶澶т汉鑴歌缃け璐�";
			break; // 鏈�灏忔渶澶т汉鑴歌缃け璐�
		case FaceInterface.cw_errcode_t.CW_OUTOF_RANGE_ERR:
			errMsg = "鏁版嵁鑼冨洿閿欒";
			break; // 鏁版嵁鑼冨洿閿欒

		case FaceInterface.cw_errcode_t.CW_UNAUTHORIZED_ERR:
			errMsg = "鏈巿鏉�";
			break; // 鏈巿鏉�
		case FaceInterface.cw_errcode_t.CW_UNINITIALIZED_ERR:
			errMsg = "灏氭湭鍒濆鍖�";
			break; // 灏氭湭鍒濆鍖�
		case FaceInterface.cw_errcode_t.CW_METHOD_UNAVAILABLE:
			errMsg = "鏂规硶鏃犳晥";
			break; // 鏂规硶鏃犳晥
		case FaceInterface.cw_errcode_t.CW_PARAM_INVALID:
			errMsg = "鍙傛暟鏃犳晥";
			break; // 鍙傛暟鏃犳晥
		case FaceInterface.cw_errcode_t.CW_DETECT_MODEL_ERR:
			errMsg = "鍔犺浇妫�娴嬫ā鍨嬪け璐�";
			break; // 鍔犺浇妫�娴嬫ā鍨嬪け璐�
		case FaceInterface.cw_errcode_t.CW_KEYPT_MODEL_ERR:
			errMsg = "鍔犺浇鍏抽敭鐐规ā鍨嬪け璐�";
			break; // 鍔犺浇鍏抽敭鐐规ā鍨嬪け璐�
		case FaceInterface.cw_errcode_t.CW_QUALITY_MODEL_ERR:
			errMsg = "鍔犺浇璐ㄩ噺璇勪及妯″瀷澶辫触";
			break; // 鍔犺浇璐ㄩ噺璇勪及妯″瀷澶辫触
		case FaceInterface.cw_errcode_t.CW_EXCEEDMAXHANDLE_ERR:
			errMsg = "瓒呰繃鎺堟潈鏈�澶у彞鏌勬暟";
			break; // 瓒呰繃鎺堟潈鏈�澶у彞鏌勬暟

		case FaceInterface.cw_errcode_t.CW_DET_ERR:
			errMsg = "妫�娴嬪け璐�";
			break; // 妫�娴嬪け璐�
		case FaceInterface.cw_errcode_t.CW_TRACK_ERR:
			errMsg = "璺熻釜澶辫触";
			break; // 璺熻釜澶辫触
		case FaceInterface.cw_errcode_t.CW_KEYPT_ERR:
			errMsg = "鎻愬彇鍏抽敭鐐瑰け璐�";
			break; // 鎻愬彇鍏抽敭鐐瑰け璐�
		case FaceInterface.cw_errcode_t.CW_ALIGN_ERR:
			errMsg = "瀵归綈浜鸿劯澶辫触";
			break; // 瀵归綈浜鸿劯澶辫触
		case FaceInterface.cw_errcode_t.CW_QUALITY_ERR:
			errMsg = "璐ㄩ噺璇勪及澶辫触";
			break; // 璐ㄩ噺璇勪及澶辫触

		case FaceInterface.cw_errcode_t.CW_RECOG_FEATURE_MODEL_ERR:
			errMsg = " 鍔犺浇鐗瑰緛璇嗗埆妯″瀷澶辫触";
			break; // 鍔犺浇鐗瑰緛璇嗗埆妯″瀷澶辫触
		case FaceInterface.cw_errcode_t.CW_RECOG_ALIGNEDFACE_ERR:
			errMsg = "瀵归綈鍥剧墖鏁版嵁閿欒";
			break; // 瀵归綈鍥剧墖鏁版嵁閿欒
		case FaceInterface.cw_errcode_t.CW_RECOG_MALLOCMEMORY_ERR:
			errMsg = "棰勫垎閰嶇壒寰佺┖闂翠笉瓒�";
			break; // 棰勫垎閰嶇壒寰佺┖闂翠笉瓒�
		case FaceInterface.cw_errcode_t.CW_RECOG_FILEDDATA_ERR:
			errMsg = "鐢ㄤ簬娉ㄥ唽鐨勭壒寰佹暟鎹敊璇�";
			break; // 鐢ㄤ簬娉ㄥ唽鐨勭壒寰佹暟鎹敊璇�
		case FaceInterface.cw_errcode_t.CW_RECOG_PROBEDATA_ERR:
			errMsg = "鐢ㄤ簬妫�绱㈢殑鐗瑰緛鏁版嵁閿欒";
			break; // 鐢ㄤ簬妫�绱㈢殑鐗瑰緛鏁版嵁閿欒
		case FaceInterface.cw_errcode_t.CW_RECOG_EXCEEDMAXFEASPEED:
			errMsg = "瓒呰繃鎺堟潈鏈�澶ф彁鐗瑰緛閫熷害";
			break; // 瓒呰繃鎺堟潈鏈�澶ф彁鐗瑰緛閫熷害
		case FaceInterface.cw_errcode_t.CW_RECOG_EXCEEDMAXCOMSPEED:
			errMsg = "瓒呰繃鎺堟潈鏈�澶ф瘮瀵归�熷害";
			break; // 瓒呰繃鎺堟潈鏈�澶ф瘮瀵归�熷害

		case FaceInterface.cw_errcode_t.CW_ATTRI_AGEGENDER_MODEL_ERR:
			errMsg = "鍔犺浇骞撮緞鎬у埆妯″瀷澶辫触";
			break; // 鍔犺浇骞撮緞鎬у埆妯″瀷澶辫触
		case FaceInterface.cw_errcode_t.CW_ATTRI_EVAL_AGEGENDER_ERR:
			errMsg = "骞撮緞鎬у埆璇嗗埆澶辫触";
			break; // 骞撮緞鎬у埆璇嗗埆澶辫触
		case FaceInterface.cw_errcode_t.CW_ATTRI_NATIONALITY_MODEL_ERR:
			errMsg = "鍔犺浇鍥界睄骞撮緞娈垫ā鍨嬪け璐�";
			break; // 鍔犺浇鍥界睄骞撮緞娈垫ā鍨嬪け璐�
		case FaceInterface.cw_errcode_t.CW_ATTRI_EVAL_NATIONALITY_ERR:
			errMsg = " 鍥界睄骞撮緞娈佃瘑鍒け璐�";
			break; // 鍥界睄骞撮緞娈佃瘑鍒け璐�

		case FaceInterface.cw_errcode_t.CW_LICENCE_ACCOUNT_TIMEOUT:
			errMsg = "瀹夊崜缃戠粶鎺堟潈璐﹀彿杩囨湡";
			break; // 瀹夊崜缃戠粶鎺堟潈璐﹀彿杩囨湡
		case FaceInterface.cw_errcode_t.CW_LICENCE_HTTP_ERROR:
			errMsg = "瀹夊崜缃戠粶鎺堟潈http閿欒";
			break; // 瀹夊崜缃戠粶鎺堟潈http閿欒
		case FaceInterface.cw_errcode_t.CW_LICENCE_MALLOCMEMORY_ERR:
			errMsg = "瀹夊崜缃戠粶鎺堟潈鍐呭瓨鍒嗛厤涓嶈冻";
			break; // 瀹夊崜缃戠粶鎺堟潈鍐呭瓨鍒嗛厤涓嶈冻

		}
		return errMsg;

	}
}
