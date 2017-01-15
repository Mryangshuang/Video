package com.ys.com.video.Activitys;

import android.content.Intent;
import android.graphics.Color;
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
import com.umeng.socialize.UMShareAPI;
import com.ys.com.video.Fragments.BaiduFragment;
import com.ys.com.video.Fragments.BdzFragment;
import com.ys.com.video.Fragments.MusicFragment;
import com.ys.com.video.Fragments.QishiFragment;
import com.ys.com.video.Fragments.VideoFragment;
import com.ys.com.video.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseTabActivity {
    public static Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });
    @ViewInject(R.id.container)
    private ViewGroup container;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    public static List<Fragment> fragments;
    private FragmentManager manager;
    private FragmentTransaction ft;

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
                {R.mipmap.ic_launcher,R.mipmap.ic_launcher},
                {R.mipmap.ic_launcher,R.mipmap.ic_launcher},
                {R.mipmap.ic_launcher,R.mipmap.ic_launcher},
                {R.mipmap.ic_launcher,R.mipmap.ic_launcher},
                {R.mipmap.ic_launcher,R.mipmap.ic_launcher}};
//        int[] textColors = {getResources().getColor(R.color.tab_text),getResources().getColor(R.color.tab_text_selected)};
        int[] textColors = {Color.BLACK,Color.RED};
        init(viewPager,container, fragments,res,textColors);
//		默认为第几页
        switchTab(0);
    }



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
