package com.nxt.nxtapp.ui;

/*
 * 发布文本、图片信息
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nxt.nxtapp.R;
import com.nxt.nxtapp.common.BitmapUtil;
import com.nxt.nxtapp.common.TimeTools;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.nxtapp.model.ResultInfo;
import com.nxt.nxtapp.model.publicmsg.GQDatas;
import com.nxt.nxtapp.model.publicmsg.Picture;
import com.nxt.nxtapp.setting.Constant;
import com.nxt.nxtapp.ui.adapter.publicmsg.ImageAdapter;

public class PublicActivity extends Activity implements OnClickListener {
	private String sort = null;
	private ImageView imag_gongying;
	private ImageView imag_qiugou;
	private EditText edit_public;
	private String public_content;
	private TextView paper_title;
	// private ImageView imag_photo;
	private static final int CHOOSE_PICTURE_REQUEST_CODE = 0;
	private static final int TAKE_PHOTO_REQUEST_CODE = 1;
	private PopupWindow popupWindow;
	private View view;
	private RelativeLayout linearlayout;

	private Button take_photo;
	private Button take_file;
	private Button cancle;
	private GridView mygridView;
	private Bitmap pic_deal;
	private int mode;
	private Handler handler;
	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
	private ImageAdapter imageAdapter;
	private List<Picture> mlist = new ArrayList<Picture>();
	private List<File> list_files = new ArrayList<File>();
	private Util util;
	private LinearLayout mysort_linearlayou;
	private int fromwhere;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_public);
		fromwhere = getIntent().getIntExtra("fromwhere", 0);
		mode = getIntent().getIntExtra("mode", 0);
		util = new Util(this);
		initView();
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case -2:// 上传图片
					ResultInfo result0 = (ResultInfo) msg.obj;
					if (result0.getResult().trim().equals("0")) {
						Toast.makeText(PublicActivity.this, "图片上传成功！！", 0)
								.show();
					} else {
						Toast.makeText(PublicActivity.this, result0.getMsg(), 0)
								.show();
					}
					break;
				case -1:// 返回结果为空
					Toast.makeText(PublicActivity.this, "信息发布失败！", 0).show();
					break;
				case -3:
					ResultInfo result = (ResultInfo) msg.obj;
					if (result.getResult().trim().equals("0")) {
						Toast.makeText(PublicActivity.this, "信息发布成功！", 0)
								.show();
						postPictureThread(result.getInfoid());

					} else {
						Toast.makeText(PublicActivity.this, result.getMsg(), 0)
								.show();
					}
					break;
				default:
					Toast.makeText(PublicActivity.this, "信息发布失败！", 0).show();
					break;
				}

			}

		};
	}

	// 异步提交发布文本信息
	private void postPublicMessage() {
		try {
			// TODO Auto-generated method stub
			Map<String, String> postMsg = new HashMap<String, String>();
			postMsg.put("siteid", Constant.SITEID);
			postMsg.put("deviceid", util.getFromSp(Util.DEVICEID, null));
			postMsg.put("uid", util.getFromSp(Util.UID, "1"));
			postMsg.put("lng", util.getFromSp(Util.LONGITUDE, null));
			postMsg.put("lat", util.getFromSp(Util.LATITUDE, null));
			postMsg.put("address", util.getFromSp(Util.ADDRESS, null));
			postMsg.put("buyorsell", sort);
			postMsg.put("content", public_content);
			postMsg.put("catid", getIntent().getStringExtra("cateid"));
			RequestParams params = new RequestParams(postMsg);
			NxtRestClient.post(Constant.PUBLIC_POSTCONTENT_URL, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							// TODO Auto-generated method stub
							super.onSuccess(content);
							com.nxt.nxtapp.common.LogUtil.syso("json:" + content);
							ResultInfo rs = (ResultInfo) JsonPaser
									.getObjectDatas(ResultInfo.class, content);
							// com.nxt.nxtapp.common.LogUtil.LogI("post", result);
							Message msg = handler.obtainMessage();
							if (rs != null) {
								msg.what = -3;
								msg.obj = rs;
							} else {
								msg.what = -1;
							}
							handler.sendMessage(msg);
						}

						@Override
						public void onFailure(Throwable error, String content) {
							// TODO Auto-generated method stub
							super.onFailure(error, content);
							com.nxt.nxtapp.common.LogUtil.syso("failure:" + content);
						}

					});

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	// 上传图片
	private void postPictureThread(final String infoid) {

		try {
			Map<String, String> pic_param = new HashMap<String, String>();
			pic_param.put("siteid", Constant.SITEID);
			pic_param.put("deviceid", util.getFromSp(Util.DEVICEID, null));
			pic_param.put("uid", util.getFromSp(Util.UID, null));
			pic_param.put("infoid", infoid);
			RequestParams params = new RequestParams(pic_param);
			for (File file : list_files) {
				params.put("imgfile", file);

				NxtRestClient.post(Constant.PUBLIC_POSTPICTURE_URL, params,
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String content) {
								// TODO Auto-generated method stub
								super.onSuccess(content);
								com.nxt.nxtapp.common.LogUtil.syso(content);
								ResultInfo rs = (ResultInfo) JsonPaser
										.getObjectDatas(ResultInfo.class,
												content);
								Message msg = handler.obtainMessage();
								if (rs != null) {
									msg.what = -2;
									msg.obj = rs;
								} else {
									msg.what = -1;
								}

								handler.sendMessage(msg);
							}

						});

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initView() {
		mysort_linearlayou = (LinearLayout) findViewById(R.id.mysort);
		paper_title = (TextView) findViewById(R.id.paper_title);
		paper_title.setText(getIntent().getStringExtra("title"));
		if (fromwhere != Constant.PUBLIC_GQ) {
			mysort_linearlayou.setVisibility(View.INVISIBLE);
		}

		imag_gongying = (ImageView) findViewById(R.id.gongying_icon);
		imag_qiugou = (ImageView) findViewById(R.id.qiugou_icon);
		// 初始化单选框
		imag_qiugou.setImageResource(R.drawable.check);
		imag_gongying.setImageResource(R.drawable.check);
		sort = null;

		// 选取照片
		// imag_photo = (ImageView) findViewById(R.id.public_photo);
		mygridView = (GridView) findViewById(R.id.mygridview);
		bitmaps.add(null);
		imageAdapter = new ImageAdapter(this, bitmaps);
		mygridView.setAdapter(imageAdapter);
		mygridView.setOnItemClickListener(new onPicItemClickListener());

		edit_public = (EditText) findViewById(R.id.public_content);
		LinearLayout layout_gy = (LinearLayout) findViewById(R.id.gongying);
		LinearLayout layout_qg = (LinearLayout) findViewById(R.id.qiugou);
		Button backButton = (Button) findViewById(R.id.cancle);
		Button postButton = (Button) findViewById(R.id.post);

		backButton.setOnClickListener(this);
		postButton.setOnClickListener(this);

		// imag_photo.setOnClickListener(this);
		// 判断是哪一种类型
		int sort_mode = getIntent().getIntExtra("sort_mode", 0);
		switch (sort_mode) {
		case 1:// buy
			imag_qiugou.setImageResource(R.drawable.checked);
			imag_gongying.setImageResource(R.drawable.check);
			sort = "buy";
			break;

		case 2:// sell
			imag_qiugou.setImageResource(R.drawable.check);
			imag_gongying.setImageResource(R.drawable.checked);
			sort = "sell";
			break;
		default:// 我的发布，添加点击事件
			layout_gy.setOnClickListener(this);
			layout_qg.setOnClickListener(this);
			break;
		}

	}

	class onPicItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (arg2 > 9) {
				Toast.makeText(PublicActivity.this, "图片最多上传9张", 0).show();
				return;
			}
			if (arg2 == bitmaps.size() - 1) {
				showWindow();
			} else {
				new AlertDialog.Builder(PublicActivity.this)
						.setTitle("提示")
						.setMessage("确定要删除该图片吗？")
						.setPositiveButton("删除",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										mlist.remove(arg2);
										bitmaps.remove(arg2);
										imageAdapter.notifyDataSetChanged();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {

									}
								}).show();
			}
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id==R.id.cancle){
			finish();
		}else if(id==R.id.post){
		public_content = edit_public.getText().toString();
		if(public_content==null||public_content.equals("")){
			util.showMsg(PublicActivity.this, "发布信息不能为空");
		}
		if(fromwhere==1&&sort==null){
			util.showMsg(PublicActivity.this, "请选择信息种类");
		}
		//提交信息封装
		GQDatas data = new GQDatas();
		data.setContacts(util.getFromSp(Util.USERNAME, null));
		data.setAddress(util.getFromSp(Util.ADDRESS, null));
		data.setBuyorsell(sort);
		data.setContent(public_content);
		data.setPubtime(TimeTools.getCurrentTime(0));
		data.setTel(util.getFromSp(Util.PHONE, ""));
		//data.setBitmap(pic_deal);
		data.setMlist(mlist);
		com.nxt.nxtapp.common.LogUtil.syso("sort@@@@@@@@"+sort);	
			
		postPublicMessage();
		}else if(id== R.id.qiugou){
			imag_qiugou.setImageResource(R.drawable.checked);
			imag_gongying.setImageResource(R.drawable.check);
			sort = "buy";
		}
		else if(id==R.id.gongying){
			imag_qiugou.setImageResource(R.drawable.check);
			imag_gongying.setImageResource(R.drawable.checked);
			sort = "sell";
		}
		else if(id==R.id.take_file){
			startChoicePicture();
		}
		else if(id==R.id.take_photo){
			startToTakePhoto();
		}
		else if(id==R.id.cancle_but){
			popupWindow.dismiss();
		}
	}

	// 从本地媒体库选取图片
	private void startChoicePicture() {
		// 使用媒体库
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		// 取得相片后返回本画面
		startActivityForResult(intent, CHOOSE_PICTURE_REQUEST_CODE);
	}

	String name;

	// 拍照
	private void startToTakePhoto() {
		// 使用媒体库
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		name = TimeTools.getCurrentTime(2) + ".jpg";
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), name)));
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		// 取得相片后返回本画面
		startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
	}

	// 处理选择后的图片
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		com.nxt.nxtapp.common.LogUtil.syso("resultCode:" + resultCode + "requestCode:"
				+ requestCode);
		Bitmap bitmap = null;
		Picture picture = new Picture();
		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case CHOOSE_PICTURE_REQUEST_CODE:// 本地选取
				// 定位uri为非系统媒体库浏览器所打开的文件uri地址
				Uri uri = data.getData();
				// 如果uri不为空，则取到了图片文件地址
				if (uri != null && !uri.equals("")) {
					// 通过文件流的方式获取图片
					ContentResolver cr = this.getContentResolver();
					try {
						BitmapFactory.Options opts = new BitmapFactory.Options();
						opts.inJustDecodeBounds = true;
						BitmapFactory.decodeStream(cr.openInputStream(uri),
								null, opts);

						opts.inSampleSize = BitmapUtil.computeSampleSize(opts,
								-1, 85 * 85);
						opts.inJustDecodeBounds = false;
						bitmap = BitmapFactory.decodeStream(
								cr.openInputStream(uri), null, opts);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (bitmap != null) {
					} else {
						// 获取到的不是图片的处理
					}
				}

				// 获取图片的路径：
				try {
					String[] proj = { MediaStore.Images.Media.DATA };

					// 好像是android多媒体数据库的封装接口，具体的看Android文档
					Cursor cursor = managedQuery(uri, proj, null, null, null);
					// 按我个人理解 这个是获得用户选择的图片的索引值
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					// 将光标移至开头 ，这个很重要，不小心很容易引起越界
					cursor.moveToFirst();
					// 最后根据索引值获取图片路径
					String path = cursor.getString(column_index);
					com.nxt.nxtapp.common.LogUtil.LogI("PATH", path);
					picture.setImageurl(path);
					list_files.add(new File(path));
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;

			case TAKE_PHOTO_REQUEST_CODE:// 拍照
				// bitmap = (Bitmap) data.getExtras().get("data");
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;

				File f = new File(Environment.getExternalStorageDirectory(),
						name);
				com.nxt.nxtapp.common.LogUtil.syso("fileName:" + f.getAbsolutePath());
				picture.setImageurl(f.getAbsolutePath());
				list_files.add(f);
				BitmapFactory.decodeFile(f.getAbsolutePath(), opts);
				opts.inSampleSize = BitmapUtil.computeSampleSize(opts, -1,
						85 * 85);
				opts.inJustDecodeBounds = false;
				try {
					bitmap = BitmapFactory
							.decodeFile(f.getAbsolutePath(), opts);
				} catch (OutOfMemoryError err) {
				}
			}
			pic_deal = bitmap;
			// imag_photo.setImageBitmap(bitmap);
			bitmaps.add(bitmap);
			picture.setBitmap(bitmap);
			mlist.add(picture);
			imageAdapter.notifyDataSetChanged();
			if (popupWindow != null)
				popupWindow.dismiss();
			// if (bitmap != null && !bitmap.isRecycled())
			// bitmap.recycle();
		}
	}

	private void showWindow() {

		if (popupWindow == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(this);

			view = layoutInflater.inflate(R.layout.public_choose, null);

			take_file = (Button) view.findViewById(R.id.take_file);
			take_photo = (Button) view.findViewById(R.id.take_photo);
			cancle = (Button) view.findViewById(R.id.cancle_but);

			take_file.setOnClickListener(this);
			take_photo.setOnClickListener(this);
			cancle.setOnClickListener(this);

			// 创建一个PopuWidow对象
			popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
					LayoutParams.MATCH_PARENT);
		}
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		// popupWindow.setOutsideTouchable(false);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		// int xPos = windowManager.getDefaultDisplay().getWidth() / 2
		// - popupWindow.getWidth() / 2;
		// popupWindow.showAsDropDown(parent, xPos, 0);
		popupWindow
				.showAtLocation(this.getCurrentFocus(), Gravity.CENTER, 0, 0);
		// 打开软键盘
		// InputMethodManager imm = (InputMethodManager) this
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

	}

	/** 点击空白隐藏软键盘 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
			View v = getCurrentFocus();

			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 多种隐藏软件盘方法的其中一种
	 * 
	 * @param token
	 */
	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
