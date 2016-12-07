package com.nxt.nxtapp.ui.adapter.publicmsg;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nxt.nxtapp.R;

public class ImageAdapter  extends BaseAdapter
{
    // 定义Context
    private Context     mContext;
    // 定义整型数组 即图片源
    private List<Bitmap> bitmaps;
    public ImageAdapter(Context c)
    {
        mContext = c;
    }
 
    public ImageAdapter(Context c,List<Bitmap> bitmaps)
    {
        mContext = c;
        this.bitmaps = bitmaps;
    }
    // 获取图片的个数
    public int getCount()
    {
        return bitmaps.size();
    }
 
    // 获取图片在库中的位置
    public Object getItem(int position)
    {
        return position;
    }
 
    // 获取图片ID
    public long getItemId(int position)
    {
        return position;
    }
 
 
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
       
        if (convertView == null)
        {
            // 给ImageView设置资源
            imageView = new ImageView(mContext);
            // 设置布局 图片120×120显示
            imageView.setLayoutParams(new GridView.LayoutParams(85,85));
            // 设置显示比例类型
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        
        if(position==bitmaps.size()-1){
        	imageView.setImageResource(R.drawable.add_pic);
        }else{
        	 imageView.setImageBitmap(bitmaps.get(position+1));
        }
       
        return imageView;
    }
 
}

