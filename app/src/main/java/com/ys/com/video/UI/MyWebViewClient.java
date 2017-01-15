package com.ys.com.video.UI;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String request) {
        view.loadUrl(request);
        return true;
    }
}
