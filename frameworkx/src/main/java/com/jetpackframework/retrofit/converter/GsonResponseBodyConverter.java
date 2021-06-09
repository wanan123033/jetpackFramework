package com.jetpackframework.retrofit.converter;

import com.google.gson.Gson;
import com.jetpackframework.retrofit.ResponseBodyConverter;

import java.io.IOException;

import okhttp3.Response;

public final class GsonResponseBodyConverter<T> implements ResponseBodyConverter<Response, T> {
  private final Gson gson;
  private Class<T> tClass;

  public GsonResponseBodyConverter(Gson gson) {
    this.gson = gson;
  }

  public T convert(Response value) throws IOException {
      try {
        return gson.fromJson(value.body().string(),tClass);
      }finally {
        value.close();
      }
  }

  @Override
  public void setClass(Class<T> result) {
    this.tClass = result;
  }
}
