package com.mindmachine.common.contract;

import com.gwm.annotation.retrofit.DNS;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import okhttp3.Dns;
@DNS
public class LocalDns implements Dns {
    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        return Dns.SYSTEM.lookup(hostname);
    }
}
