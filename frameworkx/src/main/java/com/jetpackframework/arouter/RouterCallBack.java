package com.jetpackframework.arouter;

import android.content.Context;

public interface RouterCallBack {
    void notFound(Context context);
    void onError(Exception ex);
}
