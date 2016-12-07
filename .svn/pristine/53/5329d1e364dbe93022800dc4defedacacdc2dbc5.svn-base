package com.nxt.ynt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.ynt.entity.DetailsHYData;
import com.nxt.ynt.entity.HYDatas;
import com.nxt.ynt.entity.JiHuo;
import com.nxt.ynt.entity.LoginInfo;
import com.nxt.ynt.utils.Util;

public class GZHYActivity extends Activity implements OnClickListener {
	private ListView lv_group;
	private List<HYDatas> clist;
	private String TAG = "GZHYActivity";
	private Context context;
	private ArrayList<String> tagId;
	private ArrayList<String> tagName;
	private TextView tv_title;
	private Util util;
	private String hy;
	private String hyid;
	private TextView work_pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sorts_group);
		context = this;
		util = new Util(this);
		clist = new ArrayList<HYDatas>();
		tagId= new ArrayList<String>();
		tagName = new ArrayList<String>();
		lv_group = (ListView) this.findViewById(R.id.lv_sort);
		tv_title = (TextView) this.findViewById(R.id.category_title);
		tv_title.setText("关注行业");
		HttpCallBack callback = new HttpCallBack() {

			@Override
			public void onSuccess(String content) {
				// 在父类对content做了解密处理
				content = this.getContent(content);
				LogUtil.LogE(TAG, "content值 = " + content);
				String entity;
				// Json数据解析实例
				try {
					JSONObject job = new JSONObject(content);
					String sortList = job.getString("sortList");
					JSONArray job3 = new JSONArray(sortList);
					for (int i = 0; i < job3.length(); i++) {
						String sort = job3.getString(i);
						String sorts = job.getString(job3.getString(i));
						LogUtil.LogE(TAG, "sort===" + sort);
						HYDatas hydata = new HYDatas();
						hydata.setBigsort(sort);
						hydata.setLittlesort(sorts);
						clist.add(hydata);
					}
					lv_group.setAdapter(new MyGroupAdapter(context, clist));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LoginInfo rootdata = (LoginInfo) JsonPaser.getObjectDatas(
						LoginInfo.class, content);
				LogUtil.LogI(TAG, "rootdata:" + rootdata);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				Toast.makeText(context, "网络不给力啊，检查下网络或者再试一次吧！", 1).show();
			}
		};
		NxtRestClientNew.post("industryFetch", null, callback);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_back) {
			finish();
		} else if (id == R.id.iv_submit) {
			if (tagId.size() == 0) {
				Toast.makeText(context, "请选择行业分类", 1).show();
			} else {
				work_pd = PersonalDetailsActivity.workname;
				hyid=tagId.toString();
				hy=tagName.toString();
				Map<String,String> params = new HashMap<String, String>();
				params.put("hy", hyid.substring(1, hyid.length()-1));
				HttpCallBack callback = new HttpCallBack() {
					private JiHuo data;

					@Override
					public void onSuccess(String content) {
						// 在父类对content做了解密处理
						content = this.getContent(content);
						LogUtil.LogE(TAG, "content值 = " + content);
						data=(JiHuo) JsonPaser.getObjectDatas(JiHuo.class, content);
						if(data.getErrorcode().equals("0")){
							util.saveToSp("hy", hy.substring(1, hy.length()-1));
							util.saveToSp("hyid", hyid.substring(1, hyid.length()-1));
							Toast.makeText(context, "修改成功！", 1).show();
							work_pd.setText(hy.substring(1, hy.length()-1));
						}else{
							Toast.makeText(context, "修改失败，请重试！", 1).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						Toast.makeText(context, "网络不给力啊，检查下网络或者再试一次吧！", 1).show();
					}
				};
				NxtRestClientNew.post("useredit", params, callback);
				finish();
			}
		}
	}
	class MyGroupAdapter extends BaseAdapter {
		private Context context;
		private List<HYDatas> clist;

		public MyGroupAdapter(Context context, List<HYDatas> clist) {
			this.context = context;
			this.clist = clist;
		}

		@Override
		public int getCount() {
			return clist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return clist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			final ViewHolder vh;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = mInflater.inflate(R.layout.sort_group_item, null);
				vh.tv_sort = (TextView) convertView.findViewById(R.id.tv_sort);
				vh.gv_sort = (GridView) convertView
						.findViewById(R.id.sort_gridview);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			HYDatas gs = clist.get(position);
			vh.tv_sort.setText(gs.getBigsort());
			List<DetailsHYData> datas = JsonPaser.getArrayDatas(
					DetailsHYData.class, gs.getLittlesort());
			GridViewAdapter mygridviewAdapter = new GridViewAdapter(context,
					datas);
			vh.gv_sort.setAdapter(mygridviewAdapter);
			return convertView;
		}

		public class ViewHolder {
			public TextView tv_sort;
			public GridView gv_sort;
		}
	}

	public class GridViewAdapter extends BaseAdapter {
		private Context mContext;
		public List<DetailsHYData> mList;
		private String util_work;
		private String[] work_id;

		public GridViewAdapter(Context mContext, List<DetailsHYData> mList) {
			super();
			this.mContext = mContext;
			this.mList = mList;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return this.mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(this.mContext).inflate(
						R.layout.gridview_item, null, false);
				holder.button = (Button) convertView
						.findViewById(R.id.gridview_item_button);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final String str_tagId = mList.get(position).getIndustryId();
			final String str_tagName = mList.get(position).getIndustryName();
			//判断是否是订阅
			util_work = util.getFromSp(Util.workid, "");
			if(!"".equals(util_work)){
				work_id=util_work.replaceAll(" ","").split(",");
				for(int i=0;i<work_id.length;i++){
					if(work_id[i].equals(str_tagId)){
						holder.button.setBackground(mContext.getResources()
								.getDrawable(R.drawable.group_tag_search_bg_click));
						tagId.add(str_tagId);
						tagName.add(str_tagName);
					}
				}
			}
			holder.button.setText(str_tagName);
			holder.button.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					if (tagId.size() == 3 && !tagId.contains(str_tagId)) {
						Toast.makeText(context, "最多只能选择三个分类", 1).show();
					} else {
						if (tagId.contains(str_tagId)) {
							holder.button.setBackground(mContext.getResources()
									.getDrawable(R.drawable.group_tag_bg_white));
							tagName.remove(tagId.indexOf(str_tagId));
							tagId.remove(str_tagId);
						} else {
							holder.button
							.setBackground(mContext
									.getResources()
									.getDrawable(
											R.drawable.group_tag_search_bg_click));
							tagId.add(str_tagId);
							tagName.add(str_tagName);
						}
					}
				}
			});
			return convertView;
		}

		private class ViewHolder {
			Button button;
		}
	}

}
