package com.nxt.nxtapp.ui;

/*
 * �����ı���ͼƬ��Ϣ
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
				case -2:// �ϴ�ͼƬ
					ResultInfo result0 = (ResultInfo) msg.obj;
					if (result0.getResult().trim().equals("0")) {
						Toast.makeText(PublicActivity.this, "ͼƬ�ϴ��ɹ�����", 0)
								.show();
					} else {
						Toast.makeText(PublicActivity.this, result0.getMsg(), 0)
								.show();
					}
					break;
				case -1:// ���ؽ��Ϊ��
					Toast.makeText(PublicActivity.this, "��Ϣ����ʧ�ܣ�", 0).show();
					break;
				case -3:
					ResultInfo result = (ResultInfo) msg.obj;
					if (result.getResult().trim().equals("0")) {
						Toast.makeText(PublicActivity.this, "��Ϣ�����ɹ���", 0)
								.show();
						postPictureThread(result.getInfoid());

					} else {
						Toast.makeText(PublicActivity.this, result.getMsg(), 0)
								.show();
					}
					break;
				default:
					Toast.makeText(PublicActivity.this, "��Ϣ����ʧ�ܣ�", 0).show();
					break;
				}

			}

		};
	}

	// �첽�ύ�����ı���Ϣ
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

	// �ϴ�ͼƬ
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
		// ��ʼ����ѡ��
		imag_qiugou.setImageResource(R.drawable.check);
		imag_gongying.setImageResource(R.drawable.check);
		sort = null;

		// ѡȡ��Ƭ
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
		// �ж�����һ������
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
		default:// �ҵķ�������ӵ���¼�
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
				Toast.makeText(PublicActivity.this, "ͼƬ����ϴ�9��", 0).show();
				return;
			}
			if (arg2 == bitmaps.size() - 1) {
				showWindow();
			} else {
				new AlertDialog.Builder(PublicActivity.this)
						.setTitle("��ʾ")
						.setMessage("ȷ��Ҫɾ����ͼƬ��")
						.setPositiveButton("ɾ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										mlist.remove(arg2);
										bitmaps.remove(arg2);
										imageAdapter.notifyDataSetChanged();
									}
								})
						.setNegativeButton("ȡ��",
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
			util.showMsg(PublicActivity.this, "������Ϣ����Ϊ��");
		}
		if(fromwhere==1&&sort==null){
			util.showMsg(PublicActivity.this, "��ѡ����Ϣ����");
		}
		//�ύ��Ϣ��װ
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

	// �ӱ���ý���ѡȡͼƬ
	private void startChoicePicture() {
		// ʹ��ý���
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		// ȡ����Ƭ�󷵻ر�����
		startActivityForResult(intent, CHOOSE_PICTURE_REQUEST_CODE);
	}

	String name;

	// ����
	private void startToTakePhoto() {
		// ʹ��ý���
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		name = TimeTools.getCurrentTime(2) + ".jpg";
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), name)));
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		// ȡ����Ƭ�󷵻ر�����
		startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
	}

	// ����ѡ����ͼƬ
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
			case CHOOSE_PICTURE_REQUEST_CODE:// ����ѡȡ
				// ��λuriΪ��ϵͳý�����������򿪵��ļ�uri��ַ
				Uri uri = data.getData();
				// ���uri��Ϊ�գ���ȡ����ͼƬ�ļ���ַ
				if (uri != null && !uri.equals("")) {
					// ͨ���ļ����ķ�ʽ��ȡͼƬ
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
						// ��ȡ���Ĳ���ͼƬ�Ĵ���
					}
				}

				// ��ȡͼƬ��·����
				try {
					String[] proj = { MediaStore.Images.Media.DATA };

					// ������android��ý�����ݿ�ķ�װ�ӿڣ�����Ŀ�Android�ĵ�
					Cursor cursor = managedQuery(uri, proj, null, null, null);
					// ���Ҹ������ ����ǻ���û�ѡ���ͼƬ������ֵ
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					// �����������ͷ ���������Ҫ����С�ĺ���������Խ��
					cursor.moveToFirst();
					// ����������ֵ��ȡͼƬ·��
					String path = cursor.getString(column_index);
					com.nxt.nxtapp.common.LogUtil.LogI("PATH", path);
					picture.setImageurl(path);
					list_files.add(new File(path));
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;

			case TAKE_PHOTO_REQUEST_CODE:// ����
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

			// ����һ��PopuWidow����
			popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
					LayoutParams.MATCH_PARENT);
		}
		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);

		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		// popupWindow.setOutsideTouchable(false);
		// ��ʾ��λ��Ϊ:��Ļ�Ŀ�ȵ�һ��-PopupWindow�ĸ߶ȵ�һ��
		// int xPos = windowManager.getDefaultDisplay().getWidth() / 2
		// - popupWindow.getWidth() / 2;
		// popupWindow.showAsDropDown(parent, xPos, 0);
		popupWindow
				.showAtLocation(this.getCurrentFocus(), Gravity.CENTER, 0, 0);
		// �������
		// InputMethodManager imm = (InputMethodManager) this
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

	}

	/** ����հ���������� */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			// ��õ�ǰ�õ������View��һ������¾���EditText������������ǹ켣�����ʵ�尸�����ƶ����㣩
			View v = getCurrentFocus();

			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * ����EditText����������û������������Աȣ����ж��Ƿ����ؼ��̣���Ϊ���û����EditTextʱû��Ҫ����
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
				// ���EditText���¼�����������
				return false;
			} else {
				return true;
			}
		}
		// ������㲻��EditText����ԣ������������ͼ�ջ����꣬��һ�����㲻��EditView�ϣ����û��ù켣��ѡ�������Ľ���
		return false;
	}

	/**
	 * ������������̷���������һ��
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
