package com.nxt.ynt;

/**
 * 当前登录用户个人信息
 * @author 赵佳丽
 *
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nxt.jnb.R;
import com.nxt.nxtapp.common.FileUtil;
import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.ynt.imageutil.ImageLoader;
import com.nxt.ynt.utils.ChangeGenderDialog;
import com.nxt.ynt.utils.Constans;
import com.nxt.ynt.utils.PublicResult;
import com.nxt.ynt.utils.Tools;
import com.nxt.ynt.utils.UpdateUserAvatar;
import com.nxt.ynt.utils.UploadUtil;
import com.nxt.ynt.utils.Util;
import com.qiniu.auth.JSONObjectRet;
import com.qiniu.io.IO;
import com.qiniu.utils.QiniuException;

public class PersonalDetailsActivity extends Activity implements
OnClickListener {
	private static String TAG = "PersonalDetailsActivity";
	private Util util;
	private Context context;
	private ImageView faceImage;
	private ImageLoader loader;
	private TextView username;
	public static TextView nickname, workname, areaname, gender;
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	public static Bitmap photo;
	private Drawable drawable;
	private String hobbyStr, util_area, util_work, userpic, util_uid,
	util_nickname, util_gender, util_username;
	// 头像路径
	private Uri head_path;
	// 弹出底部菜单
	private UpdateUserAvatar mUpdateUserAvatar;
	private ChangeGenderDialog dialog;

	public Handler head_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 修改性别成功
				PublicResult loginfo = (PublicResult) msg.obj;
				if (loginfo.getError().equals("0")) {
					// 保存用户信息
					util.saveToSp(Util.SEX, hobbyStr);
					gender.setText(hobbyStr);
					Util.showMsg(context, loginfo.getMsg());
				} else {
					Util.showMsg(context, loginfo.getMsg());
				}
				break;
			case 1:// 修改性别失败
				Toast.makeText(context, "修改失败，请检查网路连接是否正常！", 0).show();
				break;
			case 2:// 修改头像
				String headkey = null;
				if (msg.obj != null) {
					LogUtil.LogE(TAG, "msg.obj===" + msg.obj.toString());
					try {
						JSONObject jsonObject = new JSONObject(
								msg.obj.toString());
						headkey = jsonObject.optString("key", "");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// 保存用户信息
					faceImage.setImageDrawable(drawable);
					SettingActivity.faceImage.setImageDrawable(drawable);
					util.saveToSp(Util.UPIC, headkey);
					LogUtil.LogE(TAG, "upic===" + headkey);
					Toast.makeText(context, "修改成功！", 0).show();
					final Map<String, String> params = new HashMap<String, String>();
					params.put("upic", headkey);
					HttpCallBack callback = new HttpCallBack() {

						@Override
						public void onSuccess(String content) {
							content = this.getContent(content);
							LogUtil.LogI(TAG, "PersonalDetails 里边的content值 = "
									+ content);
						}

						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							LogUtil.LogI(TAG,
									"PersonalDetails 里边错误的content值 = "
											+ content + ":" + error);
						}
					};
					NxtRestClientNew.post("useredit", params, callback);
				} else {
					Toast.makeText(context, "修改失败，请检查网路连接是否正常！", 0).show();
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
						util.showMsg(PersonalDetailsActivity.this,
								"网络不给力啊，检查下网络或者再试一次吧！");
					}
				});
				break;
			case 4:// 获取upToken
				HttpCallBack callback = new HttpCallBack() {

					@Override
					public void onSuccess(String content) {
						content = this.getContent(content);
						LogUtil.LogE(TAG, "content===" + content);
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
						if (i != 3) {
							LogUtil.LogI(TAG, "获取失败：" + content + ";error:"
									+ error);
						} else {
							util.showMsg(PersonalDetailsActivity.this,
									"网络不给力啊，检查下网络或者再试一次吧！");
						}
					}
				};
				NxtRestClientNew.post("seven", null, callback);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.personal_details);
		context = this;
		loader = ImageLoader.getInstance(context);
		findViewId();
	}

	private void findViewId() {
		util = new Util(context);
		util_uid = util.getFromSp(Util.UID, "");
		util_nickname = util.getFromSp(Util.NICK, "");
		util_username = util.getFromSp(Util.UNAME, "");
		util_gender = util.getFromSp(Util.SEX, "");
		util_work = util.getFromSp(Util.work, "");
		util_area = util.getFromSp(Util.area, "");
		userpic = util.getFromSp(Util.UPIC, "");
		gender = (TextView) findViewById(R.id.gender);
		nickname = (TextView) findViewById(R.id.nickname);
		faceImage = (ImageView) findViewById(R.id.iv_head);
		workname = (TextView) findViewById(R.id.work);
		areaname = (TextView) findViewById(R.id.area);
		username = (TextView) findViewById(R.id.username);
		username.setText(util_username);
		if (util_work != null && !"".equals(util_work)) {
			workname.setText(util_work);
		}
		if (util_area != null && !"".equals(util_area)) {
			areaname.setText(util_area);
		}
		if (util_nickname != null && !"".equals(util_nickname)) {
			nickname.setText(util_nickname);
		}
		if (util_gender != null && !"".equals(util_gender)) {
			gender.setText(util_gender);
		}
		if (userpic != null && !"".equals(userpic)) {
			if (userpic.contains("http"))
				loader.displayImage(userpic, faceImage,
						R.drawable.contractdefalut);
			else
				loader.displayImage(Constans.getHeadUri(userpic), faceImage,
						R.drawable.contractdefalut);
		} else {
			faceImage.setImageResource(R.drawable.contractdefalut);
		}
	}

	public void onClick(View arg0) {
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		int id = arg0.getId();
		if (id == R.id.news_view_back) {
			finish();
		} else if (id == R.id.myhead) {
			NetworkInfo isNetWork = cwjManager.getActiveNetworkInfo();
			if (isNetWork == null) {
				Toast.makeText(context, "网络未连接，请稍后再试", 1).show();
				return;
			}
			mUpdateUserAvatar = new UpdateUserAvatar(context, this);
			ColorDrawable cd = new ColorDrawable(0x000000);
			mUpdateUserAvatar.setBackgroundDrawable(cd);
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = 0.4f;
			getWindow().setAttributes(lp);
			mUpdateUserAvatar.showAtLocation(
					findViewById(R.id.personaldetails), Gravity.BOTTOM, 0, 0);
		} else if (id == R.id.capture_pic_bt) {
			if (cwjManager.getActiveNetworkInfo() == null) {
				Toast.makeText(context, "网络未连接，请稍后再试", 1).show();
				return;
			}
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
			startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
			if (null != mUpdateUserAvatar && mUpdateUserAvatar.isShowing()) {
				mUpdateUserAvatar.dismiss();
				onDismiss();
			}
		} else if (id == R.id.select_pic_bt) {
			if (cwjManager.getActiveNetworkInfo() == null) {
				Toast.makeText(context, "网络未连接，请稍后再试", 1).show();
				return;
			}
			Intent intentFromGallery = new Intent();
			intentFromGallery.setType("image/*"); // 设置文件类型
			intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
			if (null != mUpdateUserAvatar && mUpdateUserAvatar.isShowing()) {
				mUpdateUserAvatar.dismiss();
				onDismiss();
			}
		} else if (id == R.id.close_update_avatar) {
			if (null != mUpdateUserAvatar && mUpdateUserAvatar.isShowing()) {
				mUpdateUserAvatar.dismiss();
				onDismiss();
			}
		} else if (id == R.id.iv_head) {
			if (userpic != null && !"".equals(userpic) && photo != null) {
				Intent intent = new Intent(context, ShowImagePageActivity.class);
				// intent传值bitmap
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] bitmapByte = baos.toByteArray();
				intent.putExtra("bitmap", bitmapByte);
				context.startActivity(intent);
			}
		} else if (id == R.id.mynickname) {
			Intent intent = new Intent(context, EditDetailActivity.class);
			context.startActivity(intent);
		} else if (id == R.id.mygender) {
			String str_gender = gender.getText().toString().trim();
			dialog = new ChangeGenderDialog(context, str_gender, this);
			dialog.show();
		} else if (id == R.id.nan) {
			uploadGender("男");
			dialog.dismiss();
		} else if (id == R.id.nv) {
			uploadGender("女");
			dialog.dismiss();
		} else if (id == R.id.myarea) {
			Intent area = new Intent(context, AddAreaActivity.class);
			area.putExtra("type", "area");
			startActivity(area);
		} else if (id == R.id.mywork) {
			/*
			 * Intent work = new Intent(context, AddHangYeActivity.class);
			 * work.putExtra("type", "hy"); startActivity(work);
			 */
			Intent intent = new Intent(this, GZHYActivity.class);
			startActivity(intent);
		} else if (id == R.id.mynamecard) {
			Intent card = new Intent(context, NamecardActivity.class);
			startActivity(card);
		}
	}

	public void onDismiss() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 1f;
		getWindow().setAttributes(lp);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (Tools.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory() + "/"
									+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(context, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
					.show();
				}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mUpdateUserAvatar != null && mUpdateUserAvatar.isShowing()) {
				mUpdateUserAvatar.dismiss();
				onDismiss();
				return true;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
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
			message.what = 4;
			message.obj = "Uptoken";
			head_handler.sendMessage(message);
		}
	}

	private void uploadGender(String gender) {
		hobbyStr = gender;
		RequestParams rp = new RequestParams();
		rp.put("uid", util_uid);
		rp.put("sex", gender);
		NxtRestClient.post(Constans.USEREDIT, rp,
				new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				LogUtil.LogI(TAG, "成功:" + content);
				PublicResult rs = (PublicResult) JsonPaser
						.getObjectDatas(PublicResult.class, content);
				Message message = head_handler.obtainMessage();
				message.obj = rs;
				message.what = 0;
				head_handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Message message = head_handler.obtainMessage();
				message.what = 1;
				head_handler.sendMessage(message);
			}
		});
	}

}
