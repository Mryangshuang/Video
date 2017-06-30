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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.client.android.decode.Intents;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.ys.com.video.Activitys.PassWordActivity;
import com.ys.com.video.Constants.Constant;
import com.ys.com.video.R;
import com.ys.com.video.Tool.ToastTool;
import com.ys.com.video.UI.MyWebViewClient;

public class BaiduFragment extends Fragment {
    @ViewInject(R.id.webview)
    private static WebView webview;
    private View view;
    private static LinearLayout buttons;
    private static ImageView share;
    private static int count;
    private int index;

    private String[] URLS={Constant.URL_YELLOW_PAGE_ONE,Constant.URL_YELLOW_PAGE_TWO,
            Constant.URL_YELLOW_PAGE_THREE};

    @OnClick({R.id.share,R.id.baidu,R.id.jp_one,R.id.jp_two,R.id.jp_three,R.id.change_password})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.share:
//                只用QQ分享
//                new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.QQ)
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
                Log.i("conn",webview.getOriginalUrl());
                break;
            case R.id.baidu:
                LoadURL(Constant.URL_BAIDU);
                break;
            case R.id.jp_one:
                index=0;
                checkPassword(1,0);
                break;
            case R.id.jp_two:
                index=1;
                checkPassword(1,0);
                break;
            case R.id.jp_three:
                index=2;
                checkPassword(1,0);
                break;
            case R.id.change_password:
                checkPassword(2,1);
                break;

        }
    }

    /**
     * 第一个参数 1：是检验密码  2：更改密码
     * 第二个参数 0：检验密码的请求码  1：更改密码的请求码  在onActivityResult使用
     * @param style
     * @param RequestNum
     */
    private void checkPassword(int style,int RequestNum) {
        Intent intent=new Intent(getActivity(),PassWordActivity.class);
        intent.putExtra("style",style);
        startActivityForResult(intent,RequestNum);
    }

    /**
     * 登录相应的网址URL
     * @param url
     */
    private void LoadURL(String url) {
        count=0;
        buttons.setVisibility(View.INVISIBLE);
        webview.setVisibility(View.VISIBLE);
        webview.loadUrl(url);
        share.setVisibility(View.VISIBLE);
    }

    /**
     * 分享成功反馈
     */
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_base,null);
//        找到按钮集   控制显示或是消失；
        buttons=(LinearLayout)view.findViewById(R.id.buttons);
        buttons.setVisibility(View.VISIBLE);
        share= (ImageView) view.findViewById(R.id.share);
        share.setVisibility(View.INVISIBLE);
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
        webview.setWebViewClient(new MyWebViewClient());
//        计数归零
        count=0;
    }

    public static boolean clickBack(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            count=0;
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_BACK && !webview.canGoBack()){
            if(count==0){
                buttons.setVisibility(View.VISIBLE);
                share.setVisibility(View.GONE);
                webview.setVisibility(View.INVISIBLE);
                webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                webview.clearCache(true);
                webview.clearHistory();
                count++;
            }
            else{
                count=0;
                System.exit(2);
            }
            return true;
        }
        return  false;
    }

    /**
     * 友盟分享  和更改密码相关
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);

        if(requestCode==0&&resultCode==1){
            if(!data.getBooleanExtra("PSW",false)){
                ToastTool.toast(getContext(),"密码错误，请重新输入！！");
            }else{
                ToastTool.toast(getContext(),"密码正确,敬请观看！！");
                LoadURL(URLS[index]);
            }
        }
        if(requestCode==1&&resultCode==1){
            if(data.getBooleanExtra("PSW",false)){
                ToastTool.toast(getContext(),"密码修改成功");
            }else{
                ToastTool.toast(getContext(),"密码修改失败");
            }
        }
    }
}
