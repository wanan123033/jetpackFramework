package com.jetpackframework.retrofit;

import java.io.IOException;

public interface ResponseBodyConverter<F, T> {
    T convert(F value) throws IOException;

    void setClass(Class<T> result);
}
