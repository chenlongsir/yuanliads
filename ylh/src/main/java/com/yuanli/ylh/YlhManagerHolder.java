package com.yuanli.ylh;

import android.annotation.SuppressLint;
import android.content.Context;

import com.qq.e.comm.managers.GDTAdSdk;
import com.yuanli.ylh.constants.YlhBean;
import com.yuanli.ylh.utils.InitUtils;

public class YlhManagerHolder {
    private static boolean sInit;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static void init(Context context, YlhBean bean) {
        YlhManagerHolder.context = context;
        InitUtils.init(bean);
        doInit(context);
    }

    // 清除回调，避免内存泄漏
    public static void clear(){
    }


    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context) {
        if (!sInit) {
            initKSSDK(context);
        }
    }

    public static void initKSSDK(Context appContext) {
        GDTAdSdk.init(appContext,InitUtils.getBean().getAppId());
        sInit = true;
    }

}
