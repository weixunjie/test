package cn.cloudwalk.sdk;


public class FaceParam {
	/**
	 * roi��������, Ĭ����֡ͼ��
	 **/
	public int roiX; 
	public int roiY;
	public int roiWidth;
	public int roiHeight;
	/**
	 * �����ߴ緶Χ-��С�ߴ磬Ĭ��100
	 **/
	public int minSize;
	/**
	 * �����ߴ緶Χ-���ߴ磬Ĭ��400
	 **/
	public int maxSize;
	/**
	 * ÿ֡�����������Ĭ��20
	 **/
	public int maxFaceNumPerImg;
	/**
	 * һ��1-10��Խ������Խ�ͣ������ԽС��Ĭ��3
	 **/
	public int nMinNeighbors; 
	/**
	 * ȫ�ּ��Ƶ�ʣ� Ĭ��10
	 **/
	public int globleDetFreq;
	/**
	 * �Ƿ�����Ŀ����٣� Ĭ�Ͽ���. 0�رգ���0����
	 **/
	public int b_track;
	/**
	 * Ԥ����֡����Ĭ��3
	 **/
	public int det_frame_for_new;
	/**
	 * �������˳����ٵ�֡����Ĭ��150
	 **/
	public int max_frame_since_lost; 
	
}