package com.example.mylibrary;


import ohos.agp.components.webengine.WebAgent;
import ohos.agp.components.webengine.WebConfig;
import ohos.agp.components.webengine.WebView;

import java.util.logging.Logger;

public class ScriptoWebViewClient extends WebAgent {

    interface OnPageLoadedListener {
        void onPageLoaded1();
    }

    private OnPageLoadedListener onPageLoadedListener;

    void setOnPageLoadedListener(OnPageLoadedListener onPageLoadedListener) {
        this.onPageLoadedListener = onPageLoadedListener;
    }

    @Override
    public void onPageLoaded(WebView webView, String url) {
        super.onPageLoaded(webView, url);

        if (onPageLoadedListener != null) {
            onPageLoadedListener.onPageLoaded1();
        }
    }
}
