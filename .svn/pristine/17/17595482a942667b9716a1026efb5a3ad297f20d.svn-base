package com.nxt.ynt.utils;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MeiJingPagerAdapter extends PagerAdapter {
	ShowView sView;
	private List<String> pics;

	public MeiJingPagerAdapter( List<String> pics, ShowView sView) {
		this.sView = sView;
		this.pics = pics;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		// com.nxt.nxtapp.common.LogUtil.syso("±¾´ÎÍ¼Æ¬µÄ³ß´ç£º"+list.size()+" arg1:"+arg1);
		((ViewPager) arg0).removeView((View) arg2);

	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public int getCount() {
		return pics.size();

	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		return sView.show(arg0, arg1);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	public interface ShowView {
		public Object show(View arg0, int arg1);
	}
}
