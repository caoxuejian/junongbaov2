
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
	/** 标识是否需要退出。为true时表示当前的Activity要执行finish()。 */
	private boolean need2Exit;
	public static String phoneNumber;
	public static String upcontent;
	public static String imei;
	public static boolean updateFlag = false;
	private static SoftApplication instance;
	//	public LocationClient mLocationClient;
	//	public MyLocationListener mMyLocationListener;
	public boolean m_bKeyRight = true;// key是否正确
	public static String area;
	String province, city, district;
	private LocationClient mClient;
	public MyLocationListener myListener = new MyLocationListener();

	// 单例模式获取唯一的MyApplication实例
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
		//就是这个方法设置为true，才能获取当前的位置信息
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
		//int span = 1000;
		//option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		mClient = new LocationClient(getApplicationContext());
		mClient.setLocOption(option);
		mClient.registerLocationListener(myListener);
		mClient.start();
		need2Exit = false;
		initImageLoader(getApplicationContext());
		/**
		 * X5内核在使用preinit接口之后，对于首次安装首次加载没有效果
		 * 实际上，X5webview的preinit接口只是降低了webview的冷启动时间；
		 * 因此，现阶段要想做到首次安装首次加载X5内核，必须要让X5内核提前获取到内核的加载条件
		 */
		GetHost.host =getString(R.string.host);
		LogUtil.isSysoLog =this.getString(R.string.ifSysoLog);
		preinitX5WebCore();
	}
	private void preinitX5WebCore() {
		/*if (!QbSdk.isTbsCoreInited()) {// preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view
			System.out.println("@@@@@@@@@@@@@false");
			QbSdk.preInit(this);
		}*/
		//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
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
		//设置缓存路径
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache");  
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
				.Builder(context)  
		.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
		.threadPoolSize(3)//线程池内加载的数量  
		.threadPriority(Thread.NORM_PRIORITY - 2)  
		.denyCacheImageMultipleSizesInMemory()  
		.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
		.memoryCacheSize(2 * 1024 * 1024)    
		.discCacheSize(50 * 1024 * 1024)    
		.tasksProcessingOrder(QueueProcessingType.LIFO)  
		.discCacheFileCount(100) //缓存的文件数量  
		.discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径  
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
		.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
		.writeDebugLogs() // Remove for release app  
		.build();//开始构建  
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
				sb.append("\n省");
				sb.append(location.getProvince());
				sb.append("\n市");
				sb.append(location.getCity());
				sb.append("\n区县");
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
