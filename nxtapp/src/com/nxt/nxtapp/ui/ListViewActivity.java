package com.nxt.nxtapp.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nxt.nxtapp.R;
import com.nxt.nxtapp.listview.PullDownListView;

public class ListViewActivity extends Activity  implements PullDownListView.OnRefreshListioner{

	private PullDownListView mPullDownView;
	private ListView mListView;
	private List<String> list = new ArrayList<String>();
	private MyAdapter adapter;
	private Handler mHandler = new Handler();
	private int maxAount = 20;//�������������ֵ
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviewtest);
    	mPullDownView = (PullDownListView) findViewById(R.id.sreach_list);
		mPullDownView.setRefreshListioner(this);
		mListView = mPullDownView.mListView;
		addLists(10);
	    adapter = new MyAdapter(this,list);	
	    mPullDownView.setMore(true);//��������true��ʾ���и�����أ�����Ϊfalse�ײ�������ʾ����
	    mListView.setAdapter(adapter);
    }
    
    private void addLists(int n){
    	 n += list.size();
    	 for(int i=list.size();i<n;i++){
	        list.add("ѡ��"+i);
	     }
    }
    
    /**
     * ˢ�£������list������Ȼ�����¼��ظ�������
     */
	public void onRefresh() {
		
		mHandler.postDelayed(new Runnable() {
			
			public void run() {
				list.clear();
				addLists(10);
				mPullDownView.onRefreshComplete();//�����ʾˢ�´�����ɺ������ļ���ˢ�½�������
				mPullDownView.setMore(true);//��������true��ʾ���и�����أ�����Ϊfalse�ײ�������ʾ����
				adapter.notifyDataSetChanged();	
				
			}
		}, 1500);
		
		
	}
	
	/**
	 * ���ظ��࣬��ԭ�������������������
	 */
	public void onLoadMore() {
		
		mHandler.postDelayed(new Runnable() {
			public void run() {
				addLists(5);//ÿ�μ�������������
				mPullDownView.onLoadMoreComplete();//�����ʾ���ظ��ദ����ɺ������ļ��ظ�����棨���ػ��������������ࣩ
				if(list.size()<maxAount)//�жϵ�ǰlist������ӵ������Ƿ�С�����ֵmaxAount������ô����ʾ���������ʾ
					mPullDownView.setMore(true);//��������true��ʾ���и�����أ�����Ϊfalse�ײ�������ʾ����
				else
					mPullDownView.setMore(false);
				adapter.notifyDataSetChanged();	
				
			}
		}, 1500);
	}

	 class MyAdapter extends BaseAdapter {

			private List<String> arrays = null;
			private Context mContext;
			private Button curDel_btn;
			private float x,ux;

			public MyAdapter(Context mContext, List<String> arrays) {
				this.mContext = mContext;
				this.arrays = arrays;

			}

			public int getCount() {
				return this.arrays.size();
			}

			public Object getItem(int position) {
				return null;
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(final int position, View view, ViewGroup arg2) {
				ViewHolder viewHolder = null;
				if (view == null) {
					viewHolder = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
					viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
					view.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) view.getTag();
				}			
				viewHolder.tvTitle.setText(this.arrays.get(position));
				return view;
			}

			final  class ViewHolder {
				TextView tvTitle;
			}
		}
}