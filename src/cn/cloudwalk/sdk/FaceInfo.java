package cn.cloudwalk.sdk;

public class FaceInfo {
	public FaceInfo()
	{

	}

	public int detected; // 1: ��⵽������,.0�����ٵ�������.
	                     // ע�� ���ٵ��Ľ�ID��������������Ч

	public int trackId;  // ����ID��ID<0��ʾû�н�����٣�
	
	// face rect������
	public int x;        // ���Ͻ�x����
	public int y;        // ���Ͻ�y����
	public int width;    // ������
	public int height;   // ������

	
	// face_point�ؼ��㣬���68���ؼ��㣬Ŀǰʹ��9��ؼ���ģ��
	public float[] keypt_x;      // �ؼ���x����
	public float[] keypt_y;      // �ؼ���y����
	public float keyptScore;    // �ؼ���÷�
	
	// face_aligned�����������ݣ�����������
	public byte[] alignedData;  // ͼ�����ݣ��ռ����128*128
	public int alignedW;        // ��
	public int alignedH;        // ��
	public int nChannels;       // ͼ��ͨ��
	
	// ������
	int livenessErrcode;		            // �����������
	int headPitch;							// ��̧ͷ������1��̧ͷ��0:ͷδ��,-1����ͷ��
	int headYaw;							// ͷת������1����ͼ�����תͷ��0��ͷδ����-1��ͼ���Ҳ�תͷ��
	int mouthAct;							// �첿������1�����죬0��δ���죩
	int eyeAct;								// �۾�������1��գ�ۣ�0��δգ�ۣ�
	int attack;								// �������ͣ�0:����ͼ�� -1:ͼ�񶶶� -2:�챻��ȡ -3:���۱���ȡ
		                                    //          -4:ͼƬ���� -5:�������ȶ� -6:����(��ֽƬ��pad)���� -7:��Ƶ������
	
	// face_quality����������
	public int   errcode;		// ��������������
	public float faceScore;     // �ܷ֣�0~1.0֮�䣬Խ������������Խ��.
	public float brightness;    // ����
	public float clearness;     // ������
	public float symmetry;      // �Գ���
	public float glassness;     // �۾�: ����ֵ0~1��ֵԽ��Խ�п���û���۾�.
	public float skiness;       // ��ɫ������ֵ0~1, ��ɫ���ռ��������ı���
	public float mouthness;     // �첿: �����趨��ֵThres��Լ��(0,1)���䣬����ThresΪ����.
	public float eyeLeft;       // ����
	public float eyeRight;      // ����
	public float occlusion;     // �۾����ڵ������Ŷȣ����ڻ��壩���Ƽ���ֵΪ0.5

	// head_poseͷ����̬
	public float pitch;         // ̧ͷ����ͷ,��Χ-90��90��Խ���ʾԽ̧ͷ
	public float yaw;           // ����תͷ
	public float roll;          // ƽ����ƫͷ

}
