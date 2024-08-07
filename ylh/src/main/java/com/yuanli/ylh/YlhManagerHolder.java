package com.yuanli.ylh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.yuanli.base.BuildConfig;
import com.yuanli.ylh.constants.YlhBean;
import com.yuanli.ylh.utils.InitUtils;

public class YlhManagerHolder {
    private static boolean sInit;
    private static boolean isFail;
    private static InitListener initListener;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static KsLoadManager get() {
        if (!sInit) {
            throw new RuntimeException("TTAdSdk is not init, please check.");
        }
        return KsAdSDK.getLoadManager();
    }

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
//        4.560.1430及以后版本
        GDTAdSdk.initWithoutStart(appContext, "您在腾讯联盟开发者平台的APPID"); // 该接口不会采集用户信息
// 调用initWithoutStart后请尽快调用start，否则可能影响广告填充，造成收入下降
        GDTAdSdk.start(new GDTAdSdk.OnStartListener() {
            @Override
            public void onStartSuccess() {
                // 推荐开发者在onStartSuccess回调后开始拉广告
            }

            @Override
            public void onStartFailed(Exception e) {
                Log.e("gdt onStartFailed:", e.toString());
            }
        });
    }

}
