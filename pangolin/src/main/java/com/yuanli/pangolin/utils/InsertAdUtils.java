package com.yuanli.pangolin.utils;

import android.app.Activity;


import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot;
import com.yuanli.base.Ad;
import com.yuanli.base.AdStateListener;
import com.yuanli.base.log.Logger;
import com.yuanli.pangolin.constants.AdConstants;
import com.yuanli.pangolin.holder.TTAdManagerHolder;

public class InsertAdUtils implements Ad {
    private Activity activity;
    private TTAdNative mTTAdNative;
    private TTFullScreenVideoAd mTTFullScreenVideoAd; // 插全屏广告对象
    private TTAdNative.FullScreenVideoAdListener mFullScreenVideoListener; // 广告加载监听器
    private TTFullScreenVideoAd.FullScreenVideoAdInteractionListener mFullScreenVideoAdInteractionListener; // 广告展示监听器
    private AdStateListener stateListener;

    public InsertAdUtils(Activity activity) {
        this.activity = activity;
    }

    public void loadAd(final AdStateListener stateListener) {
        InsertAdUtils.this.stateListener = stateListener;

        TTAdManagerHolder.setInitListener(new TTAdManagerHolder.InitListener() {
            @Override
            public void onSuccess() {
                AdSlot adSlot = new AdSlot.Builder()
                        .setCodeId(InitUtils.getBean().getInsertId()) //广告位id
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
                stateListener.onError(msg);
                stateListener.toast(msg);
            }
        });
    }

    @Override
    public void showAd() {
        if (this.mTTFullScreenVideoAd == null) {
            Logger.i(AdConstants.TAG, "请先加载广告或等待广告加载完毕后再调用show方法");
            return;
        }
        /*设置展示监听器，展示广告 */
        this.mTTFullScreenVideoAd.setFullScreenVideoAdInteractionListener(this.mFullScreenVideoAdInteractionListener);
        this.mTTFullScreenVideoAd.showFullScreenVideoAd(activity);
    }

    private void initListeners() {
        this.mFullScreenVideoListener = new TTAdNative.FullScreenVideoAdListener() {
            public void onError(int code, String message) {
                Logger.i(AdConstants.TAG, "InterstitialFull onError code = " + code + " msg = " + message);
                stateListener.stopLoading();
                stateListener.onError(message);
            }

            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                Logger.i(AdConstants.TAG, "InterstitialFull onFullScreenVideoLoaded");
                mTTFullScreenVideoAd = ad;
                stateListener.stopLoading();
                stateListener.real(InsertAdUtils.this);
            }

            public void onFullScreenVideoCached() {
                Logger.i(AdConstants.TAG, "InterstitialFull onFullScreenVideoCached");
            }

            public void onFullScreenVideoCached(TTFullScreenVideoAd ad) {
                Logger.i(AdConstants.TAG, "InterstitialFull onFullScreenVideoCached");
                mTTFullScreenVideoAd = ad;
                stateListener.stopLoading();
                stateListener.real(InsertAdUtils.this);
            }
        };
        this.mFullScreenVideoAdInteractionListener = new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
            public void onAdShow() {
                Logger.i(AdConstants.TAG, "InterstitialFull onAdShow");
            }

            public void onAdVideoBarClick() {
                Logger.i(AdConstants.TAG, "InterstitialFull onAdVideoBarClick");
            }

            public void onAdClose() {
                stateListener.success();
                Logger.i(AdConstants.TAG, "InterstitialFull onAdClose");
            }

            public void onVideoComplete() {
                Logger.i(AdConstants.TAG, "InterstitialFull onVideoComplete");
            }

            public void onSkippedVideo() {
                stateListener.success();
                Logger.i(AdConstants.TAG, "InterstitialFull onSkippedVideo");
            }
        };
    }

    public void onDestroy() {
        if (mTTFullScreenVideoAd != null && mTTFullScreenVideoAd.getMediationManager() != null) {
            mTTFullScreenVideoAd.getMediationManager().destroy();
        }
        TTAdManagerHolder.clear();
        mTTAdNative = null;
        mFullScreenVideoListener = null;
        mFullScreenVideoAdInteractionListener = null;
        stateListener = null;
        activity = null;
    }

}
