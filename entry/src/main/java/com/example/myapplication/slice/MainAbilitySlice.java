package com.example.myapplication.slice;

import com.example.myapplication.ResourceTable;
import com.example.myapplication.User;
import com.example.myapplication.interfaces.HarmonyInterface;
import com.example.myapplication.interfaces.PreferencesInterface;
import com.example.myapplication.script.UserInfoScript;
import com.example.myapplication.utils.AssetsReader;
import com.example.mylibrary.Scripto;
import com.example.mylibrary.ScriptoException;
import com.example.mylibrary.ScriptoPrepareListener;
import com.example.mylibrary.java.JavaInterfaceConfig;
import com.example.mylibrary.js.JavaScriptCallErrorCallback;
import com.example.mylibrary.js.JavaScriptCallResponseCallback;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.webengine.WebView;
import ohos.agp.window.dialog.ToastDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbilitySlice extends AbilitySlice {
    private Scripto scripto;
    private UserInfoScript userInfoScript;
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "Scripto");
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        WebView webView = (WebView) findComponentById(ResourceTable.Id_webview);
        Button btn = (Button)  findComponentById(ResourceTable.Id_Click);
        btn.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                showUserInfo();
            }
        });

        scripto = new Scripto.Builder(webView).build();
        scripto.addInterface("Harmony", new HarmonyInterface(this), new JavaInterfaceConfig().enableAnnotationProtection(true));
        scripto.addInterface("Preferences", new PreferencesInterface(this), new JavaInterfaceConfig());
        scripto.addJsFileFromAssets("scripto/scripto.js");
        scripto.addJsFileFromAssets("interfaces/android_interface.js");
        scripto.addJsFileFromAssets("interfaces/preferences_interface.js");
        scripto.addJsFileFromAssets("test.js");

        userInfoScript = scripto.create(UserInfoScript.class);
        scripto.onError(new Scripto.ErrorHandler() {

            @Override
            public void onError(ScriptoException error) {

                new ToastDialog(getApplicationContext()).setText( "Error: " + error.getMessage()).show();
            }
        });

        scripto.onPrepared(new ScriptoPrepareListener() {
            @Override
            public void onScriptoPrepared() {
                HiLog.debug(LABEL, "Scripto is prepared");
            }
        });

        String html = AssetsReader.readFileAsText(this, "test.html");
        webView.load("file:///android_asset/", html, "text/html", "utf-8", null);
    }
    public void showUserInfo() {
        userInfoScript.getUser()
                .onResponse(new JavaScriptCallResponseCallback<User>() {
                    @Override
                    public void onResponse(User user) {
                        new ToastDialog(getApplicationContext()).setText(user.getUserInfo()).show();

                    }
                }).onError(new JavaScriptCallErrorCallback() {
            @Override
            public void onError(ScriptoException error) {
                new ToastDialog(getApplicationContext()).setText( "User info load error: " + error.getMessage()).show();

            }
        }).call();
    }
    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
