package com.nxt.ynt.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;


public class AsyncImageTask extends AsyncTask<ImageView, Void, Bitmap> {
	private ImageView gView;
	private int model = 0;
	private int[] sizes = new int[] {};

	public AsyncImageTask() {

	}

	public AsyncImageTask(int model) {
		this.model = model;
	}

	public AsyncImageTask(int[] sizes) {
		super();
		this.sizes = sizes;
	}

	protected Bitmap doInBackground(ImageView... views) {
		Bitmap bmp = null;
		ImageView view = views[0];
		if (view.getTag() != null) {
			switch (model) {
			case 0:
				bmp = ImageTool.getRemoteImage(view.getTag().toString());
				if (sizes.length != 0) {
					// com.nxt.nxtapp.common.LogUtil.syso("sizes[0]:"+sizes[0]);
					bmp = BitmapUtil.getBitmap(bmp, sizes[0], sizes[1]);
				}
				break;
			case 1:
				bmp = ImageTool.getRemoteImage(view.getTag().toString(), 1);
				break;
			case Constans.GONGLUE:
				bmp = ImageTool.getRemoteImage(view.getTag().toString(), Constans.GONGLUE);
				break;
			}
		}
		this.gView = view;
		return bmp;
	}

	protected void onPostExecute(Bitmap bm) {
		if (bm != null) {
			this.gView.setImageBitmap(bm);
			this.gView = null;
		}
	}

}
