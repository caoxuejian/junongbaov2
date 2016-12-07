package com.nxt.ynt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.jutong.live.LivePusher;
import com.jutong.live.LiveStateChangeListener;
import com.jutong.live.Live_MainActivity;
import com.nxt.jnb.R;
import com.nxt.nxtapp.common.FileUtil;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.ynt.img.util.CommonDefine;
import com.nxt.ynt.img.util.ImageUtils;
import com.nxt.ynt.utils.Constans;
import com.nxt.ynt.utils.UploadUtil;
import com.nxt.ynt.utils.Util;
import com.qiniu.auth.JSONObjectRet;
import com.qiniu.io.IO;
import com.qiniu.utils.QiniuException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera.CameraInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DescribeActivity extends Activity implements OnClickListener,
Callback, LiveStateChangeListener {

	private ImageView button01;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private LivePusher livePusher;
	private ImageView iv_back;
	private Button bt_direct;
	private Util util;
	private ImageView iv_fengmian;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int RESULT_REQUEST_CODE = 2;
	private BitmapDrawable drawable;
	private Bitmap photo;
	String util_username;
	private Uri head_path;
	private EditText et_content;
	String headkey = null;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_activity_main2);
		button01 = (ImageView) findViewById(R.id.button_first);
		iv_fengmian=(ImageView)findViewById(R.id.iv_fengmian);
		iv_back=(ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		bt_direct=(Button)findViewById(R.id.bt_direct);
		et_content=(EditText)findViewById(R.id.group_camera_photo_content);
		bt_direct.setOnClickListener(this);
		util_username = String.valueOf(System.currentTimeMillis());
		iv_fengmian.setOnClickListener(this);
		button01.setOnClickListener(this);
		button01.setBackground(getResources().getDrawable(R.drawable.start));
		mSurfaceView = (SurfaceView) this.findViewById(R.id.surface);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		livePusher = new LivePusher(this, 960, 720, 1024000, 15,
				CameraInfo.CAMERA_FACING_FRONT);
		livePusher.setLiveStateChangeListener(this);
		livePusher.prepare(mSurfaceHolder);
		util=new Util(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		livePusher.relase();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id==R.id.bt_direct){
			//			createvideo();
			if(headkey!=null){
				// 获取上传token
				Message message = head_handler.obtainMessage();
				message.what = 4;
				message.obj = "Uptoken";
				head_handler.sendMessage(message);
			}else{
				createvideo(headkey);
			}
		}else if(id==R.id.iv_fengmian){
			//			selectimg();
			showSelectImageDialog();
		}else if(id==R.id.iv_back){
//			livePusher.stopPusher();
//			livePusher.relase();
			finish();
		}
	}
	// 选择相册，相机
	private void showSelectImageDialog() {
		final Dialog picAddDialog = new Dialog(this, R.style.dialog);
		View picAddInflate = View.inflate(this, R.layout.item_dialog_camera, null);
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
				head_path = Uri.fromFile(file);
				// 设置系统相机拍摄照片完成后图片文件的存放地址
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, head_path);

				// �??启系统拍照的Activity
				startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
				picAddDialog.dismiss();
			}
		});
		TextView mapStroge = (TextView) picAddInflate.findViewById(R.id.mapstorage);
		mapStroge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// 选择图库
				Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);  
				intent1.addCategory(Intent.CATEGORY_OPENABLE);  
				intent1.setType("image/*");  
				startActivityForResult(intent1, IMAGE_REQUEST_CODE);
				//					startActivityForResult(intent, CommonDefine.TAKE_PICTURE_FROM_GALLERY);

				picAddDialog.dismiss();
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
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			case CommonDefine.TAKE_PICTURE_FROM_CAMERA:
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
					return;
				}
				Bitmap bitmap = ImageUtils.getUriBitmap(this, head_path, 400, 400);
				drawable = new BitmapDrawable(bitmap);
				// 获取上传token
				Message message = head_handler.obtainMessage();
				message.what = 1;
				head_handler.sendMessage(message);
				/*String cameraImagePath = FileUtils.saveBitToSD(bitmap, System.currentTimeMillis()+"");

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
				gridImageAdapter.notifyDataSetChanged();*/
				break;
			
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
		intent.putExtra("aspectY", 2);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 400);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
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
			message.what = 1;
			head_handler.sendMessage(message);
		}
	}
	public Handler head_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				iv_fengmian.setImageDrawable(drawable);
			case 2:
				if (msg.obj != null) {
					try {
						JSONObject jsonObject = new JSONObject(
								msg.obj.toString());
						headkey = jsonObject.optString("key", "");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					createvideo(headkey);
				}
				break;
			case 3:// 上传文件
				final String uptoken = msg.obj.toString();
				IO.putFile(null, uptoken,util_username, head_path, null,
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
					}
				};
				NxtRestClientNew.post("seven", null, callback);
				break;
			default:
				break;
			}
		};
	};

	private void createvideo(String headkey) {
		// TODO Auto-generated method stub
		final Map<String, String> params = new HashMap<String, String>();
		params.put("uid", util.getFromSp(util.UID, ""));
		params.put("title", et_content.getText().toString());
		params.put("cover",headkey);//封面图，七牛的key
		HttpCallBack callback = new HttpCallBack() {

			private String rtmpurl;
			private String videoid;

			@Override
			public void onSuccess(String content) {
//				livePusher.relase();
				// 在父类对content做了解密处理
				content = this.getContent(content);
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(content);
					rtmpurl=jsonObject.getString("url");//直播地址
					videoid=jsonObject.getString("videoid");//用于直播结束通知服务器带的参数
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Intent intent=new Intent();
				intent.setClass(DescribeActivity.this, Live_MainActivity.class);
				intent.putExtra("rtmpurl", rtmpurl);
				intent.putExtra("videoid", videoid);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(DescribeActivity.this, "请检查网络是否连接！", Toast.LENGTH_LONG).show();
			}
		};
		NxtRestClientNew.post("createvideo", params, callback);
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	/**
	 * 可能运行在子线程
	 */
	@Override
	public void onErrorPusher(int code) {
	}

	/**
	 * 可能运行在子线程
	 */
	@Override
	public void onStartPusher() {
	}

	/**
	 * 可能运行在子线程
	 */
	@Override
	public void onStopPusher() {
	}
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			livePusher.stopPusher();
//			livePusher.relase();
			finish();
		}
		return true;
	}*/

}
