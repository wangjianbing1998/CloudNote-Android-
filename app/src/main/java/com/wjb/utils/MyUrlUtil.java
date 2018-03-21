package com.wjb.utils;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class MyUrlUtil {
    public static String getURL(){
        return "http://"+IPUtils.getIPAddress()+":8080/";
    }
}
