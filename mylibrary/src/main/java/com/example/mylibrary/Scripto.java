package com.example.mylibrary;



import java.lang.reflect.Proxy;
import java.util.ArrayList;

import com.example.mylibrary.converter.JavaToJsonConverter;
import com.example.mylibrary.converter.JsonToJavaConverter;
import com.example.mylibrary.java.JavaInterface;
import com.example.mylibrary.java.JavaInterfaceConfig;
import com.example.mylibrary.js.ScriptoProxy;
import com.example.mylibrary.utils.ScriptoAssetsJavaScriptReader;

import com.example.mylibrary.utils.ScriptoUtils;
import ohos.agp.components.webengine.JsCallback;
import ohos.agp.components.webengine.WebView;

/**
 * Creates proxy objects for JS-scripts. Adds JavaScript interfaces.
 */
public class Scripto {

    private static final String ASSETS_FOLDER_PATH = "file:///harmony_asset/";

    public interface ErrorHandler {
        void onError(ScriptoException error);
    }

    private WebView webView;
    private JavaToJsonConverter javaToJsonConverter;
    private JsonToJavaConverter jsonToJavaConverter;

    private ErrorHandler errorHandler;
    private ScriptoPrepareListener prepareListener;

    private ScriptoAssetsJavaScriptReader scriptoAssetsJavaScriptReader;
    private ArrayList<String> jsFiles;

    private Scripto(Builder builder) {
        this.webView = builder.webView;
        this.javaToJsonConverter = builder.javaToJsonConverter;
        this.jsonToJavaConverter = builder.jsonToJavaConverter;

        jsFiles = new ArrayList<>();
//        if (this.webView== null){
//            return;
//        }
        scriptoAssetsJavaScriptReader = new ScriptoAssetsJavaScriptReader(webView.getContext());

        initWebView(builder);
    }

    private void initWebView(Builder builder) {
        ScriptoWebViewClient scriptoWebViewClient = builder.scriptoWebViewClient;
        scriptoWebViewClient.setOnPageLoadedListener(new ScriptoWebViewClient.OnPageLoadedListener() {
            @Override
            public void onPageLoaded() {
                addJsScripts();
            }
        });
        webView.setWebAgent(scriptoWebViewClient);

        webView.getWebConfig().setJavaScriptPermit(true);
        //waits for the Scripto's readiness to work
        webView.addJsCallback("",new JsCallback() {

            @Override
            public String onCallback(String s) {
                ScriptoUtils.runOnUi(new Runnable() {
                    @Override
                    public void run() {
                        if (prepareListener != null) {
                            prepareListener.onScriptoPrepared();
                        }
                    }
                });
                return null;
            }
        });
    }

    private void addJsScripts() {
        StringBuilder jsScriptsListBuilder = new StringBuilder();

        int scriptsCount = jsFiles.size();
        //list of JS-file
        for (int i = 0; i < scriptsCount; i++) {
            jsScriptsListBuilder.append("\"").append(jsFiles.get(i)).append("\"");
            boolean isLasElement = i < scriptsCount - 1;
            if (isLasElement) {
                jsScriptsListBuilder.append(", ");
            }
        }

        webView.load(
                "javascript:(function() {" +
                "   var jsFiles = [" + jsScriptsListBuilder.toString() + "];" +
                "    " +
                "   jsFiles.forEach(function(jsFile, i, jsFiles) {" +
                "       var jsScript = document.createElement(\"script\");" +
                "       jsScript.setAttribute(\"src\", jsFile);" +
                "       document.head.appendChild(jsScript);" +
                "   });" +
                "   ScriptoPreparedListener.onScriptoPrepared();" + //notify java-library about readiness for work
                "})();");
    }

    public void addJsFile(String filePath) {
        jsFiles.add(filePath);
    }

    public void addJsFileFromAssets(String filePath) {
        jsFiles.add(ASSETS_FOLDER_PATH + filePath);
    }

    /**
     * Создает прокси из интерфейса для вызова JS-функций
     */
    public <T> T create(final Class<T> script) {
        ScriptoUtils.checkNotNull(script, "Script class can't be null");

        //если объект не является интерфейсом, выбрасываем исключение
        ScriptoUtils.validateScriptInterface(script);
        return (T) Proxy.newProxyInstance(script.getClassLoader(), new Class<?>[]{script}, new ScriptoProxy(this, script));
    }

    public WebView getWebView() {
        return webView;
    }

    public JavaToJsonConverter getJavaToJsonConverter() {
        return javaToJsonConverter;
    }

    public JsonToJavaConverter getJsonToJavaConverter() {
        return jsonToJavaConverter;
    }

    public void addInterface(String tag, Object jsInterface) {
        addInterface(tag, jsInterface, new JavaInterfaceConfig());
    }

    public void addInterface(String tag, Object jsInterface, JavaInterfaceConfig config) {
        if (tag == null) {
            throw new NullPointerException("Tag can't be null");
        }

        if (jsInterface == null) {
            throw new NullPointerException("JavaScript interface object can't be null");
        }

        if (config == null) {
            throw new NullPointerException("Config object can't be null");
        }

        webView.addJsCallback(tag, new JsCallback() {
            @Override
            public String onCallback(String s) {
                return null;
            }
        });
    }

    public void removeInterface(String tag) {
        webView.removeJsCallback(tag);
    }

    public void onError(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void onPrepared(ScriptoPrepareListener prepareListener) {
        this.prepareListener = prepareListener;
    }

    public static class Builder {

        public WebView webView;
        public ScriptoWebViewClient scriptoWebViewClient;
        public JavaToJsonConverter javaToJsonConverter;
        public JsonToJavaConverter jsonToJavaConverter;

        public Builder(WebView webView) {
            this.webView = webView;

            this.scriptoWebViewClient = new ScriptoWebViewClient();
            this.javaToJsonConverter = new JavaToJsonConverter();
            this.jsonToJavaConverter = new JsonToJavaConverter();
        }

        public Builder setWebViewClient(ScriptoWebViewClient scriptoWebViewClient) {
            ScriptoUtils.checkNotNull(scriptoWebViewClient, "ScriptoWebViewClient can not be null");
            this.scriptoWebViewClient = scriptoWebViewClient;
            return this;
        }

        public Builder setJavaToJsonConverter(JavaToJsonConverter javaToJsonConverter) {
            ScriptoUtils.checkNotNull(javaToJsonConverter, "Converter can not be null");
            this.javaToJsonConverter = javaToJsonConverter;
            return this;
        }

        public Builder setJsonToJavaConverter(JsonToJavaConverter jsonToJavaConverter) {
            ScriptoUtils.checkNotNull(jsonToJavaConverter, "Converter can not be null");
            this.jsonToJavaConverter = jsonToJavaConverter;
            return this;
        }

        public Scripto build() {
            return new Scripto(this);
        }

    }


}