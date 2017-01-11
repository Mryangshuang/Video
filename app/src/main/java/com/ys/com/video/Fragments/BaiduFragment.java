package com.ys.com.video.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ys.com.video.Constants.Constant;
import com.ys.com.video.R;
import com.ys.com.video.UI.MyWebViewClient;
import com.ys.com.video.Activitys.MainActivity;

public class BaiduFragment extends Fragment {
    @ViewInject(R.id.webview)
    private static WebView webview;

    @OnClick({R.id.refresh})
    private void click(View view){
        MainActivity.fragments.set(0,new BaiduFragment());
    }
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_base, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewUtils.inject(this, view);
        //支持javascript
        webview.getSettings().setJavaScriptEnabled(true);
// 设置可以支持缩放 
        webview.getSettings().setSupportZoom(true);
// 设置出现缩放工具 
        webview.getSettings().setBuiltInZoomControls(true);
//扩大比例的缩放
        webview.getSettings().setUseWideViewPort(true);
//自适应屏幕
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);

//设置webviewClient 后 不会从手机自带浏览器接收数据
        webview.loadUrl(Constant.URL_YELLOW_PAGE);
        webview.setWebViewClient(new MyWebViewClient());
    }

    public static boolean clickBack(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return false;
    }
}