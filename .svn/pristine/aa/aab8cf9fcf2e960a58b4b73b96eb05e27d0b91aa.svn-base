package com.nxt.ynt.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.nxt.nxtapp.common.CacheData;
import com.nxt.ynt.JNBMainActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;


public class ImageTool {
	private static String url_str;

	// 设置图片大
	public static Bitmap getScaleImg(Bitmap bm, int newWidth, int newHeight) {
		// 图片源
		// Bitmap bm = BitmapFactory.decodeStream(getResources()
		// .openRawResource(id));
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth1 = newWidth;
		int newHeight1 = newHeight;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;

	}

	// 设置seekbar thumb
	public static Drawable setthumb(Context context, int src) {
		Drawable drawable = context.getResources().getDrawable(src);
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap bitmap = bd.getBitmap();

		bitmap = ImageTool.getScaleImg(bitmap, 55, 55);

		BitmapDrawable bd2 = new BitmapDrawable(bitmap);

		return bd2;
	}

	public static Bitmap getRemoteImage(String urlstr) {
		InputStream stream = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.RGB_565;
		options.inInputShareable = true;
		options.inPurgeable = true;
		try {

			String fileName = JNBMainActivity.PIC_PATH + File.separator
					+ CacheData.MD5(urlstr)
					+ urlstr.substring(urlstr.lastIndexOf("."));
			Bitmap bmp = null;
			com.nxt.nxtapp.common.LogUtil.LogI("vpath", urlstr);
			if(new File(urlstr).exists()){
				
				 BitmapFactory.Options opts =  new  BitmapFactory.Options();
                 opts.inJustDecodeBounds =  true ;
                 
                 com.nxt.nxtapp.common.LogUtil.syso("fileName:"+urlstr);
            	 BitmapFactory.decodeFile(urlstr,opts);
            	 opts.inSampleSize = BitmapUtil.computeSampleSize(opts, - 1 , JNBMainActivity.widthPx*JNBMainActivity.HeigtPx );       
                 opts.inJustDecodeBounds =  false ;
                 try  {
                	 bmp = BitmapFactory.decodeFile(urlstr, opts);
                      }  catch  (OutOfMemoryError err) {
                    	  err.printStackTrace();
                      }
				
                 com.nxt.nxtapp.common.LogUtil.LogI("vpath", "bendiyou......"+bmp);
				return bmp;
			}
			
			if (new File(fileName).exists()) {
				// options.inJustDecodeBounds = true;
				bmp = BitmapFactory.decodeFile(fileName, options);
			} else {
				com.nxt.nxtapp.common.LogUtil.syso("downloading.................................");
				URL url = new URL(urlstr);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				stream = conn.getInputStream();
				// 图片加载处理
				bmp = BitmapFactory.decodeStream(stream, null, options);
				com.nxt.nxtapp.common.LogUtil.syso("bmp:@@@@@@@@@@@@@@"+bmp);
				FileUilts.saveFile(bmp, fileName,
						urlstr.substring(1 + urlstr.lastIndexOf(".")));
			}

			return bmp;

		} catch (Exception e) {

			com.nxt.nxtapp.common.LogUtil.LogD("DEBUGTAG", "Oh noooz an error..." + e.getMessage());

		} finally {
			if (stream != null)
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return null;

	}

	public static Bitmap getRemoteImage(String urlstr, int model) {
		InputStream stream = null;
		try {

			String fileName = JNBMainActivity.PIC_PATH + File.separator
					+ CacheData.MD5(urlstr)
					+ urlstr.substring(urlstr.lastIndexOf("."));
			
			Bitmap bmp = null;
			if (new File(fileName).exists()) {
				bmp = BitmapFactory.decodeFile(fileName);
			} else {
		
					url_str = Constans.ROOT_URL + urlstr;

					URL url = new URL(url_str);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setDoInput(true);
					conn.connect();
					stream = conn.getInputStream();
					// 图片加载处理
					Options options = new Options();
					options.inJustDecodeBounds = false;
					options.inSampleSize = ImageTool.calculateSampleSize(
							options.outWidth, options.outHeight, 290, 420,
							ScalingLogic.CROP);
					bmp = BitmapFactory.decodeStream(stream);
					bmp = ImageTool.createScaledBitmap(bmp, 380, 544,
							ScalingLogic.CROP);
					FileUilts.saveFile(bmp, fileName,
							urlstr.substring(1 + urlstr.lastIndexOf(".")));
				
			}

			return bmp;

		} catch (Exception e) {

			com.nxt.nxtapp.common.LogUtil.LogD("DEBUGTAG", "Oh noooz an error..." + e.getMessage());

		} finally {
			if (stream != null)
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return null;

	}

	/*
	 * 图像通过fit方法缩放。图片已被缩小到适合的尺寸和高度，
	 * 结果是小于想要的高度。右边的图像：图像crop方法缩放。图像已被缩放到适应至少想要的尺寸。 因此原图已被裁剪，切割了成左边和右边二部分。
	 */
	public static Bitmap createScaledBitmap(Bitmap unscaledBitmap,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {

		Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);

		Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);

		Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(),
				dstRect.height(), Config.ARGB_8888);

		Canvas canvas = new Canvas(scaledBitmap);

		canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(
				Paint.FILTER_BITMAP_FLAG));
		return scaledBitmap;

	}

	/*
	 * 创建源矩形
	 */
	public static Rect calculateSrcRect(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {

		if (scalingLogic == ScalingLogic.CROP) {

			final float srcAspect = (float) srcWidth / (float) srcHeight;

			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {

				final int srcRectWidth = (int) (srcHeight * dstAspect);

				final int srcRectLeft = (srcWidth - srcRectWidth) / 2;

				return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth,
						srcHeight);

			} else {

				final int srcRectHeight = (int) (srcWidth / dstAspect);

				final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;

				return new Rect(0, scrRectTop, srcWidth, scrRectTop
						+ srcRectHeight);

			}

		} else {

			return new Rect(0, 0, srcWidth, srcHeight);

		}

	}

	// 创建目标矩形
	public static Rect calculateDstRect(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;
			if (srcAspect > dstAspect) {
				return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
			} else {
				return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
			}
		} else {
			return new Rect(0, 0, dstWidth, dstHeight);
		}
	}

	public static Bitmap decodeFile(String pathName, int dstWidth,
			int dstHeight, ScalingLogic scalingLogic) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateSampleSize(options.outWidth,
				options.outHeight, dstWidth, dstHeight, scalingLogic);
		Bitmap unscaledBitmap = BitmapFactory.decodeFile(pathName, options);
		return unscaledBitmap;
	}

	public static int calculateSampleSize(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;
			if (srcAspect > dstAspect) {
				return srcWidth / dstWidth;
			} else {
				return srcHeight / dstHeight;
			}
		} else {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;
			if (srcAspect > dstAspect) {
				return srcHeight / dstHeight;
			} else {
				return srcWidth / dstWidth;
			}
		}
	}

	public static Bitmap getResolveEitmap(String pathname, int dstWidth,
			int dstHeight, ScalingLogic scalingLogic) {
		Bitmap unscaledBitmap = decodeFile(pathname, dstWidth, dstHeight,
				scalingLogic);
		Bitmap scaledBitmap = createScaledBitmap(unscaledBitmap, dstWidth,
				dstHeight, scalingLogic);
		return scaledBitmap;
	}

}
