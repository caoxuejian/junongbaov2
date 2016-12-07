package com.nxt.ynt;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.nxt.jnb.R;
import com.nxt.nxtapp.common.Util;
import com.nxt.ynt.adapter.HorizontalListViewAdapter;
import com.nxt.ynt.entity.LivesInfo;
import com.nxt.ynt.map.PoiOverlay;
import com.nxt.ynt.map.RouteActivity;
import com.nxt.ynt.view.HorizontalListView;

public class LivesActivity extends FragmentActivity implements
OnGetPoiSearchResultListener, OnInfoWindowClickListener,OnClickListener {

	private Context context = LivesActivity.this;
	private HorizontalListView hListView;
	private HorizontalListViewAdapter hListViewAdapter;
	private List<LivesInfo> info;

	// poi检索
	private PoiSearch poi;
	private BaiduMap mBaiduMap = null;
	private Util util;
	private String city, name, address, phone;
	private double lat, lng;

	private String title;
	private String[] content;
	private int load_Index = 0;
	private RelativeLayout marker;
	private TextView category_title;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.activity_lives);
		initView();
		util = new Util(context);
		city = util.getFromSp("city", "");
		title = getIntent().getStringExtra("title");
		category_title.setText(title);
	}
	protected void initView() {

		marker = (RelativeLayout) findViewById(R.id.marker);
		category_title=(TextView)findViewById(R.id.category_title);
		iv_back=(ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		hListView = (HorizontalListView) findViewById(R.id.horizontal_listView);

		// one 创建检索实例
		poi = PoiSearch.newInstance();
		// three 设置POI检索监听者
		poi.setOnGetPoiSearchResultListener(this);
		mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager()
				.findFragmentById(R.id.mapView))).getBaiduMap();
		content = getIntent().getStringArrayExtra("content");

		hListViewAdapter = new HorizontalListViewAdapter(
				getApplicationContext(), content);
		hListView.setAdapter(hListViewAdapter);

		hListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				load_Index++;
				poisearch(content[arg2]);
			}
		});
	}

	private void poisearch(String titles) {
		/**
		 * four 发起检索请求
		 */
		poi.searchInCity(new PoiCitySearchOption().city(city).keyword(titles)
				.pageNum(load_Index));
	}

	/**
	 * five 释放检索实例
	 */
	@Override
	protected void onDestroy() {
		poi.destroy();
		super.onDestroy();
	}


	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			// PoiInfo类是poi信息类
			PoiInfo poiInfo = getPoiResult().getAllPoi().get(index);
			poi.searchPoiDetail((new PoiDetailSearchOption())
					.poiUid(poiInfo.uid));
			name = poiInfo.name;
			address = poiInfo.address;
			phone = poiInfo.phoneNum;
			lat = poiInfo.location.latitude;
			lng = poiInfo.location.longitude;
			info = new ArrayList<LivesInfo>();
			info.add(new LivesInfo(name, address, phone));
			Log.d("TAG", "lives :: ====== " + info);
			return true;
		}
	}

	/**
	 * 获取详细信息
	 */
	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {

		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
		} else {
			/*LogUtils.showMsg(context,
					result.getName() + ": " + result.getAddress() + ": "
							+ result.getTelephone());*/
			String Name = result.getName();
			String Address = result.getAddress();
			String Tel = result.getTelephone();
			info.add(new LivesInfo(Name, Address, Tel));
			Log.d("TAG", "marker :: ==========" + info.get(info.size() - 1));
			LivesInfo Info = info.get(info.size() - 1);
			popupInfo(marker, Info);
			marker.setVisibility(View.VISIBLE);
		}
	}

	private void popupInfo(RelativeLayout marker, final LivesInfo info) {
		if (marker == null || info == null) {
			return;
		}
		ViewHolder vh = null;
		if (marker.getTag() == null) {
			vh = new ViewHolder();
			vh.title = (TextView) marker.findViewById(R.id.marker_tvTitle);
			vh.addrss = (TextView) marker.findViewById(R.id.marker_tvAddress);
			vh.tel = (TextView) marker.findViewById(R.id.marker_tvTel);
			vh.ivPhone = (ImageView) marker.findViewById(R.id.marker_ivTel);
			vh.close = (ImageView) marker.findViewById(R.id.marker_close);
			vh.marker = (RelativeLayout) marker.findViewById(R.id.marker);
			marker.setTag(vh);
		} else {
			vh = (ViewHolder) marker.getTag();
		}
		vh.title.setText(info.getTitle());
		vh.addrss.setText("地址：" + info.getAddress());
		vh.tel.setText("电话：" + info.getTel());
		vh.ivPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL);
				Uri data = Uri.parse("tel:" + info.getTel());
				intent.setData(data);
				startActivity(intent);
			}
		});
		vh.marker.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ii = new Intent(context, RouteActivity.class);
				ii.putExtra("title", name);
				ii.putExtra("address", address);
				ii.putExtra("lat", lat);
				ii.putExtra("lng", lng);
				startActivity(ii);
			}
		});
		vh.close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	class ViewHolder {
		ImageView ivPhone, close;
		TextView title, addrss, tel;
		RelativeLayout marker;
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			return;
		}
	}

	@Override
	public void onInfoWindowClick() {
		mBaiduMap.hideInfoWindow();
	}

	@Override
	public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(R.id.iv_back==id){
			finish();
		}
	}
}
