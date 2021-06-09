package com.jetpackframework.arouter;

import java.util.Map;

public interface RouterInitialization extends RouterCallBack {
    void onInit(Map<String, String> routerMap);
}
