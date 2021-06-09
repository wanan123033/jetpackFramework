package com.jetpackframework.databus;

public class DataBusEventBean<T> {
    private String event;
    private T data;

    public DataBusEventBean(String event) {
        this.event = event;
    }

    public DataBusEventBean(String event, T data) {
        this.event = event;
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
