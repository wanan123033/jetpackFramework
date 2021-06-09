package com.jetpackframework.arouter;

public interface RouterInterceptor {
    /**
     *
     * @param toUri   去哪里
     * @param fromUrl 从哪里来
     * @return
     */
    boolean interceptor(String toUri, String fromUrl);

}