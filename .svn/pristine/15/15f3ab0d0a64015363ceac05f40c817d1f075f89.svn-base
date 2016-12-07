package com.nxt.ynt.img;

/**
 * 相机，相册�?�择  发表
 */
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.ynt.AbsMainActivity;
import com.nxt.ynt.SoftApplication;
import com.nxt.ynt.entity.LoginInfo;
import com.nxt.ynt.imageutil.ImageZipUtil;
import com.nxt.ynt.img.adapter.GridImageAdapter;
import com.nxt.ynt.img.util.CommonDefine;
import com.nxt.ynt.img.util.FileUtils;
import com.nxt.ynt.img.util.ImageUtils;
import com.nxt.ynt.utils.CustomProgressDialog;
import com.nxt.ynt.utils.Util;
import com.qiniu.auth.JSONObjectRet;
import com.qiniu.io.IO;
import com.qiniu.utils.QiniuException;

public class AlbumEditActivity extends AbsMainActivity {
	private EditText mETGroupPhotoContent;
	private String locationMsg;
	String objectKey = null;
	private GridView gridView;
	private ArrayList<String> dataList;
	private GridImageAdapter gridImageAdapter;
	private ArrayList<String> tDataList;
	private String photoContent;
	private String intranetID;
	private String cameraImagePath = "";
	private int finishCount = -1;
	private StringBuilder builder;
	private Uri uri;
	private String sort;
	private String title;
	private String shareimg;
	private String shareUrl;
	private CustomProgressDialog pdlogin;
	private int num=0;
	private Util utils;
	private String token;
	private List<Uri> url_mlist = new ArrayList<Uri>();
	private List listkey = new ArrayList();
	String key;
	String url = "";
	String urlkey = "";
	private Uri fileUri;
	private String upZipPath;

	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.activity_album_edit);
		RelativeLayout topRl = (RelativeLayout) findViewById(R.id.top_title);
		ImageView mTVCancel = (ImageView) topRl.findViewById(R.id.set_cancel);
		ImageView mTVOk = (ImageView) topRl.findViewById(R.id.set_ok);
		mTVOk.setVisibility(View.VISIBLE);
		mTVCancel.setVisibility(View.VISIBLE);
		mTVCancel.setOnClickListener(mCancelListener);
		mTVOk.setOnClickListener(mOkListener);
		sort = getIntent().getStringExtra("sort");
		title = getIntent().getStringExtra("title");
		shareimg = getIntent().getStringExtra("shareimg");
		shareUrl = getIntent().getStringExtra("shareUrl");

//		pdlogin = new ProgressDialog(AlbumEditActivity.this);
		pdlogin =new CustomProgressDialog(this,R.anim.loadingframe);
		//设置点击进度对话框外的区域对话框不消失 
		pdlogin.setCanceledOnTouchOutside(false);
		dataList = new ArrayList<String>();
		init();
		initListener();
		photoContent = mETGroupPhotoContent.getText().toString();

		Bundle extras = getIntent().getExtras();
		String path = extras.getString("path");

		tDataList = (ArrayList<String>)extras.getSerializable("dataList");
		String editContent = extras.getString("editContent");
		if(editContent != null){
			mETGroupPhotoContent.setText(editContent);
		}
		if(path != null) {
			dataList.add(path);
			if(dataList.size() < 9){
				dataList.add("camera_default");
			}
			gridImageAdapter.notifyDataSetChanged();
		}
		if (tDataList != null) {
			for (int i = 0; i < tDataList.size(); i++) {
				String string = tDataList.get(i);
				dataList.add(string);
			}
			if (dataList.size() < 9) {
				dataList.add("camera_default");
			}
			gridImageAdapter.notifyDataSetChanged();
		}
	}

	private void init() {
		utils = new Util(this);
		mETGroupPhotoContent = (EditText) findViewById(R.id.group_camera_photo_content);
		gridView = (GridView) findViewById(R.id.gridview_image);
		//		dataList.add("camera_default");
		gridImageAdapter = new GridImageAdapter(this, dataList, loader, options);
		gridView.setAdapter(gridImageAdapter);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				LoginInfo login = (LoginInfo) msg.obj;
				if(login.getErrorcode().equals("2")){
					pdlogin.cancel();
					utils.showMsg(getApplicationContext(), login.getMsg());
				}else {
					utils.showMsg(getApplicationContext(), "发表成功");
					Intent ref = new Intent();
					if(sort.contains("#")){
						ref.setAction("refreshs");//大家说
					}else{
						ref.setAction("refresh");//园主秀
						//					ref.putExtra("paymsg", orderid);
					}
					sendBroadcast(ref);
					pdlogin.cancel();
					finish();
				}

				break;
			case 2:
				num++;
				android.os.Message message = handler.obtainMessage();
				message.what = 3;
				handler.sendMessage(message);
				break;
			case 3:
				pdlogin.setMessage("正在上传第"+(num+1)+"张图片");
				pdlogin.show();
				// 上传文件
				IO.putFile(null, token, null, url_mlist.get(num), null,
						new JSONObjectRet() {

					private String key;

					public void onProcess(long current, long total) {

					};
					public void onSuccess(JSONObject resp) {
						key = resp.optString("key", "");
						if (key != null) {
							listkey.add(key);
						}
						if(num==(url_mlist.size()-1)){
							postPublicMessage();
						}else{
							android.os.Message message = handler
									.obtainMessage();
							message.what = 2;
							handler.sendMessage(message);

						}

					}

					public void onFailure(QiniuException ex) {
						ex.printStackTrace();
						android.os.Message message = handler
								.obtainMessage();
						message.what = 4;
						handler.sendMessage(message);
						/*util.showMsg(FaBiaoActivity.this,
								"网络不给力啊，检查下网络或�?�再试一次吧�??");
						pdlogin.cancel();*/
					}
				});
				break;
			case 4:
				// 获取upToken
				HttpCallBack callback = new HttpCallBack() {

					

					@Override
					public void onSuccess(String content) {
						// {"uploadToken":"vahlgVTVPR59i46tgrzZVlzybNF9S5CRMASH_RMS:pzmzowtU9T88B2NzqYBS4XsRHqs=:eyJzY29wZSI6Im5vbmd4bG9nIiwiZGVhZGxpbmUiOjE0MTE4NzExMTh9",
						// "errorcode":1,"expireSeconds":3600}
						// 在父类对content做了解密处理
						content = this.getContent(content);
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(content);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						token = jsonObject.optString("uploadToken",
								"");
						
						android.os.Message message = handler
								.obtainMessage();
						message.obj = token;
						message.what = 3;
						handler.sendMessage(message);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						int i = this.AutoLoading("uptoken", null, this);
						android.os.Message message = handler
								.obtainMessage();
						message.what = 4;
						handler.sendMessage(message);
						/*if (i != 3) {
							LogUtil.LogI(TAG, "获取失败�??" + content + ";error:"
									+ error);
						} else {
							util.showMsg(FaBiaoActivity.this,
									"网络不给力啊，检查下网络或�?�再试一次吧�??");
							pdlogin.cancel();
						}*/
					}
				};
				NxtRestClientNew.post("seven", null, callback);
				break;
			case 5:
				File file = new File(upZipPath );
				fileUri = Uri.fromFile(file);
				url_mlist.add(fileUri);
				break;
			}
		}
	};


	private void initListener() {

		gridView.setOnItemClickListener(new GridView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String path = dataList.get(position);
				if (path.contains("default") && position == dataList.size() -1 && dataList.size() -1 != 9) {
					showSelectImageDialog();
				} else {
					Intent intent = new Intent(mActThis, ImageDelActivity.class);
					intent.putExtra("position", position);
					intent.putExtra("path", dataList.get(position));
					startActivityForResult(intent, CommonDefine.DELETE_IMAGE);
				}
			}
		});
	}

	private OnClickListener mCancelListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Dialog dialog = new Dialog(mActThis, R.style.dialog);
			View inflate = View.inflate(mActThis, R.layout.dialog_del, null);
			TextView dialogTitle = (TextView) inflate.findViewById(R.id.dialog_title);
			dialogTitle.setText("放弃此次编辑？");
			TextView dialogCancel = (TextView) inflate.findViewById(R.id.del_cancel);
			dialogCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			TextView dialogConfirm = (TextView) inflate.findViewById(R.id.confirm_del);
			dialogConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlbumEditActivity.this.finish();
				}
			});
			dialog.setContentView(inflate);
			dialog.show();
		}
	};

	private OnClickListener mOkListener = new OnClickListener() {

	


		@Override
		public void onClick(View v) {

			// TODO 发�?�，带图
			if((TextUtils.isEmpty(mETGroupPhotoContent.getText().toString()) && dataList.size() == 1)){

				Toast.makeText(mActThis, "发布信息不能为空", Toast.LENGTH_SHORT).show();
			} else {
				pdlogin.setCancelable(false);
				pdlogin.setMessage("正在上传，请稍后...");
				pdlogin.show();
				if(dataList.size()==1){  //如果不�?�择图片的时�??
					postPublicMessage();
				}else{     //如果选择图片的时�??

					new Thread(){
						public void run() {
							for(String path:dataList){
								
								if(!path.contains("camera_default")){
									/*
									File file = new File(path);
									fileUri = Uri.fromFile(file);
									url_mlist.add(fileUri);*/
									upZipPath = ImageZipUtil.getzippath(path);
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									android.os.Message message = handler
											.obtainMessage();
									message.what = 5;
									handler.sendMessage(message);
								}
								
							}
							android.os.Message message = handler
									.obtainMessage();
							message.what = 4;
							handler.sendMessage(message);
						};
					}.start();

				}  






			}
		}
	};


	// 异步提交发布信息
	private void postPublicMessage() {
		Map<String, String> postMsg = new HashMap<String, String>();
		postMsg.put("address", utils.getFromSp(Util.ADDRESS, ""));
		postMsg.put("msg", mETGroupPhotoContent.getText().toString());
		postMsg.put("sort", sort);
		if (listkey.size()!=0) {
			for (int i = 0; i < listkey.size(); i++) {
				if (i == 0) {
					url = (String) listkey.get(i);
				} else {
					url = "," + (String) listkey.get(i);
				}
				urlkey = urlkey + url;
			}
		}
		postMsg.put("img", urlkey);
		if(shareUrl!=null){
			postMsg.put("urlLink", shareUrl);
		}
		//util.showMsg(getApplicationContext(), "img:"+urlkey);
		HttpCallBack cb = new HttpCallBack() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				content = this.getContent(content);
				LoginInfo rootdata = (LoginInfo) JsonPaser.getObjectDatas(
						LoginInfo.class, content);
				Message msg = handler.obtainMessage();
				msg.obj = rootdata;
				msg.what=1;
				handler.sendMessage(msg);
			}

			@Override 
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				utils.showMsg(getApplicationContext(), "发表失败");
				pdlogin.cancel();
			}
		};
		NxtRestClientNew.post("postdongtai", postMsg, cb);
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			final Dialog dialog = new Dialog(this, R.style.dialog);
			View inflate = View.inflate(this, R.layout.dialog_del, null);
			TextView dialogTitle = (TextView) inflate.findViewById(R.id.dialog_title);
			dialogTitle.setText("放弃此次编辑？");
			TextView dialogCancel = (TextView) inflate.findViewById(R.id.del_cancel);
			dialogCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			TextView dialogConfirm = (TextView) inflate.findViewById(R.id.confirm_del);
			dialogConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlbumEditActivity.this.finish();
				}
			});
			dialog.setContentView(inflate);
			dialog.show();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CommonDefine.TAKE_PICTURE_FROM_CAMERA:
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
					return;
				}
				Bitmap bitmap = ImageUtils.getUriBitmap(this, uri, 400, 400);
				String cameraImagePath = FileUtils.saveBitToSD(bitmap, System.currentTimeMillis()+"");

				//				Bundle bundle = data.getExtras();
				//				Bitmap bitmap = (Bitmap) bundle.get("data");
				//				String cameraImagePath = ImageUtils.setCameraImage(bitmap);

				for (int i = 0; i < dataList.size(); i++) {
					String path = dataList.get(i);
					if(path.contains("default")) {
						dataList.remove(dataList.size() - 1);
					}
				}
				dataList.add(cameraImagePath);
				if(dataList.size() < 9) {
					dataList.add("camera_default");
				}
				gridImageAdapter.notifyDataSetChanged();
				break;
			case CommonDefine.TAKE_PICTURE_FROM_GALLERY:
				Bundle bundle2 = data.getExtras();
				tDataList = (ArrayList<String>) bundle2.getSerializable("dataList");
				if (tDataList != null) {
					for (int i = 0; i < tDataList.size(); i++) {
						String string = tDataList.get(i);
						dataList.add(string);
					}
					if (dataList.size() < 9) {
						dataList.add("camera_default");
					}
					gridImageAdapter.notifyDataSetChanged();
				}

				break;
			case CommonDefine.DELETE_IMAGE:
				int position = data.getIntExtra("position", -1);
				dataList.remove(position);
				if(dataList.size() < 9 ) {
					dataList.add(dataList.size(), "camera_default");
					for (int i = 0; i < dataList.size(); i++) {
						String path = dataList.get(i);
						if(path.contains("default")) {
							dataList.remove(dataList.size() - 2);
						}
					}
				}
				gridImageAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	// 选择相册，相�??
	private void showSelectImageDialog() {
		final Dialog picAddDialog = new Dialog(mActThis, R.style.dialog);
		View picAddInflate = View.inflate(mActThis, R.layout.item_dialog_camera, null);
		TextView camera = (TextView) picAddInflate.findViewById(R.id.camera);
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// 选择相机
				Intent cameraIntent = new Intent();
				cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
				// 根据文件地址创建文件
				File file = new File(CommonDefine.FILE_PATH);
				if (file.exists()) {
					file.delete();
				}
				uri = Uri.fromFile(file);
				// 设置系统相机拍摄照片完成后图片文件的存放地址
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

				// �??启系统拍照的Activity
				startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
				picAddDialog.dismiss();
			}
		});
		TextView mapStroge = (TextView) picAddInflate.findViewById(R.id.mapstorage);
		mapStroge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// 选择图库
				Intent intent = new Intent(mActThis, AlbumActivity.class);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("dataList", getIntentArrayList(dataList));
				bundle.putString("editContent", mETGroupPhotoContent.getText().toString());
				bundle.putString("sort", sort);
				intent.putExtras(bundle);
				startActivityForResult(intent, CommonDefine.TAKE_PICTURE_FROM_GALLERY);

				picAddDialog.dismiss();
				AlbumEditActivity.this.finish();
			}
		});
		TextView cancel = (TextView) picAddInflate.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				picAddDialog.dismiss();
			}
		});
		picAddDialog.setContentView(picAddInflate);
		picAddDialog.show();

	}

	private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {

		ArrayList<String> tDataList = new ArrayList<String>();

		for (String s : dataList) {
			if (!s.contains("default")) {
				tDataList.add(s);
			}
		}
		return tDataList;
	}

}
