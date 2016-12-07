package com.nxt.nxtapp.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.format.DateUtils;
import android.text.format.Time;

public class TimeTools {
	//获取系统当前时间
	public static String getCurrentTime(int mode){
		SimpleDateFormat df = null;//设置日期格式
		switch(mode){
		case 0://yyyy-MM-dd HH:mm:ss
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		case 1:
			df = new SimpleDateFormat("MM.dd");
			break;
		case 2:
			df = new SimpleDateFormat("yyyyMMddHHmmss");
			break;
		default: break;
		}
		return df.format(new Date());
	}
	
	//获取时间值
    public static long getTimestamp(String timestr) {
        long rand = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d2 = null;
            try {
                d2 = sdf.parse(timestr);//将String to Date类型
                rand = d2.getTime();
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rand;
    }

    //按照短信时间显示
    public static String formatTimeStampString(Context context, long when, boolean fullFormat) {  
        Time then = new Time();  
        then.set(when);  
        Time now = new Time();  
        now.setToNow();  
  
        // Basic settings for formatDateTime() we want for all cases.  
        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT |  
                           DateUtils.FORMAT_ABBREV_ALL |  
                           DateUtils.FORMAT_CAP_AMPM;  
  
        // If the message is from a different year, show the date and year.  
        if (then.year != now.year) {  
            format_flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;  
        } else if (then.yearDay != now.yearDay) {  
            // If it is from a different day than today, show only the date.  
            format_flags |= DateUtils.FORMAT_SHOW_DATE;  
        } else {  
            // Otherwise, if the message is from today, show the time.  
            format_flags |= DateUtils.FORMAT_SHOW_TIME;  
        }  
  
        // If the caller has asked for full details, make sure to show the date  
        // and time no matter what we've determined above (but still make showing  
        // the year only happen if it is a different year from today).  
        if (fullFormat) {  
            format_flags |= (DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);  
        }  
  
        return DateUtils.formatDateTime(context, when, format_flags);  
    }  
}
