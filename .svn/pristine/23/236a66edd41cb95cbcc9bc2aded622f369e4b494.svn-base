package com.nxt.ynt;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.nxt.ynt.utils.AsyncImageTask;
import com.nxt.ynt.utils.Constans;
import com.nxt.ynt.utils.DragImageView;
import com.nxt.ynt.utils.MeiJingPagerAdapter;
import com.nxt.ynt.utils.MeiJingPagerAdapter.ShowView;

public class MyViewPagerJHActivity extends Activity implements OnClickListener{
	/** Called when the activity is first created. */
	private int window_width, window_height;// 控件宽度
	private DragImageView dragImageView;// 自定义控件
	private int state_height;// 状态栏的高度
	private ViewTreeObserver viewTreeObserver;
	List<View> mViewArray;
	private int pager;
	private int MODE;
	private ImageView[] images;
	private List<String> pics = new ArrayList<String>();
	private Button btn_down;
	private PopupWindow pop;
	private ViewGroup viewGroup;
	private boolean isback=true;
	private ImageView downloadimg;
	private int page;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		SoftApplication appState = (SoftApplication)this.getApplication();
		appState.addActivity(this);
		getMyIntent();
		/** 获取可見区域高度 **/
		WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight() - 53;
		com.nxt.nxtapp.common.LogUtil.syso("获取可見区域高度 window_width:" + window_width
				+ " window_height:" + window_height);
		LayoutInflater inflater = getLayoutInflater();
		viewGroup = (ViewGroup) inflater.inflate(R.layout.viewpager, null);
		// group是R.layou.main中的负责包裹小圆点的LinearLayout.
		ViewGroup group = (ViewGroup) viewGroup.findViewById(R.id.viewGroup);
		ViewPager viewPager = (ViewPager) viewGroup.findViewById(R.id.viewPager);
		downloadimg=(ImageView)viewGroup.findViewById(R.id.downloadimg);
		downloadimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 new MYTask().execute(pics.get(page));
				
				 Toast.makeText(MyViewPagerJHActivity.this,"图片路径："+ Constans.NX_RECV_SAVE_PATH,Toast.LENGTH_LONG).show();
			}
		});
		images = new ImageView[pics.size()];
		// 圆点处理
		for (int i = 0; i < pics.size(); i++) {
			ImageView imageView = new ImageView(MyViewPagerJHActivity.this);
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			imageView.setPadding(0, 0, 10, 0);
			images[i] = imageView;
			if (i == pager) {
				// 默认进入程序后第一张图片被选中;
				page=i;
				images[i].setImageResource(R.drawable.selected);
			} else {
				images[i].setImageResource(R.drawable.unselect);
			}
			group.addView(images[i]);
		}

		setContentView(viewGroup);

		MeiJingPagerAdapter myPagerAdapter = new MeiJingPagerAdapter(pics,
				new ShowView() {

			

			@Override
			public Object show(View arg0, int arg1) {
				LayoutInflater mInflater = LayoutInflater
						.from(MyViewPagerJHActivity.this);

				View view = mInflater.inflate(
						R.layout.waterfall_image_big, null);

				dragImageView = (DragImageView) view
						.findViewById(R.id.image);
				dragImageView.setTag(pics.get(arg1));

				//						if(pics.get(arg1).contains("http://")){//有缓存直接加载
				//							dragImageView.setImageBitmap(pics.get(arg1).getBitmap());
				//						}else{
				AsyncImageTask task = new AsyncImageTask(new int[] {
						window_width, window_height });
				task.execute(dragImageView);
				//	}
				dragImageView.setmActivity(MyViewPagerJHActivity.this);// 注入Activity.
				/** 测量状态栏高度 **/
				viewTreeObserver = dragImageView.getViewTreeObserver();
				viewTreeObserver
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (state_height == 0) {
							// 获取状况栏高度
							Rect frame = new Rect();
							getWindow()
							.getDecorView()
							.getWindowVisibleDisplayFrame(
									frame);
							state_height = frame.top;
							com.nxt.nxtapp.common.LogUtil.syso("state_height:"
									+ state_height);
							dragImageView
							.setScreen_H(window_height
									- state_height);
							dragImageView
							.setScreen_W(window_width);
						}

					}
				});
				// imageView.setOnTouchListener(new
				// MulitPointTouchListener());
				// dragImageView.setOnClickListener(new
				// MyOnClickListener());
				view.setOnClickListener(new MyOnClickListener());
				/*dragImageView.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						System.out.println("@@@@@@@@@@长按下载");
						Toast.makeText(MyViewPagerJHActivity.this, "长按", Toast.LENGTH_LONG).show();
						return true;
					}
				});*/
				dragImageView.setOnTouchListener(new OnTouchListener() {

					private long time;
					private TimeCount timecount;

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						switch (event.getAction() & MotionEvent.ACTION_MASK) {
						/*case MotionEvent.ACTION_DOWN:
							isback=true;
							timecount = new TimeCount(1000, 1000);// 构造CountDownTimer对象
							timecount.start();
							//获取按下去时间
							time = System.currentTimeMillis();
							break;*/
						case MotionEvent.ACTION_UP://点击图片返回
							finish();
							/*timecount.cancel();
							if(isback){
								finish();
							}*/
							/*System.out.println("@@@@@@@@@@@@time!!"+(System.currentTimeMillis()-time));
							if((System.currentTimeMillis()-time)>=1000){
								initpowview();
								changePopupWindowState();
							}else{
								finish();
							}*/
							break;
						}
						return true;
					}
				});
				((ViewPager) arg0).addView(view, 0);
				return view;
			}
		});

		viewPager.setAdapter(myPagerAdapter);
		viewPager.setOnPageChangeListener(new MyListener());
		viewPager.setCurrentItem(pager);

	}


	//初始化弹出分享菜单
		private void initpowview() {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(this);
			// 引入窗口配置文件 - 即弹窗的界面
			View view = inflater.inflate(R.layout.menu_download, null);

			btn_down = (Button) view.findViewById(R.id.btn_download);
			
			btn_down.setOnClickListener(this);
			view.setFocusableInTouchMode(true);
			// PopupWindow实例化
			pop = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT, true);
			/**
			 * PopupWindow 设置
			 */
			// pop.setFocusable(true); //设置PopupWindow可获得焦点
			// pop.setTouchable(true); //设置PopupWindow可触摸
			// pop.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
			// 设置PopupWindow显示和隐藏时的动画
			pop.setAnimationStyle(R.style.MenuAnimationFade);
			/**
			 * 改变背景可拉的弹出窗口。后台可以设置为null。 这句话必须有，否则按返回键popwindow不能消失 或者加入这句话
			 * ColorDrawable dw = new
			 * ColorDrawable(-00000);pop.setBackgroundDrawable(dw);
			 */
			pop.setBackgroundDrawable(new BitmapDrawable());

		}
	
	private void getMyIntent() {
		Intent intent = getIntent();
		// list = intent.getStringArrayListExtra("list");
		// page = intent.getStringExtra("page");
		pager = intent.getIntExtra("page", 0);
		pics = intent.getStringArrayListExtra("img");
		//		Object[] objects = (Object[])intent.getSerializableExtra("pics");
		//		for(Object obj : objects){
		//			pics.add((Picture)obj);
		//		}
	}

	// back to listenr

	class BacktoClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}

	}

	// 页面滑动时
	class MyListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < images.length; i++) {
				//获取当前page页码
				page=arg0;
				images[i].setImageResource(R.drawable.selected);
				if (arg0 != i) {
					images[i].setImageResource(R.drawable.unselect);
				}
			}

		}

	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			// 动画效果
			// overridePendingTransition(R.anim.hyperspace_in,
			// R.anim.hyperspace_out);
			//overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id==R.id.btn_download){//下载图片
			isback=true;
			changePopupWindowState();
		}
	}
	/**
	 * 改变 PopupWindow 的显示和隐藏
	 */
	private void changePopupWindowState() {
		if (pop.isShowing()) {
			// 隐藏窗口，如果设置了点击窗口外消失，则不需要此方式隐藏
			pop.dismiss();
		} else {
			// 弹出窗口显示内容视图,默认以锚定视图的左下角为起点，这里为点击按钮
			pop.showAtLocation(viewGroup, Gravity.BOTTOM, 0, 0);
		}
	}
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			isback=false;
			initpowview();
			changePopupWindowState();
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
		}
	}
	public class MYTask extends AsyncTask<String, Void, Bitmap> {

		private Bitmap bitmap;
		private File file;
		private File bigFile;

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			// 保存路徑
			file = new File( Constans.NX_RECV_SAVE_PATH);
			if (!file.exists()) {
				file.mkdir();
			}
			bigFile = new File(file + "/" + System.currentTimeMillis());
			try {
				URL url = new URL(params[0]);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream inputStream = conn
						.getInputStream();
				bitmap = BitmapFactory
						.decodeStream(inputStream);
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(bigFile));
				bitmap.compress(
						Bitmap.CompressFormat.JPEG, 80,
						bos);
				bos.flush();
				bos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		
	}
	
	
	
}