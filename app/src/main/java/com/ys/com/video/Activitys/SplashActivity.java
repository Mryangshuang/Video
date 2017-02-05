package com.ys.com.video.Activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ys.com.video.R;
import com.ys.com.video.Tool.ToastTool;

import cn.smssdk.SMSSDK;

import static cn.jpush.android.api.JPushInterface.init;

public class SplashActivity extends AppCompatActivity {
    private AnimationDrawable drawable;
    @ViewInject(R.id.image_splash)
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ViewUtils.inject(this);
//        极光推送  初始化
        init(this);
        setPermission();
//        短信初始化
        SMSSDK.initSDK(this, "1b129e6315c0a", "714eac7a4f9b6135b7fd7692b33fa505");
    }

    /***
     * 对于安卓6.0以上的进行权限申请
     */
    private void setPermission() {
        if(Build.VERSION.SDK_INT>=23){
            int i =ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE);
            if(i!= PackageManager.PERMISSION_GRANTED){
                //            如果没有获取权限  有没弹出对话框
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.STORAGE)){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                    dialog.setMessage("该相册需要赋予访问存储的权限，不开启将无法正常工作！");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                            dialog.cancel();
                        }
                    });
                    dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.create().show();
                }else{
//                如果没有权限就申请   而且可以弹出对话框
                    ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        }
    }

    /**
     * 权限申请回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1&&permissions[0].equals(Manifest.permission_group.STORAGE)&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            ToastTool.toast(this,"获取内存卡读写权限成功");
//            TODO
        }
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
        drawable.stop();
        super.onDestroy();
    }
}
