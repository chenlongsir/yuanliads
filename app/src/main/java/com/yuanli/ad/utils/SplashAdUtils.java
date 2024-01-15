package com.yuanli.ad.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.CSJAdError;
import com.bytedance.sdk.openadsdk.CSJSplashAd;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.yuanli.ad.constants.AdConstants;
import com.yuanli.ad.holder.TTAdManagerHolder;

public class SplashAdUtils {
    private static final String TAG = "SplashAdUtils";
    private Context mContext;

    /** 开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms */
    private static final int AD_TIME_OUT = 3000;

    public void release(){
        mContext = null;
        mAdStateListener = null;
    }

    /**
     * 获取广告位
     */
    private AdSlot getAdSlot(){
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = mContext.getResources().getDisplayMetrics().heightPixels;
        return new AdSlot.Builder()
                .setCodeId(AdConstants.AD_SPLASH_COD_ID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(width, height)
                .build();
    }

    public void loadSplashAd(Activity activity, AdStateListener adStateListener){
        mContext = activity;
        mAdStateListener = adStateListener;
        AdSlot adSlot = getAdSlot();
        TTAdNative adNative = TTAdManagerHolder.get().createAdNative(activity);
        adNative.loadSplashAd(adSlot, new TTAdNative.CSJSplashAdListener() {
            @Override
            public void onSplashLoadSuccess(CSJSplashAd csjSplashAd) {

            }

            @Override
            public void onSplashLoadFail(CSJAdError csjAdError) {
                Log.e(TAG, "开屏广告加载异常"+ csjAdError.getMsg());
                closeAd();
            }

            @Override
            public void onSplashRenderSuccess(CSJSplashAd csjSplashAd) {
                Log.d(TAG,"开屏广告请求成功");
                if (csjSplashAd == null) {
                    closeAd();
                    return;
                }
                View view = csjSplashAd.getSplashView();
                setSuccess(view);
                csjSplashAd.setSplashAdListener(new CSJSplashAd.SplashAdListener() {
                    @Override
                    public void onSplashAdShow(CSJSplashAd csjSplashAd) {

                    }

                    @Override
                    public void onSplashAdClick(CSJSplashAd csjSplashAd) {

                    }

                    @Override
                    public void onSplashAdClose(CSJSplashAd csjSplashAd, int i) {
                        closeAd();
                    }
                });
            }

            @Override
            public void onSplashRenderFail(CSJSplashAd csjSplashAd, CSJAdError csjAdError) {
                closeAd();
            }
        },3000) ;
    }



    /**
     * SplashView的交互监听器
     * @param ttSplashAd
     */
    private void setAdInteractionListener(TTSplashAd ttSplashAd){
        ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.d(TAG,"开屏广告点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.d(TAG,"开屏广告展示");
            }

            @Override
            public void onAdSkip() {
                Log.d(TAG,"开屏广告跳过");
                closeAd();
            }

            @Override
            public void onAdTimeOver() {
                Log.d(TAG,"开屏广告倒计时结束");
                closeAd();
            }
        });
    }

    /**
     * 设置下载监听
     * @param ttSplashAd
     */
    private void setDownloadListener(TTSplashAd ttSplashAd){
        ttSplashAd.setDownloadListener(new TTAppDownloadListener() {
            boolean hasShow = false;
            @Override
            public void onIdle() { }
            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!hasShow) {
                    Toast.makeText(mContext, "下载中...", Toast.LENGTH_SHORT).show();
                    hasShow = true;
                }
            }
            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                Toast.makeText(mContext, "下载暂停...", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                Toast.makeText(mContext, "下载失败...", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {}
            @Override
            public void onInstalled(String fileName, String appName) {}
        });
    }

    public interface AdStateListener{
        /**
         * 广告加载成功
         * @param view
         */
        void onAdLoadSuccess(View view);

        /**
         * 广告关闭或异常
         */
        void onClose();
    }

    private AdStateListener mAdStateListener;

    private void setSuccess(View view){
        if (mAdStateListener != null){
            mAdStateListener.onAdLoadSuccess(view);
        }
    }

    private void closeAd(){
        if (mAdStateListener != null){
            mAdStateListener.onClose();
        }
    }
}
