package com.ys.com.video.Activitys;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ys.com.video.Fragments.BaiduFragment;
import com.ys.com.video.Fragments.BdzFragment;
import com.ys.com.video.Fragments.MusicFragment;
import com.ys.com.video.Fragments.QishiFragment;
import com.ys.com.video.Fragments.VideoFragment;
import com.ys.com.video.R;
import com.ys.com.video.Receiver.NetWorkStateReceiver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseTabActivity {
    @ViewInject(R.id.container)
    private ViewGroup container;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    public static List<Fragment> fragments;
    private FragmentManager manager;
    private FragmentTransaction ft;
    //网络变化相关
    BroadcastReceiver networkstatereceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        fragments = new ArrayList<Fragment>();
        Fragment baidu=new BaiduFragment();
        Fragment qishi= new QishiFragment();
        Fragment bdz=new BdzFragment();
        Fragment video=new VideoFragment();
        Fragment music=new MusicFragment();

        fragments.add(baidu);
        fragments.add(qishi);
        fragments.add(bdz);
        fragments.add(video);
        fragments.add(music);

        int[][] res = {
                {R.mipmap.ic_launcher,R.mipmap.ic_baidu},
                {R.mipmap.ic_launcher,R.mipmap.ic_horse},
                {R.mipmap.ic_launcher,R.mipmap.ic_sword},
                {R.mipmap.ic_launcher,R.mipmap.ic_video},
                {R.mipmap.ic_launcher,R.mipmap.ic_music}};
//        int[] textColors = {getResources().getColor(R.color.tab_text),getResources().getColor(R.color.tab_text_selected)};
        int[] textColors = {Color.BLACK,Color.RED};
        init(viewPager,container, fragments,res,textColors);
//		默认为第几页
        switchTab(0);
        //网络变化相关
        initNetWork();
    }

    /**
     * 注册网络变化监听
     */
    public void initNetWork() {
        if(networkstatereceiver==null){
            networkstatereceiver=new NetWorkStateReceiver();
        }
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkstatereceiver,filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(networkstatereceiver);
        super.onDestroy();
    }

    /**
     * 返回键相关
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(curIndex==0){
            BaiduFragment.clickBack(keyCode, event);
            return false;
        }else if(curIndex==1){
            QishiFragment.clickBack(keyCode, event);
            return false;
        }else if(curIndex==2){
            BdzFragment.clickBack(keyCode, event);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
