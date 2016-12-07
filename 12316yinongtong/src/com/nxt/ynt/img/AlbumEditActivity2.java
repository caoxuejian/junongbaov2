package com.nxt.ynt.img;

/**
 * 相机，相册�?�择  发表
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nxt.jnb.R;
import com.nxt.nxtapp.common.FileUtil;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.ynt.AbsMainActivity;
import com.nxt.ynt.PersonalDetailsActivity;
import com.nxt.ynt.SettingActivity;
import com.nxt.ynt.SoftApplication;
import com.nxt.ynt.entity.DongTaiList;
import com.nxt.ynt.entity.DynamicMsgListContent;
import com.nxt.ynt.entity.LoginInfo;
import com.nxt.ynt.imageutil.ImageZipUtil;
import com.nxt.ynt.img.adapter.GridImageAdapter;
import com.nxt.ynt.img.util.CommonDefine;
import com.nxt.ynt.img.util.FileUtils;
import com.nxt.ynt.img.util.ImageUtils;
import com.nxt.ynt.utils.Constans;
import com.nxt.ynt.utils.PublicResult;
import com.nxt.ynt.utils.Tools;
import com.nxt.ynt.utils.UpdateUserAvatar;
import com.nxt.ynt.utils.UploadUtil;
import com.nxt.ynt.utils.Util;
import com.qiniu.auth.JSONObjectRet;
import com.qiniu.io.IO;
import com.qiniu.utils.QiniuException;

public class AlbumEditActivity2 extends AbsMainActivity implements OnClickListener{
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
	private ProgressDialog pdlogin;
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
	private String nickname;
	private String userpic;
	private UpdateUserAvatar mUpdateUserAvatar;
	private String util_username;
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";


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
		mTVOk.setOnClickListener(this);
		//		mTVOk.setOnClickListener(mOkListener);
		sort = getIntent().getStringExtra("sort");
		title = getIntent().getStringExtra("title");
		shareimg = getIntent().getStringExtra("shareimg");
		shareUrl = getIntent().getStringExtra("shareUrl");


		pdlogin = new ProgressDialog(AlbumEditActivity2.this);

		dataList = new ArrayList<String>();
		init();
		util_username = utils.getFromSp(utils.UNAME, "");
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
					ref.setAction("refresh");
					//					ref.putExtra("paymsg", orderid);
					sendBroadcast(ref);
					pdlogin.cancel();
					Map<String, String> params = new HashMap<String, String>();
					params.put("start", "0");
					params.put("end", "10");
					params.put("sort", sort);
					NxtRestClientNew.post("dynamicMsgList", params,
							new HttpCallBack() {

						private List dydatas;
						@Override
						public void onFailure(Throwable error,
								String content) {
							super.onFailure(error, content);
						}
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							content = this.getContent(content);
							if (content == null) {
								Toast.makeText(AlbumEditActivity2.this,
										"无数据，请检查网络是否连接！",
										Toast.LENGTH_SHORT).show();
							} else {
								DongTaiList news = (DongTaiList) com.nxt.nxtapp.json.JsonPaser
										.getObjectDatas(
												DongTaiList.class,
												content);
								dydatas = com.nxt.nxtapp.json.JsonPaser
										.getArrayDatas(
												DynamicMsgListContent.class,
												news.getDynamicMsgList());
								if (dydatas != null) {
									/*if(WeiBoFragment.messagePublicAdapter!=null){
										WeiBoFragment.dydatas_result = dydatas;
										WeiBoFragment.messagePublicAdapter.newsInfos_result = dydatas;
										WeiBoFragment.messagePublicAdapter
										.notifyDataSetChanged();
									}*/
								} else {
									Toast.makeText(AlbumEditActivity2.this,
											"暂无数据", Toast.LENGTH_SHORT)
											.show();
								}
							}
						}
					});

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
					AlbumEditActivity2.this.finish();
				}
			});
			dialog.setContentView(inflate);
			dialog.show();
		}
	};

	private BitmapDrawable drawable;
	private Uri head_path;
	public static Bitmap photo;


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
					AlbumEditActivity2.this.finish();
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
			case CommonDefine.IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CommonDefine.CAMERA_REQUEST_CODE:
				if (Tools.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory() + "/"
									+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(AlbumEditActivity2.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
					.show();
				}
				break;
			case CommonDefine.RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
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
				AlbumEditActivity2.this.finish();
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id==R.id.set_ok){
			//这里进行判断，当前用户无头像或者昵称时先要求其输入昵称头像
			nickname=utils.getFromSp(utils.NICK, "");
			userpic=utils.getFromSp(utils.UPIC, "");
			if(nickname==null||userpic==null||"".equals(nickname)||"".equals(userpic)){
				if(userpic==null||"".equals(userpic)){
					mUpdateUserAvatar = new UpdateUserAvatar(AlbumEditActivity2.this, this);
					ColorDrawable cd = new ColorDrawable(0x000000);
					mUpdateUserAvatar.setBackgroundDrawable(cd);
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.alpha = 0.4f;
					getWindow().setAttributes(lp);
					mUpdateUserAvatar.showAtLocation(
							findViewById(R.id.album_edit), Gravity.CENTER, 0, 0);

				}else if(nickname==null||"".equals(nickname)){
					Message message = head_handler.obtainMessage();
					message.what = 1;
					head_handler.sendMessage(message);
				}

			}else{
				Message message = head_handler.obtainMessage();
				message.what = 0;
				head_handler.sendMessage(message);
			}
		}else if (id == R.id.capture_pic_bt) {
			Intent intentFromCapture = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// 判断存储卡是否可以用，可用进行存储
			if (Tools.hasSdcard()) {
				intentFromCapture
				.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(),
								IMAGE_FILE_NAME)));
				intentFromCapture.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
			}
			startActivityForResult(intentFromCapture,  CommonDefine.CAMERA_REQUEST_CODE);
			if (null != mUpdateUserAvatar && mUpdateUserAvatar.isShowing()) {
				mUpdateUserAvatar.dismiss();
				onDismiss();
			}
		}else if (id == R.id.select_pic_bt) {
			Intent intentFromGallery = new Intent();
			intentFromGallery.setType("image/*"); // 设置文件类型
			intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intentFromGallery,  CommonDefine.IMAGE_REQUEST_CODE);
			if (null != mUpdateUserAvatar && mUpdateUserAvatar.isShowing()) {
				mUpdateUserAvatar.dismiss();
				onDismiss();
			}
		} else if (id == R.id.close_update_avatar) {
			if (null != mUpdateUserAvatar && mUpdateUserAvatar.isShowing()) {
				mUpdateUserAvatar.dismiss();
				onDismiss();
			}
		} 
	}
	public void onDismiss() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 1f;
		getWindow().setAttributes(lp);
	}
	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		com.nxt.nxtapp.common.LogUtil.syso(uri.getPath());
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CommonDefine.RESULT_REQUEST_CODE);
	}
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			drawable = new BitmapDrawable(photo);
			Bitmap bitmap = UploadUtil.drawable2Bitmap(drawable);
			String path = Constans.NX_RECV_SAVE_PATH;
			File mediaFile = new File(path + File.separator + "head"
					+ util_username + ".jpg");
			// 创建.nomedia文件
			FileUtil.createFile(path, ".nomedia");
			if (mediaFile.exists()) {
				mediaFile.delete();
			}
			if (!new File(path).exists()) {
				new File(path).mkdirs();
			}
			try {
				mediaFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(mediaFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
				fos.flush();
				fos.close();
				bitmap.recycle();
				bitmap = null;
				System.gc();
			} catch (IOException e) {
				e.printStackTrace();
			}
			head_path = Uri.fromFile(mediaFile);
			// 获取上传token
			Message message = head_handler.obtainMessage();
			message.what = 4;
			message.obj = "Uptoken";
			head_handler.sendMessage(message);
		}
	}

	public Handler head_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// TODO 发�?�，带图
				if((TextUtils.isEmpty(mETGroupPhotoContent.getText().toString()) && dataList.size() == 1)){

					Toast.makeText(mActThis, "发布信息不能为空", Toast.LENGTH_SHORT).show();
				} else {

					pdlogin.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
				break;
			case 1:
				showAlertDialog();
				break;
			case 2:// 修改头像
				String headkey = null;
				if (msg.obj != null) {
					try {
						JSONObject jsonObject = new JSONObject(
								msg.obj.toString());
						headkey = jsonObject.optString("key", "");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					utils.saveToSp(Util.UPIC, headkey);
					final Map<String, String> params = new HashMap<String, String>();
					params.put("upic", headkey);
					HttpCallBack callback = new HttpCallBack() {

						@Override
						public void onSuccess(String content) {
							content = this.getContent(content);
							Message message = head_handler.obtainMessage();
							message.what = 1;
							head_handler.sendMessage(message);
						}

						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
						}
					};
					NxtRestClientNew.post("useredit", params, callback);
				} else {
				}
				break;
			case 3:// 上传文件
				final String uptoken = msg.obj.toString();
				IO.putFile(null, uptoken, "head/" + util_username + "/"
						+ System.currentTimeMillis(), head_path, null,
						new JSONObjectRet() {
					@Override
					public void onSuccess(JSONObject resp) {
						Message message = head_handler.obtainMessage();
						message.obj = resp;
						message.what = 2;
						head_handler.sendMessage(message);
					}

					@Override
					public void onFailure(QiniuException ex) {
						ex.printStackTrace();
					}
				});
				break;
			case 4:// 获取upToken
				HttpCallBack callback = new HttpCallBack() {

					@Override
					public void onSuccess(String content) {
						content = this.getContent(content);
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(content);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						String token = jsonObject.optString("uploadToken", "");
						Message message = head_handler.obtainMessage();
						message.obj = token;
						message.what = 3;
						head_handler.sendMessage(message);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						int i = this.AutoLoading("uptoken", null, this);
					}
				};
				NxtRestClientNew.post("seven", null, callback);
				break;
			default:
				break;
			}
		};
	};
	private void showAlertDialog() {
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setView(LayoutInflater.from(this).inflate(R.layout.alert_dialog, null));
		dialog.show();
		dialog.getWindow().setContentView(R.layout.alert_dialog);
		Button btnPositive = (Button) dialog.findViewById(R.id.btn_add);
		Button btnNegative = (Button) dialog.findViewById(R.id.btn_cancel);
		final EditText etContent = (EditText) dialog.findViewById(R.id.et_content);
		btnPositive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final String str = etContent.getText().toString();
				if ("".equals(str)) {
					etContent.setError("输入内如不能为空");
				} else {
					dialog.dismiss();
					Map<String, String> params = new HashMap<String, String>();
					params.put("nick", str);
					NxtRestClientNew.post("useredit", params , new HttpCallBack(){
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							// 保存用户信息
							utils.saveToSp(utils.NICK, str);
						}

						@Override
						public void onFailure(Throwable error,
								String content) {
							super.onFailure(error, content);
							content = this.getContent(content);
						}
					});
				}
			}
		});
		btnNegative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
	}

}
