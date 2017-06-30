package com.ys.com.video.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ys.com.video.R;
import com.ys.com.video.Tool.ToastTool;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class PassWordActivity extends AppCompatActivity {
    private Intent intent;
    private int password;
    private String et;
    private SharedPreferences temp;

    @ViewInject(R.id.et_check)
    private EditText et_checkPSW;

    @ViewInject(R.id.et_change)
    private EditText et_changePSW;

    @ViewInject(R.id.et_change_two)
    private EditText et_changePSW_two;

    @ViewInject(R.id.box_one)
    private LinearLayout box_one;

    @ViewInject(R.id.box_two)
    private LinearLayout box_two;

    @ViewInject(R.id.box_three)
    private LinearLayout box_three;




    @OnClick({R.id.bt_check,R.id.bt_check,R.id.btn_change,R.id.btn_change_two})
    private void click(View view){
        switch (view.getId()){
            case R.id.bt_check:
                et=et_checkPSW.getText().toString();
                if(TextUtils.isEmpty(et)){
                    ToastTool.toast(this,"请输入密码！！");
                    return;
                }else{
//                    如果匹配
                    if(password==Integer.parseInt(et)){
                        intent.putExtra("PSW",true);
                    }else{
                        intent.putExtra("PSW",false);
                    }
                    setResult(1,intent);
                }
                finish();
                break;
            case R.id.btn_change:
//                如果原始密码不是空的
                if(!TextUtils.isEmpty(et_changePSW.getText().toString())){
                    if(Integer.parseInt(et_changePSW.getText().toString())==password){
                        box_two.setVisibility(View.INVISIBLE);
                        box_three.setVisibility(View.VISIBLE);
                    }else{
                        ToastTool.toast(this,"原始密码不正确！！！");
                    }
                }else{
//                    输入密码为空
                    ToastTool.toast(this,"请输入原始密码！！！");
                }
                break;
            case R.id.btn_change_two:
                //                    原始密码正确
                password=Integer.parseInt(et_changePSW_two.getText().toString());
                temp.edit().putInt("PSW",password).commit();
                intent.putExtra("PSW",true);
                setResult(1,intent);
                finish();
                break;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        ViewUtils.inject(this);

//        先提取密码，如果有密码，保存密码，如果没有密码，就设置默认秘密
        temp = getSharedPreferences("temp", MODE_PRIVATE);
        password=temp.getInt("PSW",0);
        if(password==0){
            temp.edit().putInt("PSW",123456).commit();
            password=123456;
        }
//        获取intent 判断是check 还是change
        intent=getIntent();
        if(intent.getIntExtra("style",0)==1){
            box_one.setVisibility(View.VISIBLE);
            box_two.setVisibility(View.INVISIBLE);
            box_three.setVisibility(View.INVISIBLE);
        }else if(intent.getIntExtra("style",0)==2){
            box_one.setVisibility(View.INVISIBLE);
            box_two.setVisibility(View.VISIBLE);
            box_three.setVisibility(View.INVISIBLE);

        }
    }
}
