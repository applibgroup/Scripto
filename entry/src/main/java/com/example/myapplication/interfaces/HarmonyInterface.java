package com.example.myapplication.interfaces;

import com.example.mylibrary.java.JavaScriptSecure;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

public class HarmonyInterface {
    private Context context;

    public HarmonyInterface(Context context) {
        this.context = context;
    }

    @JavaScriptSecure
    public void showToastMessage(String text) {
        new ToastDialog(context)
                .setText(text)
                .show();
    }
}
