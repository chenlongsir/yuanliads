package com.yuanli.ad.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.CSJAdError;
import com.bytedance.sdk.openadsdk.CSJSplashAd;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.yuanli.ad.holder.TTAdManagerHolder;

public class SplashAdUtils {
    private static final String TAG = "SplashAdUtils";
    private Context mContext;

    /**
     * 开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
     */
    private static final int AD_TIME_OUT = 3500;

    public void release() {
        mContext = null;
        mAdStateListener = null;
    }

    /**
     * 获取广告位
     */
    private AdSlot getAdSlot() {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        return new AdSlot.Builder()
                .setCodeId(InitUtils.getConstants().getSplashId())
                .setSupportDeepLink(true)
                .setImageAcceptedSize(width, height)
                .setExpressViewAcceptedSize(width, height)
                .build();
    }

    public void loadSplashAd(Context context, AdStateListener adStateListener) {
        TTAdManagerHolder.setInitListener(new TTAdManagerHolder.InitListener() {
            @Override
            public void onSuccess() {
                mContext = context;
                mAdStateListener = adStateListener;
                AdSlot adSlot = getAdSlot();
                TTAdNative adNative = TTAdManagerHolder.get().createAdNative(context);
                adNative.loadSplashAd(adSlot, new TTAdNative.CSJSplashAdListener() {
                    @Override
                    public void onSplashLoadSuccess(CSJSplashAd csjSplashAd) {

                    }

                    @Override
                    public void onSplashLoadFail(CSJAdError csjAdError) {
                        Log.e(TAG, "开屏广告下载失败" + csjAdError.getMsg());
                        closeAd();
                    }

                    @Override
                    public void onSplashRenderSuccess(CSJSplashAd csjSplashAd) {
                        Log.d(TAG, "开屏广告请求成功");
                        /** 渲染成功后，展示广告 */
                        showSplashAd(csjSplashAd,adStateListener);
                    }

                    @Override
                    public void onSplashRenderFail(CSJSplashAd csjSplashAd, CSJAdError 			                                        csjAdError) {
                        Log.e(TAG, "开屏广告加载超时");
                        closeAd();
                    }
                }, AD_TIME_OUT);
            }

            @Override
            public void onError(String msg) {
                closeAd();
                adStateListener.toast(msg);
            }
        });

    }

    public void showSplashAd(CSJSplashAd csjSplashAd, AdStateListener adStateListener) {
        csjSplashAd.setSplashAdListener(new CSJSplashAd.SplashAdListener() {
            @Override
            public void onSplashAdShow(CSJSplashAd csjSplashAd) {
            }

            @Override
            public void onSplashAdClick(CSJSplashAd csjSplashAd) {
            }

            @Override
            public void onSplashAdClose(CSJSplashAd csjSplashAd, int i) {
                // 广告关闭后，销毁广告页面
                adStateListener.onClose();
            }
        });
        View splashView = csjSplashAd.getSplashView();
        adStateListener.onAdLoadSuccess(splashView);

    }

    public interface AdStateListener {
        /**
         * 广告加载成功
         *
         * @param view
         */
        void onAdLoadSuccess(View view);

        /**
         * 广告关闭或异常
         */
        void onClose();

        void toast(String message);
    }

    private AdStateListener mAdStateListener;

    private void setSuccess(View view) {
        if (mAdStateListener != null) {
            mAdStateListener.onAdLoadSuccess(view);
        }
    }

    private void closeAd() {
        if (mAdStateListener != null) {
            mAdStateListener.onClose();
        }
    }
}
