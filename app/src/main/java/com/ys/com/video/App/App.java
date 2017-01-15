package com.ys.com.video.App;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;


/**
 * Created by Administrator on 2017/1/15 0015.
 */

public class App extends Application {
//    注册QQ
    {
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("1105453739", "fGQ3N1F4gj16hZ1x");
    }

//    在application中初始化sdk，这个初始化最好放在application的程序入口中，防止意外发生：
    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
    }
}
