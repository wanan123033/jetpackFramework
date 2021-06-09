package com.jetpackframework.webview;

import org.json.JSONObject;

public class WebViewCommond {
    String commond;
    JSONObject param;

    @Override
    public String toString() {
        return "WebViewCommond{" +
                "commond='" + commond + '\'' +
                ", param=" + param.toString() +
                '}';
    }
}
