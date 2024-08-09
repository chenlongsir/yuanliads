package com.yuanli.ksads;

import android.annotation.SuppressLint;
import android.content.Context;

import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsInitCallback;
import com.kwad.sdk.api.KsLoadManager;
import com.kwad.sdk.api.SdkConfig;
import com.yuanli.base.log.Logger;
import com.yuanli.ksads.constants.KsBean;
import com.yuanli.ksads.constants.KsConstants;
import com.yuanli.ksads.utils.InitUtils;

public class KsManagerHolder {
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

    public static void init(Context context, KsBean ksBean) {
        KsManagerHolder.context = context;
        InitUtils.init(ksBean);
        doInit(context);
    }


    public interface InitListener{
        void onSuccess();
        void onError(String msg);
    }

    public static void setInitListener(InitListener initListener){
        KsManagerHolder.initListener = initListener;
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
        KsAdSDK.init(appContext, new SdkConfig.Builder()
                .appId(InitUtils.getBean().getAppId()) // 测试aapId，请联系快手平台申请正式AppId，必填
                .appName(InitUtils.getBean().getAppName())  // 测试appName，请填写您应用的名称，非必填
                .showNotification(true) // 是否展示下载通知栏
                .debug(BuildConfig.DEBUG) // 是否开启sdk 调试日志  可选
                .setStartCallback(new KsInitCallback() { // 设置监听start 的调用结果
                    @Override
                    public void onSuccess() {
                        // 启动成功后再获取SDK
                        Logger.i(KsConstants.TAG, "start success");
                        isFail = false;
                        sInit = true;
                        if (initListener != null){
                            initListener.onSuccess();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        Logger.i(KsConstants.TAG, "start fail msg: " + msg);
                        isFail = true;
                        if (initListener != null) {
                            initListener.onError("SDK初始化失败");
                        }
                    }
                })
                .build());
        KsAdSDK.start();
    }

}
