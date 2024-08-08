package com.yuanli.baidu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.baidu.mobads.sdk.api.BDAdConfig;
import com.baidu.mobads.sdk.api.BDDialogParams;
import com.yuanli.baidu.constants.BaiduBean;
import com.yuanli.baidu.constants.BaiduConstants;
import com.yuanli.baidu.utils.InitUtils;

public class BaiduManagerHolder {
    private static boolean sInit;
    private static boolean isFail;
    private static InitListener initListener;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static void init(Context context, BaiduBean baiduBean) {
        BaiduManagerHolder.context = context;
        InitUtils.init(baiduBean);
        doInit(context);
    }

    public interface InitListener{
        void onSuccess();
        void onError(String msg);
    }

    public static void setInitListener(InitListener initListener){
        BaiduManagerHolder.initListener = initListener;
        if (sInit){
            initListener.onSuccess();
        }else if (isFail){
            doInit(context);
        }
    }

    public static void clear(){
        initListener = null;
    }


    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context) {
        if (!sInit) {
            BDAdConfig bdAdConfig = buildConfig(context);
            bdAdConfig.init();
        }
    }

    private static BDAdConfig buildConfig(Context context) {
        return new BDAdConfig.Builder()
                // 1、设置app名称，可选
                .setAppName(InitUtils.getBean().getAppName())
                // 2、应用在mssp平台申请到的appsid，和包名一一对应，此处设置等同于在AndroidManifest.xml里面设置
                .setAppsid(InitUtils.getBean().getAppId())
                // 3、设置下载弹窗的类型和按钮动效样式，可选
                .setDialogParams(new BDDialogParams.Builder()
                        .setDlDialogType(BDDialogParams.TYPE_BOTTOM_POPUP)
                        .setDlDialogAnimStyle(BDDialogParams.ANIM_STYLE_NONE)
                        .build())
                // 4、控制台debug日志调试开关 接入调试阶段可以打开，上线前需关闭
                .setDebug(false)
                // 5、设置微信openSDK 应用id
                .setWXAppid("")
                // 6、如需获知SDK初始化结果，可选择性注册监听
                .setBDAdInitListener(new BDAdConfig.BDAdInitListener() {
                    @Override
                    public void success() {
                        Log.d(BaiduConstants.TAG,"SDK初始化成功");
                        isFail = false;
                        sInit = true;
                        if (initListener != null){
                            initListener.onSuccess();
                        }
                    }

                    @Override
                    public void fail() {
                        Log.d(BaiduConstants.TAG,"SDK初始化失败");
                        isFail = true;
                        if (initListener != null){
                            initListener.onError("SDK初始化失败");
                        }
                    }
                })
                .build(context);
    }
}
