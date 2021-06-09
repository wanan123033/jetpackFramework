package com.jetpackframework.http;

import java.util.LinkedList;
import java.util.List;

public class HttpCache {
    private static HttpCache cache = new HttpCache();
    private List<HttpTask> tasks;
    private HttpCache(){
        tasks = new LinkedList<>();
    }

    public synchronized static HttpCache getCache(){
        return cache;
    }

    public void add(HttpTask task){
        tasks.add(task);
    }

    public HttpTask getTask(){
        return tasks.remove(0);
    }
    public boolean hasNext(){
        return !tasks.isEmpty();
    }
}
