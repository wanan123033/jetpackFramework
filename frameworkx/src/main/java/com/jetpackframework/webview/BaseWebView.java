package com.jetpackframework.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;

/**
 * 命令模式的WebView
 */
public class BaseWebView extends WebView {
    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(28)
    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init() {
        getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(this,"webview");
    }

    @JavascriptInterface
    public void jsInterface(String string){
        Log.e("TAG",string);
        WebViewCommond commond = new WebViewCommond();
        try {
            JSONObject jsonObject = new JSONObject(string);
            commond.commond = jsonObject.getString("commond");
            commond.param = jsonObject.getJSONObject("param");
        }catch (Exception e){
            e.printStackTrace();
        }
        handleWebViewCommond(commond);
    }

    public void execJavascript(String string){
        evaluateJavascript(string,null);
    }

    private void handleWebViewCommond(WebViewCommond commond) {
        WebViewServiceManager.getInstance().handleWebViewCommond(commond);
    }
}
