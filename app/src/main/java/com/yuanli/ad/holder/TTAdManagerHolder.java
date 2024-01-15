package com.yuanli.ad.holder;

import android.content.Context;

import com.bytedance.sdk.openadsdk.LocationProvider;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTCustomController;
import com.yuanli.ad.constants.AdConstants;

/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
public class TTAdManagerHolder {

    private static boolean sInit;
    private static boolean isFail;
    private static Context mContext;

    public static TTAdManager get() {
        if (!sInit) {
            throw new RuntimeException("TTAdSdk is not init, please check.");
        }
        return TTAdSdk.getAdManager();
    }

    public static void init(Context context) {
        mContext = context;
        doInit(context);
    }

    public interface InitListener{
        void onSuccess();
        void onError(String msg);
    }

    private static InitListener initListener;


    public static void setInitListener(InitListener initListener){
        TTAdManagerHolder.initListener = initListener;
        if (sInit){
            initListener.onSuccess();
        }else if (isFail){
            doInit(mContext);
        }
    }

    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context) {
        if (!sInit) {
            TTAdSdk.init(context, buildConfig());
            TTAdSdk.start(new TTAdSdk.Callback() {
                @Override
                public void success() {
                    sInit = true;
                    isFail = false;
                    if (initListener != null){
                        initListener.onSuccess();
                    }
                }

                @Override
                public void fail(int i, String s) {
                    isFail = true;
                    if (initListener != null){
                        initListener.onError(s);
                    }
                }
            });
        }
    }

    private static TTAdConfig buildConfig() {
        return new TTAdConfig.Builder()
                .appId(AdConstants.AD_APP_ID)
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .appName(AdConstants.AD_APP_NAME)
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .debug(false) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                .supportMultiProcess(false)//是否支持多进程
                .customController(new TTCustomController() {
                    @Override
                    public boolean isCanUseLocation() {
                        return false;
                    }

                    @Override
                    public LocationProvider getTTLocation() {
                        return super.getTTLocation();
                    }

                    @Override
                    public boolean alist() {
                        return false;
                    }

                    @Override
                    public boolean isCanUsePhoneState() {
                        return false;
                    }

                    @Override
                    public String getDevImei() {
                        return super.getDevImei();
                    }

                    @Override
                    public boolean isCanUseWifiState() {
                        return super.isCanUseWifiState();
                    }

                    @Override
                    public String getMacAddress() {
                        return super.getMacAddress();
                    }

                    @Override
                    public boolean isCanUseWriteExternal() {
                        return false;
                    }

                    @Override
                    public String getDevOaid() {
                        return super.getDevOaid();
                    }

                    @Override
                    public boolean isCanUseAndroidId() {
                        return false;
                    }
                })
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build();

//        return new TTAdConfig.Builder()
//                        .appId("5150229")
//                        .useTextureView(true) //默认使用SurfaceView播放视频广告,当有SurfaceView冲突的场景，可以使用TextureView
//                        .appName("标准倒数日_android")
//                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)//落地页主题
//                        .allowShowNotify(true) //是否允许sdk展示通知栏提示,若设置为false则会导致通知栏不显示下载进度
//                        .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
//                        .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合,没有设置的网络下点击下载apk会有二次确认弹窗，弹窗中会披露应用信息
//                        .supportMultiProcess(true) //是否支持多进程，true支持
//                        //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
//                        .build();
    }
}
