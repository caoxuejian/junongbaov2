package com.nxt.ynt.widget;

import com.nxt.nxtapp.setting.GetHost;

public class Constans {
	public static boolean DEBUG = true;
	public static int onSelectId = 100;
	public static final int SELECT_SANNONG = 0;
	public static final int SELECT_ZHENGCE = 1;
	public static final int SELECT_NONGYE = 2;
	public static final int SELECT_SEHNGHUO = 3;
	public static final int SELECT_CHUANGYE = 4;
	public static final int SELECT_SHICHANG = 5;
	public static final int SELECT_MEILI = 6;
	public static final int SELECT_ZHUANGJIA = 7;
	public static final String SITE_ID = "3739";
	public static final String ROOT_URL = com.nxt.nxtapp.setting.GetHost
			.getHost() + "/meilisannong/server/";
	//http://221.131.123.58:10080/wuxi/interface.php?
	//查看
	public static final String CHUANGAN_URL = "http://121.43.233.90:8069/html/interface.php?";
	//详情
	public static final String CHUANGAN_XQ = "http://121.43.233.90:8069/html/listshow.php?";
	
	//
	public static final String WULIAN_URL = "http://221.131.123.58:10080/wuxi/interface.php?";
	public static final String WULIAN = "http://wuxi.6636.net:10080/wuxi/html5-1/html/list.php?";
	public static final String GONGSHI_URL = "http://gs.6636.net/wss/wss_interface.php?";//工时管理登录接口
	public static final String GONGSHIZHENGSHI_URL = "http://gs.6636.net/mobile/sjgsa.php?";//工时管理正式接口
	public static final String WULIAN_URL_PH = "http://221.131.123.58:10080/wuxi/interface_t.php?";
	public static final String XMZFLOGIN_URL = "http://192.168.0.224:1080/svn/mobile/branch/release_1_0/json/xmydzf/login.json";
	public static final String JYCZLOGIN_URL = "http://sync.sdahi.com:9090/dongjian/isv/appAuthEngine/spGateWay?";
	//河南执法案件管理                                                                                   //http://hnws.agridoor.com.cn/xmws/isv/appAuthEngine/spGateWay  http://219.232.243.58:81/json/xmydzf/hnxmsyajgl.json
	public static final String AJGLOGINURL ="http://xmws.hnahi.org.cn/xmws/isv/appAuthEngine/spGateWay?";
	public static final String AJGLURL ="http://219.232.243.58:81/json/xmydzf/hnxmsyajgl.json";
	public static final String AJGLURL_TEST_FROM ="http://219.232.243.58:81/json/xmydzf/testfrom.json";
	public static final String AJZFLOGIN_URL = "http://nyjg.prod.agridoor.com.cn:88/hhnnyjg/integrated/_appEmbed/login?__globalAppEmbedVer=NX.1.01";
	public static final String JYCZZY = "http://219.232.243.58:81/json/12316xnb/jycz.json";
	//河南执法监督                                                                               
	public static final String HNZHZF = "http://xmzf.agridoor.com.cn/zf/admin/isv/appAuthEngine/spGateWay?";
	public static final String HNZHZFURL ="http://219.232.243.58:81/json/xmydzf/hnxmsyxzzfjd.json";
	public static final String[] tab_galls = new String[] { "三农资讯", "农业科技",
			"便民服务", "价格行情" };
	public static boolean updateFlag = true;
	public static final String SPF_DATE = "cachedate";
	public static final String SBZT = "shebzhuangtai";
	public static final String SPF_DATE_TIME = "lasttime";
	public static final String SPF_DATE_FLAG = "theflag";
	public static final String[] grid_item_names = new String[] { "三农资讯",
			"综合法规", "政策解读", "办事指南", "种植", "畜牧", "水果", "蔬菜", "行情价格", "农业专家",
			"传感物联" };
	public static final String[] grid_item_fabunames = new String[] { "动态",
			"供求", "价格", "农情", "专家问答" };
	public static boolean moreFlag = true;
	// 1、01 0F 00 50 00 01 01 01 2F 5B//增氧泵的远程控制
	// 反馈报文：01 0F 00 50 00 01 94 1A
	// 2、01 0F 00 50 00 01 01 00 EE 9B//增氧泵的自动控制
	// 反馈报文：01 0F 00 50 00 01 94 1A
	// 3、01 0F 00 51 00 01 01 01 12 9B//增氧泵的远程启动
	// 反馈报文：01 0F 00 51 00 01 C5 DA
	// 4、01 0F 00 51 00 01 01 00 D3 5B//增氧泵的远程停止
	// 反馈报文：01 0F 00 51 00 01 C5 DA
	public static final String WULIAN_YC_BW = "010F0050000101012F5B";
	public static final String WULIAN_ZD_BW = "010F005000010100EE9B";
	public static final String WULIAN_YC_ON_BW = "010F005100010101129B";
	public static final String WULIAN_YC_OFF_BW = "010F005100010100D35B";
	public static final String ZJK_URL = "meilisannong/server/index.php/api_interface/getzj";
	public static final String ZJK_URL1 = "meilisannong/server/index.php/api_interface/getzjtd";
	public static final String ZJK_URL2 = "meilisannong/server/index.php/article_interface/getzj?zy=";
	public static boolean TABLE_EXSIT = false;
	//河南畜牧执法案件
	public static String HNXMZFURL = GetHost.getHost() + "/json/xmydzf/hnxmsyxzzfgl.json?";
}
