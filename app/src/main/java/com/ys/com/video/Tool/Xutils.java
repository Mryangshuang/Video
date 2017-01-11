package com.ys.com.video.Tool;

import android.content.Context;
import android.os.Environment;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;

/**
 * Created by Administrator on 2017/1/1 0001.
 */

public class Xutils {
    private static HttpUtils httpUtils;
    private static BitmapUtils bitmapUtils;

    public static HttpUtils getHttpUtils(){
        if(httpUtils==null){
            httpUtils=new HttpUtils();
            httpUtils.configCurrentHttpCacheExpiry(0);
            httpUtils.configRequestThreadPoolSize(5);
            httpUtils.configSoTimeout(4000);
            httpUtils.configTimeout(4000);
        }
        return httpUtils;
    }
    public static BitmapUtils getBitmapUtils(Context context){
        if(bitmapUtils==null){
            String diskCachePath= Environment.getExternalStorageDirectory().getPath()+"/"+context.getPackageName()+"/cache/imgs";
            bitmapUtils = new BitmapUtils(context.getApplicationContext(), diskCachePath);
            int maxSize = (int) (Runtime.getRuntime().maxMemory()/8);
            bitmapUtils.configDefaultCacheExpiry(maxSize);
        }
        return bitmapUtils;
    }
}
