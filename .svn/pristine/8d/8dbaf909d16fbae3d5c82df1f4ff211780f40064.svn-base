package com.nxt.ynt;

/**
 * 地区、行业选择
 * @author 赵佳丽
 *
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nxt.jnb.R;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.ynt.database.DBUtils;
import com.nxt.ynt.entity.Area;
import com.nxt.ynt.entity.City_3;
import com.nxt.ynt.entity.DiQu;
import com.nxt.ynt.entity.NewsRoot;
import com.nxt.ynt.utils.Util;

public class AddAreaActivity extends Activity implements OnClickListener {
	private static String TAG = "AddAreaActivity";
	public static List<List<BasicNameValuePair>> childList = new ArrayList<List<BasicNameValuePair>>();
	private ListView lv;
	private TextView title;
	private static int PROVINCE = 0;
	private static int CITY = 1;
	private static int DISTRICT = 2;
	private ImageView cursor;// 动画图片
	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	private int one;// 页卡1 -> 页卡2 偏移量
	private int two;// 页卡1 -> 页卡3 偏移量
	private Animation animation = null;
	private List<BasicNameValuePair> list;
	private String type;
	private Util util;
	private City_3 city3;
	private DBUtils db;
	private List<DiQu> listDiqu = new ArrayList<DiQu>();
	public static ArrayList<Area> worklist;
	private int intxt;
	int last, current = 0;
	private TextView[] tvs = new TextView[3];
	private int[] ids = { R.id.diqu_province, R.id.diqu_city,
			R.id.diqu_district };
	private CityAdapter adapter;

	Handler handlerDiqu = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				adapter.clear();
				listDiqu = (List<DiQu>) msg.obj;
				adapter.addAll(listDiqu);
				adapter.update();
				break;
			case 1:
				adapter.clear();
				listDiqu = (List<DiQu>) msg.obj;
				adapter.addAll(listDiqu);
				adapter.update();
				break;
			case 2:
				adapter.clear();
				listDiqu = (List<DiQu>) msg.obj;
				adapter.addAll(listDiqu);
				adapter.update();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.add_area);
		title = (TextView) findViewById(R.id.title);
		db = new DBUtils(this);
		util = new Util(this);
		type = getIntent().getStringExtra("type");
		if (type.equals("area")) {
			title.setText(getResources().getString(R.string.sel_area));
			db.setHandler(handlerDiqu);
			viewInit_Diqu();
			initImage();
		} else if (type.equals("hy")) {
			title.setText(getResources().getString(R.string.sel_work));
		}
	}

	private void initImage() {
		cursor = (ImageView) findViewById(R.id.cursor_diqu);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		bmpW = screenW / 3;
		offset = (screenW / 3 - bmpW) / 3;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
		one = offset * 3 + bmpW;// 页卡1 -> 页卡2 偏移量
		two = one * 2;// 页卡1 -> 页卡3 偏移量
	}

	private void viewInit_Diqu() {
		title.setText("选择地区");
		lv = (ListView) findViewById(R.id.diqu_list);
		adapter = new CityAdapter(this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(onItemClickListener);
		for (int i = 0; i < tvs.length; i++) {
			tvs[i] = (TextView) findViewById(ids[i]);
			tvs[i].setOnClickListener(this);
		}
		if (city3 == null) {
			city3 = new City_3("", "", "");
		} else {
			if (city3.getSheng() != null && !city3.getSheng().equals("")) {
				tvs[0].setText(city3.getSheng());
			}
			if (city3.getShi() != null && !city3.getShi().equals("")) {
				tvs[1].setText(city3.getShi());
			}
			if (city3.getXian() != null && !city3.getXian().equals("")) {
				tvs[2].setText(city3.getXian());
			}
		}
		tvs[0].setTextColor(getResources().getColor(
				R.color.text_color_pressed_msg));
		db.initProvince();
	}

	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (current == PROVINCE) {
				String newProvince = listDiqu.get(arg2).getName();
				if (!newProvince.equals(city3.getSheng())) {
					city3.setSheng(newProvince);
					city3.setShengId(listDiqu.get(arg2).getId());
					city3.setShi("");
					city3.setShiId("");
					city3.setXian("");
					tvs[0].setText(listDiqu.get(arg2).getName());
					tvs[1].setText("市");
					tvs[2].setText("区 ");
				}
				animation = new TranslateAnimation(offset, one, 0, 0);
				current = 1;
				// 点击省份列表中的省份就初始化城市列表
				db.initCities(city3.getShengId());
			} else if (current == CITY) {
				String newCity = listDiqu.get(arg2).getName();
				if (!newCity.equals(city3.getShi())) {
					city3.setShi(newCity);
					tvs[1].setText(listDiqu.get(arg2).getName());
					city3.setShiId(listDiqu.get(arg2).getId());
					tvs[2].setText("区 ");
				}
				animation = new TranslateAnimation(one, two, 0, 0);
				// 点击城市列表中的城市就初始化区县列表
				db.initDistricts(city3.getShiId());
				current = 2;
			} else if (current == DISTRICT) {
				current = 2;
				city3.setXian(listDiqu.get(arg2).getName());
				tvs[2].setText(listDiqu.get(arg2).getName());
				animation = new TranslateAnimation(two, two, 0, 0);
			}
			tvs[last].setTextColor(getResources().getColor(
					R.color.text_color_default_msg));
			tvs[current].setTextColor(getResources().getColor(
					R.color.text_color_pressed_msg));
			last = current;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}
	};

	class CityAdapter extends ArrayAdapter<DiQu> {
		LayoutInflater inflater;

		public CityAdapter(Context context) {
			super(context, 0);
			inflater = LayoutInflater.from(AddAreaActivity.this);
		}

		@Override
		public View getView(int arg0, View v, ViewGroup arg2) {
			v = inflater.inflate(R.layout.child, null);
			TextView tv_city = (TextView) v.findViewById(R.id.child);
			tv_city.setText(getItem(arg0).getName());
			return v;
		}

		public void update() {
			this.notifyDataSetChanged();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			finish();
		}
	}

	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.news_view_back) {
			finish();
		} else if (id == R.id.btn_wancheng) {
			String diqus;
			if (current == 1 && listDiqu.size() != 0) {
				Toast.makeText(AddAreaActivity.this, "您还没有选择城市！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			LogUtil.LogE(TAG, current + "       " + listDiqu.size()
					+ "         " + tvs[2].getText());
			if (current == 2 && listDiqu.size() != 0
					&& tvs[2].getText().toString().trim().equals("区")) {
				Toast.makeText(AddAreaActivity.this, "您还没有选择区县！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (city3.getShi().equals("") || city3.getShi() == null) {
				diqus = city3.getSheng();
			} else if (city3.getXian().equals("") || city3.getXian() == null) {
				diqus = city3.getSheng() + "——" + city3.getShi();
			} else {
				diqus = city3.getSheng() + "——" + city3.getShi() + "——"
						+ city3.getXian();
			}
			TextView area_pd;
			area_pd = PersonalDetailsActivity.areaname;
			post(type, diqus, area_pd);
			finish();
		} else if (id == R.id.btn_sou) {
			com.nxt.nxtapp.common.LogUtil.syso("点  搜索  按钮之后/..///////////////////");
			Intent area = new Intent(this, AddAreaSouSuoActivity.class);
			area.putExtra("type", "area");
			startActivityForResult(area, 0);
		}
		if (v.getId() == ids[0] || v.getId() == ids[1] || v.getId() == ids[2]) {
			if (ids[0] == v.getId()) {
				if (current == 1) {
					animation = new TranslateAnimation(one, offset, 0, 0);
				} else if (current == 2) {
					animation = new TranslateAnimation(two, offset, 0, 0);
				}
				current = 0;
				db.initProvince();
				tvs[last].setTextColor(getResources().getColor(
						R.color.text_color_default_msg));
				tvs[current].setTextColor(getResources().getColor(
						R.color.text_color_pressed_msg));
				last = current;
			} else if (ids[1] == v.getId()) {
				if (city3.getSheng() == null || city3.getSheng().equals("")) {
					current = 0;
					Toast.makeText(AddAreaActivity.this, "您还没有选择省份",
							Toast.LENGTH_SHORT).show();
					return;
				}
				com.nxt.nxtapp.common.LogUtil.syso("current = " + current);
				if (current == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
					com.nxt.nxtapp.common.LogUtil.syso("offset, one");
				} else if (current == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					com.nxt.nxtapp.common.LogUtil.syso("two, one");
				}
				db.initCities(city3.getShengId());
				current = 1;
				tvs[last].setTextColor(getResources().getColor(
						R.color.text_color_default_msg));
				tvs[current].setTextColor(getResources().getColor(
						R.color.text_color_pressed_msg));
				last = current;
			} else if (ids[2] == v.getId()) {
				if (city3.getSheng() == null || city3.getSheng().equals("")) {
					Toast.makeText(AddAreaActivity.this, "您还没有选择省份",
							Toast.LENGTH_SHORT).show();
					current = 0;
					db.initProvince();
					return;
				} else if (city3.getShi() == null || city3.getShi().equals("")) {
					Toast.makeText(AddAreaActivity.this, "您还没有选择城市",
							Toast.LENGTH_SHORT).show();
					current = 1;
					db.initCities(city3.getShengId());
					return;
				}
				if (current == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (current == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else if (current == 2) {
					animation = new TranslateAnimation(two, two, 0, 0);
				}
				current = 2;
				db.initDistricts(city3.getShiId());
				tvs[last].setTextColor(getResources().getColor(
						R.color.text_color_default_msg));
				tvs[current].setTextColor(getResources().getColor(
						R.color.text_color_pressed_msg));
				last = current;
			}
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}
	}

	public void onActionsButtonClick(View view) {
		finish();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		final String item = list.get(arg2).getValue();
		com.nxt.nxtapp.common.LogUtil.syso(item);
		TextView area_pd;
		TextView work_pd;
		if (type.equals("area")) {
			area_pd = PersonalDetailsActivity.areaname;
			if (area_pd != null)
				post(type, item, area_pd);
		} else if (type.equals("hy")) {
			work_pd = PersonalDetailsActivity.workname;
			if (work_pd != null)
				post(type, item, work_pd);
		}
		finish();
	}

	public void post(final String type, final String value, final TextView tv) {
		RequestParams params = new RequestParams();
		params.put(type, value);
		NxtRestClient.post(
				"meilisannong/server/index.php/user_interface/newuseredit",
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						LogUtil.LogI(TAG, content);
						// Json数据解析实例
						NewsRoot rootdata = (NewsRoot) JsonPaser
								.getObjectDatas(NewsRoot.class, content);
						if(rootdata!=null){
							LogUtil.LogI(TAG, "rootdata:" + rootdata);
							String error = rootdata.getError();
							if (error.equals("0")) {
								util.saveToSp(type, value);
								Toast.makeText(AddAreaActivity.this,
										rootdata.getMsg(), 0).show();
								tv.setText(value);
							} else {
								Toast.makeText(AddAreaActivity.this,
										rootdata.getMsg(), 0).show();
							}
						}else{
							Toast.makeText(AddAreaActivity.this, "修改失败请选择地区后重试！", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						LogUtil.LogI(TAG, "onFailure：" + content);
					}
				});
	}

	class MyAdapter extends ArrayAdapter<Area> {
		MyAdapter() {
			super(AddAreaActivity.this, R.layout.area_listview_item, worklist);
		}

		public ArrayList<Area> getList() {
			return worklist;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			intxt = position + 1;
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.area_listview_item, parent,
						false);
			}
			TextView label = (TextView) row.findViewById(R.id.title);
			label.setText(worklist.get(position).getXian().toString());
			ImageView imageView = (ImageView) row.findViewById(R.id.img);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					com.nxt.nxtapp.common.LogUtil.LogD(TAG, "intxt=" + intxt);// intxt=position的最大值
					com.nxt.nxtapp.common.LogUtil.LogD(TAG, "xian=" + position);
					Dialog dialog = new AlertDialog.Builder(
							AddAreaActivity.this)
							.setTitle("删除关注行业")
							.setMessage("确认删除该关注行业么?")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											worklist.remove(position);
											MyAdapter.this
													.notifyDataSetChanged();
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
				}
			});
			return (row);
		}
	}

}
