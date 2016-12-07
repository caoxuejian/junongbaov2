package com.nxt.ynt.map;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.nxt.jnb.R;
import com.nxt.nxtapp.common.Util;
import com.nxt.ynt.SoftApplication;

public class RouteActivity extends Activity implements OnMapClickListener,
OnGetRoutePlanResultListener,OnClickListener{

	private Context context = RouteActivity.this;
	private EditText startEt, endEt;
	private TextView titleTv, routeTv;
	public ImageView routeIv;

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mClient;
	private MyLocationListener myListener = new MyLocationListener();
	// 搜索相关
	private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	public RouteLine route = null;
	private boolean isFirstLoc = true;// 是否首次定位

	private String title, address, Addcur;
	private double Latitude, Longitude, Lat, Lng;
	private String lat,lng;
	private boolean useDefaultIcon = false;
	public OverlayManager routeOverlay = null;

	private boolean isBaidu, isGaode;
	private Util util;
	private ImageView iv_back;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.activity_route);
		initView();
		initData();
	}
	protected void initView() {
		isBaidu = isAvilible(this, "com.baidu.BaiduMap");// 判断是否有百度地图
		isGaode = isAvilible(this, "com.autonavi.minimap");// 判断是否有高德地图
		startEt = (EditText) findViewById(R.id.RouteLineTv);
		endEt = (EditText) findViewById(R.id.tvRouteTime);
		routeIv = (ImageView) findViewById(R.id.route_dh);
		routeIv.setOnClickListener(this);
		routeTv = (TextView) findViewById(R.id.go_des);
		titleTv = (TextView) findViewById(R.id.category_title);
		iv_back=(ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 地图的点击处理
		mBaiduMap.setOnMapClickListener(this);
		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
	}

	protected void initData() {
		util = new Util(context);
		title = getIntent().getStringExtra("title");
		address = getIntent().getStringExtra("address");
		Latitude = getIntent().getDoubleExtra("lat", 0.0);
		Longitude = getIntent().getDoubleExtra("lng", 0.0);
//		lat =util.getFromSp("lat", "");
//		lng = util.getFromSp("long", "");
		Lat = Double.parseDouble(util.getFromSp("lat", lat));
		Lng = Double.parseDouble(util.getFromSp("long", lng));

		Addcur = util.getFromSp("address", "");
		startEt.setText(Addcur);
		endEt.setText(address);
		routeTv.setText(title);
		titleTv.setText(title);
		init();
		setLocation();
	}



	/**
	 * 删除百度地图Logo,缩放控件,比例尺
	 */
	private void init() {
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ImageView) {
				// 百度地图logo是ImageView的实例
				child.setVisibility(View.GONE);
			}
		}
		// 删除比例尺
		mMapView.showScaleControl(false);
		// 删除缩放控件
		mMapView.showZoomControls(false);
	}

	private boolean isAvilible(RouteActivity context, String packageName) {// 获取packagemanager
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		// 用于存储所有已安装程序的包名
		List<String> packageNames = new ArrayList<String>();
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (packageInfos != null) {
			for (int i = 0; i < packageInfos.size(); i++) {
				String packName = packageInfos.get(i).packageName;
				packageNames.add(packName);
			}
		}
		// 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
		return packageNames.contains(packageName);
	}

	/**
	 * 调用百度地图
	 */
	private void startNavi() {

		if (isBaidu && isGaode) {
			LatLng pt1 = new LatLng(Lat, Lng);
			LatLng pt2 = new LatLng(Latitude, Longitude);

			RouteParaOption para = new RouteParaOption().startPoint(pt1)
					.endPoint(pt2).endName(Addcur).cityName(address);
			try {
				BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, this);
			} catch (BaiduMapAppNotSupportNaviException e) {
				e.printStackTrace();
			}
		} else {
			showDialog();
		}
	}

	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				OpenClientUtil.getLatestBaiduMapApp(RouteActivity.this);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 定位当前位置
	 */
	private void setLocation() {
		mClient = new LocationClient(this);
		mClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mClient.setLocOption(option);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mClient.start();
		mClient.requestLocation();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
			.accuracy(location.getRadius()).direction(100)
			.latitude(Lat).longitude(Lng).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(Lat, Lng);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

	/**
	 * 发起线路规划搜索实例
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v) {
		// 重置浏览节点的路线数据
		route = null;
		mBaiduMap.clear();
		// 设置起终点信息，对于tranist search 来说，城市名无意义
		PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", startEt
				.getText().toString());
		PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", endEt
				.getText().toString());

		LatLng latLngLoc = new LatLng(Lat, Lng);
		LatLng latLngNj = new LatLng(Latitude, Longitude);
		stNode = PlanNode.withLocation(latLngLoc);
		enNode = PlanNode.withLocation(latLngNj);
		// 实际使用中请对起点终点城市进行正确的设定
		if (v.getId() == R.id.rbCarTime) {
			Toast.makeText(RouteActivity.this, "正在创建路线，请耐心等待...",
					Toast.LENGTH_SHORT).show();
			mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
					.to(enNode));
		} else if (v.getId() == R.id.rbBusTime) {
			Toast.makeText(RouteActivity.this, "正在创建路线，请耐心等待...",
					Toast.LENGTH_SHORT).show();
			mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
					.city("北京").to(enNode));
		} else if (v.getId() == R.id.rbWalkTime) {
			Toast.makeText(RouteActivity.this, "正在创建路线，请耐心等待...",
					Toast.LENGTH_SHORT).show();
			mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode)
					.to(enNode));
		} /*
		 * else if (v.getId() == R.id.bike) { mSearch.bikingSearch((new
		 * BikingRoutePlanOption()) .from(stNode).to(enNode)); }
		 */
	}

	@Override
	public void onMapClick(LatLng arg0) {
		mBaiduMap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		Log.d("TAG", "result ::====1111=== " + result);
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
			.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
			routeOverlay = overlay;
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		Log.d("TAG", "result ::====2222=== " + result);
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
			.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			route = result.getRouteLines().get(0);
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		Log.d("TAG", "result ::====3333=== " + result);
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
			.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			route = result.getRouteLines().get(0);
			WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyTransitRouteOverlay extends TransitRouteOverlay {

		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mSearch.destroy();
		mMapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onGetBikingRouteResult(BikingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetIndoorRouteResult(IndoorRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetMassTransitRouteResult(MassTransitRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id==R.id.route_dh)
			startNavi();
		else if(id==R.id.iv_back)
			finish();
	}
}
