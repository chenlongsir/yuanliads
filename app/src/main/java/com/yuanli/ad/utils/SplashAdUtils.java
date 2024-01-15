package com.yuanli.ad.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
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
        mContext = context;
        mAdStateListener = adStateListener;
        AdSlot adSlot = getAdSlot();
        TTAdNative adNative = TTAdManagerHolder.get().createAdNative(context);
        adNative.loadSplashAd(adSlot, splashAdListener, AD_TIME_OUT);
    }

    /**
     * 开屏广告加载监听
     */
    private TTAdNative.SplashAdListener splashAdListener = new TTAdNative.SplashAdListener() {
        @Override
        public void onError(int code, String message) {
            Log.e("开屏广告加载异常", message);
            closeAd();
        }

        @Override
        public void onTimeout() {
            Log.e(TAG, "开屏广告加载超时");
            closeAd();
        }

        @Override
        public void onSplashAdLoad(TTSplashAd ttSplashAd) {
            Log.d(TAG, "开屏广告请求成功");
            if (ttSplashAd == null) {
                closeAd();
                return;
            }
            //获取SplashView
            View view = ttSplashAd.getSplashView();
            setSuccess(view);
            //设置SplashView的交互监听器
            setAdInteractionListener(ttSplashAd);
            //设置SplashView的下载监听
            if (ttSplashAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                setDownloadListener(ttSplashAd);
            }
        }
    };

    /**
     * SplashView的交互监听器
     *
     * @param ttSplashAd
     */
    private void setAdInteractionListener(TTSplashAd ttSplashAd) {
        ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.d(TAG, "开屏广告点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.d(TAG, "开屏广告展示");
            }

            @Override
            public void onAdSkip() {
                Log.d(TAG, "开屏广告跳过");
                closeAd();
            }

            @Override
            public void onAdTimeOver() {
                Log.d(TAG, "开屏广告倒计时结束");
                closeAd();
            }
        });
    }

    /**
     * 设置下载监听
     *
     * @param ttSplashAd
     */
    private void setDownloadListener(TTSplashAd ttSplashAd) {
        ttSplashAd.setDownloadListener(new TTAppDownloadListener() {
            boolean hasShow = false;

            @Override
            public void onIdle() {
            }

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
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
            }

            @Override
            public void onInstalled(String fileName, String appName) {
            }
        });
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
