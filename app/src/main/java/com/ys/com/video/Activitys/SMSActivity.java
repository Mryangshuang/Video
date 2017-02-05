package com.ys.com.video.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ys.com.video.R;
import com.ys.com.video.Tool.ToastTool;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

import static cn.smssdk.SMSSDK.getSupportedCountries;
import static cn.smssdk.SMSSDK.getVerificationCode;
import static cn.smssdk.SMSSDK.submitVerificationCode;


public class SMSActivity extends AppCompatActivity {
    private String phone;
    @ViewInject(R.id.et_phone)
    private EditText et_phone;
    @ViewInject(R.id.btn_phone)
    private Button btn_phone;

    @ViewInject(R.id.et_psw)
    private EditText et_psw;
    @ViewInject(R.id.btn_submit)
    private Button btn_submit;

    @ViewInject(R.id.ll1)
    private LinearLayout ll1;
    @ViewInject(R.id.ll2)
    private LinearLayout ll2;

    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功跳转界面
                    Intent intent = new Intent(SMSActivity.this, RecorderActivity.class);
                    startActivity(intent);
                    finish();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功  就变到提交界面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ll1.setVisibility(View.INVISIBLE);
                            ll2.setVisibility(View.VISIBLE);
                        }
                    });
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };


    @OnClick({R.id.btn_phone, R.id.btn_submit})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_phone:
                if (TextUtils.isEmpty(et_phone.getText())||et_phone.getText().toString().length()!=11) {
                    ToastTool.toast(this, "请输入正确号码");
                    return;
                }

                OnSendMessageHandler listener=new OnSendMessageHandler() {
                    @Override
                    public boolean onSendMessage(String s, String s1) {
                        return false;
                    }
                };
                phone=et_phone.getText().toString();
//                提交短信请求
                getVerificationCode("86",phone,listener);
                break;
            case R.id.btn_submit:
//                提交短信验证码，在监听中返回
                submitVerificationCode("86",phone,et_psw.getText().toString());
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ViewUtils.inject(this);
        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.INVISIBLE);
//        前提必须进行调用
        getSupportedCountries();
        SMSSDK.registerEventHandler(eh); //注册短信回调

    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(eh); //注册短信回调
        super.onDestroy();
    }
}
