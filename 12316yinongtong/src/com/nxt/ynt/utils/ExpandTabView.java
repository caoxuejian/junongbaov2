package com.nxt.ynt.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nxt.jnb.R;

/**
 * 闁兼寧绮屽畷鐔煎箳瑜屽▎銏″緞閹绢喖鍔ラ柨娑樿嫰閻ㄦ繄鎲楅崨顒傚晩濞戞挸顑嗘刊娲礉閵娧勬毎闁挎稑鑻慨鈺呭箑娴ｇ儤鏅搁柟瀛樺姇閵囨棃鏌堥妸锕�鐦婚梺绛嬪枙闁叉粓鏁撻敓锟�
 * 
 * @author yueyueniao
 */

public class ExpandTabView extends LinearLayout implements OnDismissListener {

	private ToggleButton selectedButton;
	private ArrayList<String> mTextArray = new ArrayList<String>();
	private ArrayList<RelativeLayout> mViewArray = new ArrayList<RelativeLayout>();
	private ArrayList<ToggleButton> mToggleButton = new ArrayList<ToggleButton>();
	private Context mContext;
	private final int SMALL = 0;
	private int displayWidth;
	private int displayHeight;
	private PopupWindow popupWindow;
	private int selectPosition;

	public ExpandTabView(Context context) {
		super(context);
		init(context);
	}

	public ExpandTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 闁哄秷顬冨畵渚�鏌呮径瀣仴闁汇劌瀚紞鍛磾椤旀鍟庣紓鍐櫑abitem闁哄嫬澧介妵姘舵儍閸曡埖瀚�?
	 */
	public void setTitle(String valueText, int position) {
		if (position < mToggleButton.size()) {
			mToggleButton.get(position).setText(valueText);
		}
	}

	public void setTitle(String title) {

	}

	/**
	 * 闁哄秷顬冨畵渚�鏌呮径瀣仴闁汇劌瀚紞鍛磾椤斿灝绠柛娆愭桨abitem闁哄嫬澧介妵姘舵儍閸曡埖瀚�?
	 */
	public String getTitle(int position) {
		if (position < mToggleButton.size()
				&& mToggleButton.get(position).getText() != null) {
			return mToggleButton.get(position).getText().toString();
		}
		return "";
	}

	/**
	 * 閻犱礁澧介悿鍞梐bitem闁汇劌瀚柌婊堝极閺夋寧瀚查柛鎺撶箓椤﹪鏁撻敓锟�
	 */
	public void setValue(ArrayList<String> textArray, ArrayList<View> viewArray) {
		if (mContext == null) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mTextArray = textArray;
		for (int i = 0; i < viewArray.size(); i++) {
			final RelativeLayout r = new RelativeLayout(mContext);
			int maxHeight = (int) (displayHeight * 0.89);
//			int selfHeight=viewArray.get(i).getHeight();
//			int height=selfHeight>=maxHeight?maxHeight:selfHeight;
//			com.nxt.nxtapp.common.LogUtil.LogE("123","maxHeight=="+ maxHeight+"selfHeight=="+selfHeight+"height=="+height);
			RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT, maxHeight);
			rl.leftMargin = 10;
			rl.rightMargin = 10;
			r.addView(viewArray.get(i), rl);
			mViewArray.add(r);
			r.setTag(SMALL);
			ToggleButton tButton = (ToggleButton) inflater.inflate(
					R.layout.toggle_button, this, false);
			addView(tButton);
			View line = new TextView(mContext);
			line.setBackgroundResource(R.drawable.choosebar_line);
			if (i < viewArray.size() - 1) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(2,
						LinearLayout.LayoutParams.FILL_PARENT);
				addView(line, lp);
			}
			mToggleButton.add(tButton);
			tButton.setTag(i);
			tButton.setText(mTextArray.get(i));

			r.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onPressBack();
				}
			});

			r.setBackgroundColor(mContext.getResources().getColor(
					R.color.popup_main_background));
			tButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// initPopupWindow();
					ToggleButton tButton = (ToggleButton) view;

					if (selectedButton != null && selectedButton != tButton) {
						selectedButton.setChecked(false);
					}
					selectedButton = tButton;
					selectPosition = (Integer) selectedButton.getTag();
					startAnimation();
					if (mOnButtonClickListener != null && tButton.isChecked()) {
						mOnButtonClickListener.onClick(selectPosition);
					}
				}
			});
		}
	}

	private void startAnimation() {

		if (popupWindow == null) {
			popupWindow = new PopupWindow(mViewArray.get(selectPosition),
					displayWidth, displayHeight);
			popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
			popupWindow.setFocusable(false);
			popupWindow.setOutsideTouchable(true);
		}

		if (selectedButton.isChecked()) {
			if (!popupWindow.isShowing()) {
				showPopup(selectPosition);
			} else {
				popupWindow.setOnDismissListener(this);
				popupWindow.dismiss();
				hideView();
			}
		} else {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
				hideView();
			}
		}
	}

	private void showPopup(int position) {
		View tView = mViewArray.get(selectPosition).getChildAt(0);
		if (tView instanceof ViewBaseAction) {
			ViewBaseAction f = (ViewBaseAction) tView;
			f.show();
		}
		if (popupWindow.getContentView() != mViewArray.get(position)) {
			popupWindow.setContentView(mViewArray.get(position));
		}
		popupWindow.showAsDropDown(this, 0, 0);
	}

	/**
	 * 濠碘�冲�归悘澶愭嚕濠婂啫绀嬮柟瀛樺姇閻秹鏁撻敓浠嬫晸閺傘倖瀚归柟顑跨筏缁辨繈宕氬▎鎺濆敤闁兼寧绮屽畷鐔煎绩鐠虹儤绀�闁跨噦鎷�
	 */
	public boolean onPressBack() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			hideView();
			if (selectedButton != null) {
				selectedButton.setChecked(false);
			}
			return true;
		} else {
			return false;
		}

	}

	private void hideView() {
		View tView = mViewArray.get(selectPosition).getChildAt(0);
		if (tView instanceof ViewBaseAction) {
			ViewBaseAction f = (ViewBaseAction) tView;
			f.hide();
		}
	}

	private void init(Context context) {
		mContext = context;
		displayWidth = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay().getWidth();
		displayHeight = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay().getHeight();
		setOrientation(LinearLayout.HORIZONTAL);
	}

	@Override
	public void onDismiss() {
		showPopup(selectPosition);
		popupWindow.setOnDismissListener(null);
	}

	private OnButtonClickListener mOnButtonClickListener;

	/**
	 * 閻犱礁澧介悿鍞梐bitem闁汇劌瀚崑锝夊礄閼姐倖纾ч柛姘煎厸缁ㄣ劑鏁撻敓锟�
	 */
	public void setOnButtonClickListener(OnButtonClickListener l) {
		mOnButtonClickListener = l;
	}

	/**
	 * 闁煎浜滈悾鐐▕婵夌嘲bitem闁绘劗鎳撻崵顕�宕堕悙鍓佹闁规亽鍎辫ぐ锟�
	 */
	public interface OnButtonClickListener {
		public void onClick(int selectPosition);
	}

}
