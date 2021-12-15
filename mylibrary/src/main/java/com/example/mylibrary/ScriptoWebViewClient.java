package com.example.mylibrary;


import ohos.agp.components.webengine.WebAgent;
import ohos.agp.components.webengine.WebView;

public class ScriptoWebViewClient extends WebAgent {

    interface OnPageLoadedListener {
        void onPageLoaded();
    }

    private OnPageLoadedListener onPageLoadedListener;

    void setOnPageLoadedListener(OnPageLoadedListener onPageLoadedListener) {
        this.onPageLoadedListener = onPageLoadedListener;
    }


    @Override
    public void onPageLoaded(WebView webView, String url) {
        super.onPageLoaded(webView, url);
                if (onPageLoadedListener != null) {
            onPageLoadedListener.onPageLoaded();
        }
    }



}
