
package com.nxt.ynt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.nxt.jnb.R;
import com.nxt.nxtapp.NXTAPPApplication;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.setting.GetHost;
import com.sina.weibo.sdk.openapi.models.Poi;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;


public class SoftApplication extends NXTAPPApplication {
	private static final String TAG = "SoftApplication";
	private List<Activity> mainActivity;
	/** "/data/data/<app_package>/files/error.log" */
	public static final String PATH_ERROR_LOG = File.separator + "data"
			+ File.separator + "data" + File.separator + "com.nongjiatong"
			+ File.separator + "error.log";
	/** ��ʶ�Ƿ���Ҫ�˳���Ϊtrueʱ��ʾ��ǰ��ActivityҪִ��finish()�� */
	private boolean need2Exit;
	public static String phoneNumber;
	public static String upcontent;
	public static String imei;
	public static boolean updateFlag = false;
	private static SoftApplication instance;
	//	public LocationClient mLocationClient;
	//	public MyLocationListener mMyLocationListener;
	public boolean m_bKeyRight = true;// key�Ƿ���ȷ
	public static String area;
	String province, city, district;
	private LocationClient mClient;
	public MyLocationListener myListener = new MyLocationListener();

	// ����ģʽ��ȡΨһ��MyApplicationʵ��
	public static SoftApplication getInstance() {
		if (null == instance) {
			instance = new SoftApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(this);
		LocationClientOption option = new LocationClientOption();
		//���������������Ϊtrue�����ܻ�ȡ��ǰ��λ����Ϣ
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("gcj02");//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		//int span = 1000;
		//option.setScanSpan(span);//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		mClient = new LocationClient(getApplicationContext());
		mClient.setLocOption(option);
		mClient.registerLocationListener(myListener);
		mClient.start();
		need2Exit = false;
		initImageLoader(getApplicationContext());
		/**
		 * X5�ں���ʹ��preinit�ӿ�֮�󣬶����״ΰ�װ�״μ���û��Ч��
		 * ʵ���ϣ�X5webview��preinit�ӿ�ֻ�ǽ�����webview��������ʱ�䣻
		 * ��ˣ��ֽ׶�Ҫ�������״ΰ�װ�״μ���X5�ںˣ�����Ҫ��X5�ں���ǰ��ȡ���ں˵ļ�������
		 */
		GetHost.host =getString(R.string.host);
		LogUtil.isSysoLog =this.getString(R.string.ifSysoLog);
		preinitX5WebCore();
	}
	private void preinitX5WebCore() {
		/*if (!QbSdk.isTbsCoreInited()) {// preinitֻ��Ҫ����һ�Σ�����Ѿ�����˳�ʼ������ô��ֱ�ӹ���view
			System.out.println("@@@@@@@@@@@@@false");
			QbSdk.preInit(this);
		}*/
		//�Ѽ�����tbs�ں���Ϣ���ϱ������������������ؽ������ʹ���ĸ��ںˡ�
		//TbsDownloader.needDownload(getApplicationContext(), false);

		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				Log.e("app", " onViewInitFinished is " + arg0);
			}

			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub

			}
		};
		QbSdk.setTbsListener(new TbsListener() {
			@Override
			public void onDownloadFinish(int i) {
				Log.d("app","onDownloadFinish");
			}

			@Override
			public void onInstallFinish(int i) {
				Log.d("app","onInstallFinish");
			}

			@Override
			public void onDownloadProgress(int i) {
				Log.d("app","onDownloadProgress:"+i);
			}
		});

		QbSdk.initX5Environment(getApplicationContext(),  cb);
	}


	public void setNeed2Exit(boolean bool) {
		need2Exit = bool;
	}

	public boolean need2Exit() {
		return need2Exit;
	}

	public List<Activity> MainActivity() {
		return mainActivity;
	}

	public void addActivity(Activity act) {
		if (mainActivity == null)
			mainActivity = new ArrayList<Activity>();
		mainActivity.add(act);
	}

	public void finishAll() {
		for (Activity act : mainActivity) {
			if (!act.isFinishing()) {
				act.finish();
			}
		}
		mainActivity = null;
		/*if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			mLocationClient = null;
		}*/
		System.exit(0);
	}
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		//���û���·��
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache");  
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
				.Builder(context)  
		.memoryCacheExtraOptions(480, 800) // max width, max height���������ÿ�������ļ�����󳤿�  
		.threadPoolSize(3)//�̳߳��ڼ��ص�����  
		.threadPriority(Thread.NORM_PRIORITY - 2)  
		.denyCacheImageMultipleSizesInMemory()  
		.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/�����ͨ���Լ����ڴ滺��ʵ��  
		.memoryCacheSize(2 * 1024 * 1024)    
		.discCacheSize(50 * 1024 * 1024)    
		.tasksProcessingOrder(QueueProcessingType.LIFO)  
		.discCacheFileCount(100) //������ļ�����  
		.discCache(new UnlimitedDiskCache(cacheDir))//�Զ��建��·��  
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
		.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)��ʱʱ��  
		.writeDebugLogs() // Remove for release app  
		.build();//��ʼ����  
		/*
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();*/
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\nʡ");
				sb.append(location.getProvince());
				sb.append("\n��");
				sb.append(location.getCity());
				sb.append("\n����");
				sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}

			if (mClient != null) {
				mClient.stop();
			}
			util = new Util(getApplicationContext());
			util.saveToSp("address", location.getAddrStr());
			util.saveToSp("city", location.getCity());
			util.saveToSp("lat",String.valueOf(location.getLatitude()));
			util.saveToSp("long", String.valueOf(location.getLongitude()));

			Log.d("TAG", "Lat :: ---1111--- " + location.getLatitude()
					+ "Lng :: --2222--" + location.getLongitude());
		}
	}

}
