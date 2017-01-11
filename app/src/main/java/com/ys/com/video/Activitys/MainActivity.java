package com.ys.com.video.Activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseTabActivity {
    @ViewInject(R.id.container)
    private ViewGroup container;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;


    public static List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        fragments = new ArrayList<Fragment>();
        fragments.add(new BaiduFragment());
        fragments.add(new QishiFragment());
        fragments.add(new BdzFragment());
        fragments.add(new VideoFragment());
        fragments.add(new MusicFragment());
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
