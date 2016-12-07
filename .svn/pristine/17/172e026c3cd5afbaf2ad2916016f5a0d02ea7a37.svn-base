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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.nxt.ynt.entity.NewsRoot;
import com.nxt.ynt.utils.Util;

public class AddAreaSouSuoActivity extends Activity implements
		OnItemClickListener {
	private static String TAG = "AddAreaActivity";
	public static List<List<BasicNameValuePair>> childList = new ArrayList<List<BasicNameValuePair>>();
	private ListView lv;
	private EditText txtFind;
	private TextView title;
	private ImageView del;
	private List xian, shi, city, work;
	private List<BasicNameValuePair> list;
	private String type;
	private Util util;
	public static ArrayList<Area> worklist;
	private int intxt;
	public static MyAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.add_sou_area);
		title = (TextView) findViewById(R.id.title);
		DBUtils db = new DBUtils(this);
		util = new Util(this);
		type = getIntent().getStringExtra("type");
		txtFind = (EditText) findViewById(R.id.txtfind);
		if (type.equals("area")) {
			title.setText(getResources().getString(R.string.sel_area));
			xian = db.queryArea("ZAREANAME");
			shi = db.queryArea("ZPNAME");
			city = db.queryArea("ZWEATHERCITY");
			txtFind.setHint("请输入地区名称");
		} else if (type.equals("hy")) {
			title.setText(getResources().getString(R.string.sel_work));
			work = db.queryWork("ZFULLNAME");
			txtFind.setHint("请输入行业、品种名称");
		}
		lv = (ListView) findViewById(R.id.listfind);
		// lv.setVisibility(View.GONE);
		lv.setOnItemClickListener(this);
		lv.setAdapter(new ListAdapter(AddAreaSouSuoActivity.this));

		// selected = (ListView) findViewById(R.id.listview_work);
		// adapter = new MyAdapter();
		// com.nxt.nxtapp.common.LogUtil.syso(selected);
		// com.nxt.nxtapp.common.LogUtil.syso(adapter);
		// selected.setAdapter(adapter);

		del = (ImageView) findViewById(R.id.del);
		del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				txtFind.setText(null);
				del.setVisibility(View.GONE);
			}
		});

		txtFind.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s != null && !s.toString().equals("")) {
					lv.setAdapter(new ListAdapter(AddAreaSouSuoActivity.this, s
							.toString()));
					lv.setVisibility(View.VISIBLE);
					del.setVisibility(View.VISIBLE);
				} else {
					del.setVisibility(View.GONE);
					lv.setVisibility(View.GONE);
				}
			}
		});

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	protected class ListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context context;

		public ListAdapter(Context context, String strin) {
			mInflater = LayoutInflater.from(context);
			this.context = context;
			// 查询结果列表
			list = new ArrayList<BasicNameValuePair>();
			if (type.equals("area")) {
				// 查询匹配
				for (int i = 0; i < xian.size(); i++) {
					String str = (String) xian.get(i) + "——"
							+ (String) shi.get(i);
					if (str.indexOf(strin) >= 0) {
						list.add(new BasicNameValuePair("name", str));
					}
				}
			} else if (type.equals("hy")) {
				for (int i = 0; i < work.size(); i++) {
					String str = (String) work.get(i);
					if (str.indexOf(strin) >= 0) {
						list.add(new BasicNameValuePair("name", str));
					}
				}
			}
		}

		public ListAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
			this.context = context;
			// 查询结果列表
			list = new ArrayList<BasicNameValuePair>();
			if (type.equals("area")) {
				for (int i = 0; i < xian.size(); i++) {
					String str = (String) xian.get(i) + "——"
							+ (String) shi.get(i);
					list.add(new BasicNameValuePair("name", str));
				}
			} else if (type.equals("hy")) {
				for (int i = 0; i < work.size(); i++) {
					String str = (String) work.get(i);
					list.add(new BasicNameValuePair("name", str));
				}
			}
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.child, null);
			TextView title = (TextView) convertView.findViewById(R.id.child);
			title.setText(list.get(position).getValue());
			return convertView;
		}
	}

	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.news_view_back) {
			finish();
		}
	}

	public void onActionsButtonClick(View view) {
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		final String item = list.get(arg2).getValue();
		com.nxt.nxtapp.common.LogUtil.syso("item的值为。--====" + item);
		// TextView area;
		// TextView work;
		TextView area_pd;
		TextView work_pd;
		// String xian = item.substring(0, item.indexOf("——"));
		// String shi = (item.substring(item.lastIndexOf("——") + 2).trim());
		if (type.equals("area")) {
			// area = RegisterActivity.area;
			area_pd = PersonalDetailsActivity.areaname;
			if (area_pd != null)
				post(type, item, area_pd);
			// if (area != null)
			// area.setText(item);
		} else if (type.equals("hy")) {
			// work = RegisterActivity.work;
			work_pd = PersonalDetailsActivity.workname;
			if (work_pd != null)
				post(type, item, work_pd);
			// if (work != null)
			// work.setText(item);
		}

		Intent intent = getIntent();
		setResult(1, intent);
		finish();
	}

	public void post(final String type, final String value, final TextView tv) {
		RequestParams params = new RequestParams();
		// params.put("uid", util.getFromSp(Util.UID, null));
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
						LogUtil.LogI(TAG, "rootdata:" + rootdata);
						String error = rootdata.getError();
						if (error.equals("0")) {
							util.saveToSp(type, value);
							Toast.makeText(AddAreaSouSuoActivity.this,
									rootdata.getMsg(), 0).show();
							tv.setText(value);
						} else {
							Toast.makeText(AddAreaSouSuoActivity.this,
									rootdata.getMsg(), 0).show();
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
			super(AddAreaSouSuoActivity.this, R.layout.area_listview_item,
					worklist);
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
			// util.saveToSp("xian" + intxt, worklist.get(position).getXian()
			// .toString());
			// util.saveToSp("city" + intxt, worklist.get(position).getCity());
			label.setText(worklist.get(position).getXian().toString());
			ImageView imageView = (ImageView) row.findViewById(R.id.img);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					com.nxt.nxtapp.common.LogUtil.LogD(TAG, "intxt=" + intxt);// intxt=position的最大值
					com.nxt.nxtapp.common.LogUtil.LogD(TAG, "xian=" + position);
					Dialog dialog = new AlertDialog.Builder(
							AddAreaSouSuoActivity.this)
							.setTitle("删除关注行业")
							.setMessage("确认删除该关注行业么?")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// util.delData("xian" + position +
											// 1);
											// util.delData("city" + position +
											// 1);
											worklist.remove(position);
											// str.remove(position);
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
