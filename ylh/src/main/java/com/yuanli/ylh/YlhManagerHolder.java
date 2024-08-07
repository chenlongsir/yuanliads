package com.yuanli.ylh;

import android.annotation.SuppressLint;
import android.content.Context;

import com.qq.e.comm.managers.GDTAdSdk;
import com.yuanli.ylh.constants.YlhBean;
import com.yuanli.ylh.utils.InitUtils;

public class YlhManagerHolder {
    private static boolean sInit;
    private static boolean isFail;
    private static InitListener initListener;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static void init(Context context, YlhBean bean) {
        YlhManagerHolder.context = context;
        InitUtils.init(bean);
        doInit(context);
    }


    public interface InitListener{
        void onSuccess();
        void onError(String msg);
    }

    public static void setInitListener(InitListener initListener){
        YlhManagerHolder.initListener = initListener;
        if (sInit){
            initListener.onSuccess();
        }else if (isFail){
            doInit(context);
        }
    }

    // 清除回调，避免内存泄漏
    public static void clear(){
        initListener = null;
    }


    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context) {
        if (!sInit) {
            initKSSDK(context);
        }
    }

    public static void initKSSDK(Context appContext) {
        GDTAdSdk.init(context,InitUtils.getBean().getAppId());
    }

}
