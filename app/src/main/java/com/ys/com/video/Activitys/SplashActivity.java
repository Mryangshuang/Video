package com.ys.com.video.Activitys;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ys.com.video.R;

import static cn.jpush.android.api.JPushInterface.init;

public class SplashActivity extends AppCompatActivity {
    private AnimationDrawable drawable;
    @ViewInject(R.id.image_splash)
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ViewUtils.inject(this);
//        极光推送  初始化
        init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawable = (AnimationDrawable) imageView.getDrawable();
        drawable.start();
        jump();
    }

    /**
     * 调到主界面
     */
    private void jump() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }.start();

    }

    @Override
    protected void onDestroy() {
        Log.i("onDestroy", "onDestroy");
        drawable.stop();
        super.onDestroy();
    }
}
