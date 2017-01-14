package com.ys.com.video.Tool;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class TimeFileTool {
    public static String getTimeFile(){
        return "R"+new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+"";
    }
}
