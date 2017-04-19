/**
 * Project Name:cloudwalk2
 * File Name:FaceInfoCallback.java
 * Package Name:cn.cloudwalk.callback
 * Date:2016-4-27
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd All Rights Reserved.
 *
 */

package cn.cloudwalk.sdk.realtime;

import java.util.ArrayList;

import cn.cloudwalk.sdk.FaceInfo;

/**
 * ClassName: FaceInfoCallback <br/>
 * Description: <br/>
 * date: 2016-4-27 <br/>
 *
 * @author 284891377
 * @version 
 * @since JDK 1.7
 */
public interface FaceInfoCallback {
    /**
     * 
     * detectFaceInfo:人脸检测回调. <br/>
     * @author:284891377   Date: 2016年6月16日 下午2:27:30
     * @param faceInfos 每个人脸质量信息和人脸坐标信息
     * @param faceNum 人脸个数
     * @since JDK 1.7
     */
	public void detectFaceInfo(FaceInfo[] faceInfos,int faceNum);

}

