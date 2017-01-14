package com.ys.com.video.Tool;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

public class TimeFormat {
	@SuppressLint("SimpleDateFormat")
	public static  String timeformat(String time,String mat){
		SimpleDateFormat format=new SimpleDateFormat(mat);
		String format2 = format.format(Long.valueOf(time));
		return format2;
	}
}
