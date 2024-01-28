package net.geekh.wiki.util;

import java.io.Serializable;

/**
 * @Author YIHONG
 * @Description
 * @Date 2024/1/28 19:44
 */
public class RequestContext implements Serializable {
    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }
}
