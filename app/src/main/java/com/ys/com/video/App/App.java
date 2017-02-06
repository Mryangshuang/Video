package com.ys.com.video.App;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.ys.com.video.Activitys.SMSActivity;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by Administrator on 2017/1/15 0015.
 */

public class App extends Application {
    public int count;
//    注册 微信 QQ
    {
        PlatformConfig.setWeixin("wx463443266bb81f56", "10dacf33c247d9ae47ecb59b61f07576");
        PlatformConfig.setQQZone("1105453739", "fGQ3N1F4gj16hZ1x");
    }

//    在application中初始化sdk，这个初始化最好放在application的程序入口中，防止意外发生：
    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
    /**
     * Count 开始计数 计数完毕后重置
     */
    public void startCount(){
        SMSActivity.iscounting=true;
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 60; i++) {
                    try {
                        Thread.sleep(1000);
                        count++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                count=0;
                SMSActivity.iscounting=false;
            }
        }.start();

    }
}
