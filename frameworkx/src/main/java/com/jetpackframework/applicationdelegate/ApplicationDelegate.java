package com.jetpackframework.applicationdelegate;

import android.app.Application;
import android.os.Handler;

public interface ApplicationDelegate extends ApplicationLifeCycle {
    Handler getHandler();
    void init(Application application);
    String getPackageName();
}
