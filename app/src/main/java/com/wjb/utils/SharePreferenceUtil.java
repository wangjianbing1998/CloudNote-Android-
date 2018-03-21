package com.wjb.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;

import java.security.PrivateKey;
import java.util.Set;

/**
 * Created by Administrator on 2017/7/12 0012.
 * 读写的数据保存类
 */

public class SharePreferenceUtil {

    public static final String SharePreference_Name="cloudnote";
    public static String getString(Context pContext,String key){
        SharedPreferences mSharedPreferences = pContext.getSharedPreferences(SharePreference_Name, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key, "");
    }
    public static boolean getBoolean(Context pContext,String key){
        SharedPreferences mSharedPreferences = pContext.getSharedPreferences(SharePreference_Name, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(key, false);
    }
    public static int getInt(Context pContext,String key){
        SharedPreferences mSharedPreferences = pContext.getSharedPreferences(SharePreference_Name, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(key, 0);
    }
    public static Set<String> getStringSet(Context pContext, String key){
        SharedPreferences mSharedPreferences = pContext.getSharedPreferences(SharePreference_Name, Context.MODE_PRIVATE);
        return mSharedPreferences.getStringSet(key, null);
    }
    public static void putString(Context pContext,String key,String value){
        SharedPreferences mSharedPreferences = pContext.getSharedPreferences(SharePreference_Name, Context.MODE_PRIVATE);
        mSharedPreferences.edit().putString(key, value).commit();
    }
    public static void putBoolean(Context pContext,String key,boolean value){
        SharedPreferences mSharePreference = pContext.getSharedPreferences(SharePreference_Name, Context.MODE_PRIVATE);
        mSharePreference.edit().putBoolean(key, value).commit();
    }
    public static void putInt(Context pContext,String key,int value){
        SharedPreferences mSharedPreferences = pContext.getSharedPreferences(SharePreference_Name, Context.MODE_PRIVATE);
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    public static void putStringSet(Context pContext, String key, Set<String> value){
        SharedPreferences mSharePreference = pContext.getSharedPreferences(SharePreference_Name, Context.MODE_PRIVATE);
        mSharePreference.edit().putStringSet(key, value).commit();
    }

}
