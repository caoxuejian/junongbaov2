package com.nxt.ynt.utils;

import java.io.File;

import android.os.Environment;


public class Constans {
	/**
	 * �����ļ�����
	 */
	public static final String HOST="http://219.232.243.58:83";
	public static final String APP_CONFIG_FILE_NAME = "xnb_config";
	public static final String VIBRATE_SELECT_STATE = "vibrate_select_state";
	// ��ȡ��֤��
	public static String getcode = "meilisannong/server/index.php/article_interface/forgetpass_sms";
	// �޸�����
	public static String change_password = "meilisannong/server/index.php/article_interface/forgetpass2";
	// ��������
	public static String nearby_people = "meilisannong/server/index.php/user_interface/nearusers";
	// ҡһҡ
	public static String yaoyiyao = "meilisannong/server/index.php/user_interface/nearuser";
	// �ж��ֻ�ͨѶ¼�е��ֻ����Ƿ�ע���
	public static String if_phone_reg_url = "meilisannong/server/index.php/article_interface/serachemembers";
	public static String getxxtAllNews ="http://m2.365960.com/meilisannong/server/index.php/category_interface/getallnews_v2?";
	//��Ϣͨh5ҳ��
	public static String getXXTNewsH5="http://m2.365960.com/meilisannong/server/index.php/article_list/xxt_article_list?lid=";
	//��Ƶ
	public static String getXXTSP="http://m2.365960.com/meilisannong/server/index.php/article_list/lists?lid=";
	//��Ϣͨ�۸�����
	public static String getPrices="http://m2.365960.com/meilisannong/server/index.php/Price/index";
	//����ͳ��
	public static String sharecount="http://m2.365960.com/meilisannong/server/index.php/api_v2/countshare";
	// ע��ӿ�
	public static String REGURL = "meilisannong/server/index.php/user_interface/publicregister2";
	// ��¼�ӿ�
	public static String LOGINURL = "meilisannong/server/index.php/user_interface/publiclogin2";
	// ע���ӿ�
	public static String LOGOUT = "meilisannong/server/index.php/user_interface/loginouts";
	// ���˽ӿ�
	public static String find_url = "meilisannong/server/index.php/article_interface/searchmember";
	// ������Ӻ��ѽӿ�
	public static String add_friends = "meilisannong/server/index.php/article_interface/addfriendrequest";
	// ͬ����Ӻ��ѽӿ�
	public static String agree_add_friends = "meilisannong/server/index.php/api_interface/addfriend";
	// ���ںŽӿ�
	public static String SHOW_GZH = "meilisannong/server/index.php/api_interface/public_user_list";
	// �������ںŽӿ�
	public static String SEARCH_GZH = "meilisannong/server/index.php/article_interface/seracheuser_groups";
	// �ı�ע�ӿ�
	public static String USER_REMARKS = "meilisannong/server/index.php/api_interface/userremarks";
	public static String NX_RECV_SAVE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "JNB" + File.separator + "JNBfile_recv";
	// �ٲ���
	public static final String ROOT_URL = com.nxt.nxtapp.setting.GetHost
			.getHost() + "";
	// ��ţ����ͼƬ
	public static String DOWNLOAD_IMAGE = "http://nongxlog.qiniudn.com/";
	public static final int GONGLUE = 163;
	public static boolean DEBUG = true;
	public final static String SPF_DATE = "Config";
	public final static String SPF_DATE_FLAG = "yes";
	// վ��
	public static final String SITEID = "3742";
	public final static String REGISTER_URL = HOST
			+ "/meilisannong/server/index.php/user_interface/mmyregister";
	public final static String PUBLIC_POSTCONTENT_URL = HOST
			+ "/meilisannong/server/index.php/article_interface/requestbuysell";
	public final static String PUBLIC_POSTPICTURE_URL = HOST
			+ "/meilisannong/server/index.php/article_interface/selluploadpic";
	// ������Ϣ��ר���ʴ𡢸���ũ��ӿ�
	public static final String gq_wd_nq_URL = HOST
			+ "/meilisannong/server/index.php/article_interface/getbuysellinfos";
	// �����ύ�ӿ�
	public final static String PINGLUN_POST_URL = HOST
			+ "/meilisannong/server/index.php/article_interface/buysellcomment";
	public static boolean updateFlag = false;
	// ת�����޽ӿ�
	public final static String ZHUANFA_POST_URL = HOST
			+ "/meilisannong/server/index.php/article_interface/buysellcomment";
	// ר���б�
	public final static String expert_list_url = HOST
			+ "/meilisannong/server/index.php/article_interface/getzj";
	public final static String expert_list_fj_url = HOST
			+ "/json/ynt/expertlistfj.json";
	// ΢���ӿ�
	public final static String WEIBO_URL = HOST
			+ "/meilisannong/server/index.php/article_interface/getdynamics?";
	public final static String XXTSOU = HOST
			+ "/meilisannong/server/index.php/category_interface/searcharticle?";
	// com.nxt.chat.util.HOST+"/json/12316ynt/weibo1.json";
	// ��Ϣ�ӿ�
	public final static String MSG_URL = "json/xnbmsg.json";
	// ��������1Ϊר���ʴ� Ϊ�����ǹ�����Ϣ��2��Ϣͨ��3ũ�飬4΢����5�۸�
	public final static int PUBLIC_GQ = 0;
	public final static int PUBLIC_ZJWD = 1;
	public final static int PUBLIC_NQ = 3;
	public final static int PUBLIC_WEIBO = 4;
	public final static int PUBLIC_JG = 5;
	public static final String URL_LANMU_TIAOMU = "http://ahzw.6636.net/share/list.asp?";
	public static final String URL_XINXI_TIAOMU = "meilisannong/server/index.php/category_interface/getNewsAndPic?";
	//��Ϣͨ����
	public static final String URL_LIANBO=  HOST
			+ "/meilisannong/server/index.php/category_interface/getlianbolist?";
	//��Ϣͨר��
	public static final String URL_ZHUANLAN=  HOST
			+ "/meilisannong/server/index.php/category_interface/gethots?";
	public static final String URL_MYPUBLISH = "meilisannong/server/index.php/article_interface/getinfolist";
	public static String PINGLUN_URL = "meilisannong/server/index.php/article_interface/getcomments";
	public static final String contract_url = "meilisannong/server/index.php/user_interface/memberlist?";
	public static final String CONTACT_MSG = "meilisannong/server/index.php/user_interface/usernameserach?";

	// �޸��ǳ�
	public static String USEREDIT = HOST
			+ "/meilisannong/server/index.php/user_interface/newuseredit";
	// �ϴ����ѹ�ϵ
	public static final String UP_FRIENDS = "meilisannong/server/index.php/api_interface/uploadfriends";
	// ����ȫ��������Ϣ
	public static final String DOWN_FRIENDS = "meilisannong/server/index.php/api_interface/userfriendlist";
	// ���Ĺ��ں�,֪ͨ������
	public static final String DYGZH = "meilisannong/server/index.php/api_interface/addpublicuser";
	// ȡ�����ں�,֪ͨ������
	public static final String QXGZH = "meilisannong/server/index.php/api_interface/delpublicuser";
	//��ũ������ѯ
	public static final String YNDY ="http://219.232.243.58:81/yn.html";
	//����ִ��
	public static String XMZFURL = HOST+"/json/xmydzf/xmsyxzzf.json?";
	//������
	//	public static String XYW = "http://juapp.365960.com/mlcx/index.html";
	public static String XYW="http://m2.365960.com/newmlcx/index.php/mobile/cat";
	//�����Ҳ��
	public static String XMWCC="http://juapp.365960.com/xumu/index.shtml";
	//��ũ��
	public static String FX_JNB = "http://juapp.365960.com/m/index.html";
	//ׯ����
	public static String MY_XIU="http://m2.365960.com/meilisannong/comment/comments.php?sort=13";
	//ũ������
	public static String FX_NJZS = "http://juapp.365960.com/nongji/index.html";
	//�������˵
	public static String JNB_FBDJS="http://m2.365960.com/meilisannong/server/index.php/comment/postsay";
	//������ҳ
	//	public static String FX_GOUWU="http://t3.365960.com/app";
	public static String FX_GOUWU="http://t3.365960.com/jnb/user_nx.php";
	//�ҵľ�ũ��
	public static String MY_JNB="http://m2.365960.com/meilisannong/server/index.php/api_v2/zhongchoulist?phone=";
	//ʵʱ��Ƶjson�ļ�
	//	public static String rtspjson="http://219.232.243.58:83/json/junongbao/video/";
	public static String rtspjson="http://219.232.243.58:83/json/junongbao/cam/";
	//ɨ�빺
	public static String GW_saoma="http://t3.365960.com/jnb/goods_m.php?id=";
	//΢�Żص�
	public static String WX_PayEntry="http://t3.365960.com/jnb/respond.php";
	//��ũ����Ƶ��ػ�ȡ�б�
	public static String JNB_RTSP="http://www.agrim2m.cn/interface.php?";

	//�汾����
	public static String UPLOAD_VERSION="http://219.232.243.58:81/andriod/jnb_version.txt";

	//�������
	public static String GOUWU_FENLEI="http://t3.365960.com/jnb/cat_all.php";
	//��������
	public static String GOUWU_SOUSUO="http://t3.365960.com/jnb/search.php";
	//���ﳵ
	public static String GOUWU_GWC="http://t3.365960.com/jnb/flow.php";
	//�ҵĶ���
	public static String GOUWU_DINGDAN="http://t3.365960.com/jnb/user.php";
	//�����Ҳ���µ������б�
	public static String NEWS_URL="http://m2.365960.com/meilisannong/server/index.php/category_interface/getDetail_1?articleid=";

	//��ũ����¼
	public static String JINNB_LOGIN="http://wxjin.365960.cn/index.php/WxApp/login";

	//�ϱ�����
	public static String UPLOCATION="http://m2.365960.com/meilisannong/server/index.php/api_v2/userlocation";
	//�õ��û�ͷ��
	public static String getHeadUri(String headkey){
		String uri=DOWNLOAD_IMAGE+headkey;
		return uri;
	}
	/**
	 * ��ά����������
	 * 
	 * @author ������
	 * @version 1.0��2013-03-13
	 * @since 1.0
	 */
	public static final class ResultType {
		public static final int ADDRESSBOOK = 0x001;
		public static final int EMAIL_ADDRESS = 0x002;
		public static final int PRODUCT = 0x003;
		public static final int URI = 0x004;
		public static final int WIFI = 0x005;
		public static final int GEO = 0x006;
		public static final int TEL = 0x007;
		public static final int SMS = 0x008;
		public static final int CALENDAR = 0x009;
		public static final int ISBN = 0x010;
		public static final int TEXT = 0x011;
	}
}
