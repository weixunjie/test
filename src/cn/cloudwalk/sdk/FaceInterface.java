package cn.cloudwalk.sdk;


public interface FaceInterface {

	// ͼ���ʽ
	interface cw_img_form_t extends FaceInterface {
		int CW_IMAGE_GRAY8 = 0;
		int CW_IMAGE_BGR888 = 1;
		int CW_IMAGE_BGRA8888 = 2;
		int CW_IMAGE_YUV420P = 3;
		int CW_IMAGE_YV12 = 4;
		int CW_IMAGE_NV12 = 5;
		int CW_IMAGE_NV21 = 6;
		int CW_IMAGE_BINARY = 7;
	}

	// ͼ����ת�Ƕȣ���ʱ�룩
	interface cw_img_angle_t extends FaceInterface {
		int CW_IMAGE_ANGLE_0 = 0;
		int CW_IMAGE_ANGLE_90 = 1;
		int CW_IMAGE_ANGLE_180 = 2;
		int CW_IMAGE_ANGLE_270 = 3;
	}
	
	// ͼ����
	interface cw_img_mirror_t extends FaceInterface {
		int CW_IMAGE_MIRROR_NONE = 0;        // ������  
		int CW_IMAGE_MIRROR_HOR = 1;         // ˮƽ����
		int CW_IMAGE_MIRROR_VER = 2;         // ��ֱ����
		int CW_IMAGE_MIRROR_HV = 3;          // ��ֱ��ˮƽ����
	}

	// ��⿪��ѡ��
	interface cw_op_t extends FaceInterface {
		int CW_OP_DET     = 0;                  // (1 << 0) ����������⣬��������������λ��
		int CW_OP_TRACK   = 2;                  // (1 << 1)�����������٣��������������ٵ�ID
		int CW_OP_KEYPT   = 4;                  // (1 << 2)���������ؼ����⣬�����������ϵĹؼ���������Ϣ
		int CW_OP_ALIGN   = 8;                  // (1 << 3)��������ͼ����룬�����ض���������ͼ�񣨿�ֱ������������ȡ�ӿڣ����ؼ�����Ϣ
		int CW_OP_QUALITY_BASE = 16;            // (1 << 4)���������ֻ�������
		// �����忪��ѡ�ʵ��Ӧ���У����ʲô������ö�Ӧ�Ķ�������+�������أ�
		int CW_OP_LIVENESS_HEAD_UP = 64;		// (1<<6) ������: ̧ͷ
		int CW_OP_LIVENESS_HEAD_DOWN = 128;		// (1<<7) ������: ��ͷ
		int CW_OP_LIVENESS_HEAD_LEFT = 256;	    // (1<<8) ������: ����תͷ
		int CW_OP_LIVENESS_HEAD_RIGHT = 512;	// (1<<9) ������: ����תͷ
		int CW_OP_LIVENESS_MOUTH = 1024;        // (1<<10)������: �첿
		int CW_OP_LIVENESS_EYE = 2048;	        // (1<<11)������: �۾�
		int CW_OP_LIVENESS_ATTACK = 4096;	    // (1<<12)������: ��������
		int CW_OP_LIVENESS = 8128;	            // �����⣺�������ֿ����ۺ� 
	}
	
	// �����������
	interface cw_recog_pattern_t extends FaceInterface {
		int CW_Feature_Extract = 0;
		int CW_Recognition = 1;
	}

	// ͨ�ô�����
	interface cw_errcode_t extends FaceInterface {
		int CW_OK = 0;                               // �ɹ�

		int CW_EMPTY_FRAME_ERR = 20000;              // ��ͼ��
		int CW_UNSUPPORT_FORMAT_ERR = 20001;         // ͼ���ʽ��֧��
		int CW_ROI_ERR = 20002;                      // ROI����ʧ��
		int CW_MINMAX_ERR = 20003;                   // ��С�����������ʧ��
		int CW_OUTOF_RANGE_ERR = 20004;              // ���ݷ�Χ����
		
		int CW_UNAUTHORIZED_ERR = 20005;             // δ��Ȩ
		int CW_UNINITIALIZED_ERR = 20006;            // ��δ��ʼ��
		int CW_METHOD_UNAVAILABLE = 20007;	         // ������Ч
		int CW_PARAM_INVALID = 20008;                // ������Ч
		int CW_DETECT_MODEL_ERR = 20009;             // ���ؼ��ģ��ʧ��
		int CW_KEYPT_MODEL_ERR = 20010;              // ���عؼ���ģ��ʧ��
		int CW_QUALITY_MODEL_ERR = 20011;            // ������������ģ��ʧ��
		int CW_LIVENESS_MODEL_ERR = 20012;		     // ���ػ�����ģ��ʧ��
		
		int CW_DET_ERR = 20013;                      // ���ʧ��
		int CW_TRACK_ERR = 20014;                    // ����ʧ��
		int CW_KEYPT_ERR = 20015;                    // ��ȡ�ؼ���ʧ��
		int CW_ALIGN_ERR = 20016;                    // ��������ʧ��
		int CW_QUALITY_ERR = 20017;                  // ��������ʧ��
		int CW_EXCEEDMAXHANDLE_ERR = 20018;          // ������Ȩ�������
		
		int CW_RECOG_FEATURE_MODEL_ERR = 20019;      // ��������ʶ��ģ��ʧ��
		int CW_RECOG_ALIGNEDFACE_ERR = 20020;        // ����ͼƬ���ݴ���
		int CW_RECOG_MALLOCMEMORY_ERR = 20021;       // Ԥ���������ռ䲻��
		int CW_RECOG_FILEDDATA_ERR = 20022;          // ����ע����������ݴ���
		int CW_RECOG_PROBEDATA_ERR = 20023;          // ���ڼ������������ݴ���
		int CW_RECOG_EXCEEDMAXFEASPEED = 20024;      // ������Ȩ����������ٶ�
		int CW_RECOG_EXCEEDMAXCOMSPEED = 20025;      // ������Ȩ���ȶ��ٶ�
		
		int CW_ATTRI_AGEGENDER_MODEL_ERR = 20026;    // ���������Ա�ģ��ʧ�� 
		int CW_ATTRI_EVAL_AGEGENDER_ERR = 20027;     // �����Ա�ʶ��ʧ��
		int CW_ATTRI_NATIONALITY_MODEL_ERR = 20028;  // ���ع��������ģ��ʧ�� 
		int CW_ATTRI_EVAL_NATIONALITY_ERR = 20029;   // ���������ʶ��ʧ��

		int CW_LICENCE_ACCOUNT_TIMEOUT = 20030;      // ��׿������Ȩ�˺Ź���
		int CW_LICENCE_HTTP_ERROR = 20031;           // ��׿������Ȩhttp����
		int CW_LICENCE_MALLOCMEMORY_ERR = 20032;     // ��׿������Ȩ�ڴ���䲻��

	}
	
	// �����ּ�������
	interface cw_quality_errcode_t extends FaceInterface {
		int CW_QUALITY_OK            = 0;				// ������������Ч
		int	CW_QUALITY_NO_DATA       = 20150;		    // ������������Ч��ԭ����δ���
		int	CW_QUALITY_ERROR_UNKNOWN = 20151;           // δ֪����
	}
	
	interface cw_liveness_errcode_t extends FaceInterface {
		int CW_LIVENESS_OK = 0;				            // ����������Ч
		int CW_LIVENESS_BUFFERING = 20100;		        // ����������Ч��ԭ�����ڼ��
		int CW_LIVENESS_NOT_TARGET = 20101;			    // ����������Ч��ԭ�򣺲��Ǳ���Ŀ�꣨�������������δ������٣�
		int CW_LIVENESS_CHANGED = 20102;				// ����������Ч��ԭ�򣺻��� 
		int CW_LIVENESS_POOR_QAULITY = 20103;			// ����������Ч��ԭ������������
	}
	
}
