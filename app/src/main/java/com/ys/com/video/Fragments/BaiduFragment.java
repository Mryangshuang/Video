package com.ys.com.video.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.ys.com.video.Activitys.MainActivity;
import com.ys.com.video.Constants.Constant;
import com.ys.com.video.R;
import com.ys.com.video.UI.MyWebViewClient;

public class BaiduFragment extends Fragment {
    @ViewInject(R.id.webview)
    private static WebView webview;

    private View view;

    @OnClick({R.id.share})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.share:
//                new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN)
//                        .withText("牛叉的分享")
//                        .withTargetUrl(webview.getOriginalUrl())
//                        .setCallback(umShareListener)
//                        .withMedia(new UMImage(getActivity(),R.mipmap.ic_launcher))
//                        .share();
                new ShareAction(getActivity())
                        .withText("牛叉的分享")
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                        .setCallback(umShareListener)
                        .withTargetUrl(webview.getOriginalUrl())
                        .withMedia(new UMImage(getActivity(),R.mipmap.ic_launcher))
                        .open();
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);

            Toast.makeText(getContext(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getContext(),platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getContext(),platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
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
        } else if (keyCode == KeyEvent.KEYCODE_BACK && !webview.canGoBack()) {
            System.exit(2);
            return true;
        }
        return false;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

}