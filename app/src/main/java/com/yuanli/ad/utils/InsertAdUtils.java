package com.yuanli.ad.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot;
import com.yuanli.ad.constants.AdConstants;
import com.yuanli.ad.holder.TTAdManagerHolder;
import com.yuanli.ad.listener.AdListener;
import com.yuanli.ad.listener.AdStateListener;

public class InsertAdUtils implements AdListener {
    private Activity activity;
    private TTAdNative mTTAdNative;
    private TTFullScreenVideoAd mTTFullScreenVideoAd; // 插全屏广告对象
    private TTAdNative.FullScreenVideoAdListener mFullScreenVideoListener; // 广告加载监听器
    private TTFullScreenVideoAd.FullScreenVideoAdInteractionListener mFullScreenVideoAdInteractionListener; // 广告展示监听器
    private AdStateListener stateListener;

    public InsertAdUtils(Activity activity) {
        this.activity = activity;
    }

    private void initAdConfig(Context context) {
        TTAdManagerHolder.get().requestPermissionIfNecessary(context);
    }

    public void loadAd(final AdStateListener stateListener) {
        InsertAdUtils.this.stateListener = stateListener;

        TTAdManagerHolder.setInitListener(new TTAdManagerHolder.InitListener() {
            @Override
            public void onSuccess() {
                AdSlot adSlot = new AdSlot.Builder()
                        .setCodeId(InitUtils.getConstants().getInsertId()) //广告位id
                        .setOrientation(TTAdConstant.ORIENTATION_VERTICAL)//设置横竖屏方向
                        .setMediationAdSlot(new MediationAdSlot.Builder()
                                .setMuted(true)//是否静音
                                .setVolume(0.7f)//设置音量
                                .setBidNotify(true)//竞价结果通知
                                .build())
                        .build();

                initListeners();

                mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity);

                /* 加载广告 */
                stateListener.loading();
                mTTAdNative.loadFullScreenVideoAd(adSlot, InsertAdUtils.this.mFullScreenVideoListener);
            }

            @Override
            public void onError(String msg) {
                stateListener.onError();
                stateListener.toast(msg);
            }
        });
    }

    @Override
    public void showAd() {
        if (this.mTTFullScreenVideoAd == null) {
            Log.d(AdConstants.TAG, "请先加载广告或等待广告加载完毕后再调用show方法");
            return;
        }
        /*设置展示监听器，展示广告 */
        this.mTTFullScreenVideoAd.setFullScreenVideoAdInteractionListener(this.mFullScreenVideoAdInteractionListener);
        this.mTTFullScreenVideoAd.showFullScreenVideoAd(activity);
    }

    private void initListeners() {
        this.mFullScreenVideoListener = new TTAdNative.FullScreenVideoAdListener() {
            public void onError(int code, String message) {
                Log.d(AdConstants.TAG, "InterstitialFull onError code = " + code + " msg = " + message);
                stateListener.stopLoading();
                stateListener.onError();
            }

            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                Log.d(AdConstants.TAG, "InterstitialFull onFullScreenVideoLoaded");
                mTTFullScreenVideoAd = ad;
                stateListener.stopLoading();
                stateListener.real();
            }

            public void onFullScreenVideoCached() {
                Log.d(AdConstants.TAG, "InterstitialFull onFullScreenVideoCached");
            }

            public void onFullScreenVideoCached(TTFullScreenVideoAd ad) {
                Log.d(AdConstants.TAG, "InterstitialFull onFullScreenVideoCached");
                mTTFullScreenVideoAd = ad;
                stateListener.stopLoading();
                stateListener.real();
            }
        };
        this.mFullScreenVideoAdInteractionListener = new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
            public void onAdShow() {
                Log.d(AdConstants.TAG, "InterstitialFull onAdShow");
            }

            public void onAdVideoBarClick() {
                Log.d(AdConstants.TAG, "InterstitialFull onAdVideoBarClick");
            }

            public void onAdClose() {
                stateListener.success();
                Log.d(AdConstants.TAG, "InterstitialFull onAdClose");
            }

            public void onVideoComplete() {
                Log.d(AdConstants.TAG, "InterstitialFull onVideoComplete");
            }

            public void onSkippedVideo() {
                stateListener.success();
                Log.d(AdConstants.TAG, "InterstitialFull onSkippedVideo");
            }
        };
    }

    public void onDestroy() {
        if (mTTFullScreenVideoAd != null && mTTFullScreenVideoAd.getMediationManager() != null) {
            mTTFullScreenVideoAd.getMediationManager().destroy();
        }
        mTTAdNative = null;
        mFullScreenVideoListener = null;
        mFullScreenVideoAdInteractionListener = null;
        stateListener = null;
        activity = null;
    }

}
