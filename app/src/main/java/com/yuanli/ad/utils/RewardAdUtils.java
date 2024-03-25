package com.yuanli.ad.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot;
import com.yuanli.ad.constants.AdConstants;
import com.yuanli.ad.holder.TTAdManagerHolder;
import com.yuanli.ad.listener.AdListener;
import com.yuanli.ad.listener.AdStateListener;

public class RewardAdUtils implements AdListener {
    private Activity activity;
    private TTRewardVideoAd mTTRewardVideoAd; // 插全屏广告对象
    private TTAdNative.RewardVideoAdListener mRewardVideoListener; // 广告加载监听器
    private TTRewardVideoAd.RewardAdInteractionListener mRewardVideoAdInteractionListener; // 广告展示监听器
    private AdStateListener adStateListener;

    public RewardAdUtils(Activity activity){
        this.activity = activity;
    }

    private void initAdConfig(Context context) {
        TTAdManagerHolder.get().requestPermissionIfNecessary(context);
    }

    public void loadAd(final AdStateListener adStateListener){
        RewardAdUtils.this.adStateListener = adStateListener;
        TTAdManagerHolder.setInitListener(new TTAdManagerHolder.InitListener() {
            @Override
            public void onSuccess() {
                /** 1、创建AdSlot对象 */
                AdSlot adslot = new AdSlot.Builder()
                        .setCodeId(InitUtils.getConstants().getRewardId())
                        .setOrientation(TTAdConstant.ORIENTATION_VERTICAL)
                        .setMediationAdSlot(new MediationAdSlot
                                .Builder()
                                .setRewardName("rewardName")
                                .setRewardAmount(100)
                                .build())
                        .build();

                /** 2、创建TTAdNative对象 */
                TTAdNative adNativeLoader = TTAdSdk.getAdManager().createAdNative(activity);

                /** 3、创建加载、展示监听器 */
                initListeners();

                adStateListener.loading();
                /** 4、加载广告 */
                adNativeLoader.loadRewardVideoAd(adslot, mRewardVideoListener);
            }

            @Override
            public void onError(String msg) {
                adStateListener.stopLoading();
                adStateListener.onError();
                adStateListener.toast(msg);
            }
        });
    }

    private void initListeners() {
        this.mRewardVideoListener = new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.i(AdConstants.TAG, "reward load fail: errCode: " + i + ", errMsg: " + s);
                adStateListener.stopLoading();
                adStateListener.onError();
            }

            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                Log.i(AdConstants.TAG, "reward load success");
                mTTRewardVideoAd = ttRewardVideoAd;
                adStateListener.stopLoading();
                adStateListener.real();
            }

            @Override
            public void onRewardVideoCached() {
                Log.i(AdConstants.TAG, "reward cached success");
            }

            @Override
            public void onRewardVideoCached(TTRewardVideoAd ttRewardVideoAd) {
                Log.i(AdConstants.TAG, "reward cached success 2");
                mTTRewardVideoAd = ttRewardVideoAd;
                adStateListener.stopLoading();
                adStateListener.real();
            }
        };
        this.mRewardVideoAdInteractionListener = new TTRewardVideoAd.RewardAdInteractionListener() {
            @Override
            public void onAdShow() {
                Log.i(AdConstants.TAG, "reward show");
            }

            @Override
            public void onAdVideoBarClick() {
                Log.i(AdConstants.TAG, "reward click");
            }

            @Override
            public void onAdClose() {
                Log.i(AdConstants.TAG, "reward close");
            }

            @Override
            public void onVideoComplete() {
                Log.i(AdConstants.TAG, "reward onVideoComplete");
            }

            @Override
            public void onVideoError() {
                Log.i(AdConstants.TAG, "reward onVideoError");
            }

            @Override
            public void onRewardVerify(boolean b, int i, String s, int i1, String s1) {
                Log.i(AdConstants.TAG, "reward onRewardVerify");
            }

            @Override
            public void onRewardArrived(boolean b, int i, Bundle bundle) {
                Log.i(AdConstants.TAG, "reward onRewardArrived");
                adStateListener.success();
            }

            @Override
            public void onSkippedVideo() {
                Log.i(AdConstants.TAG, "reward onSkippedVideo");
            }
        };
    }


    @Override
    public void showAd() {
        if (mTTRewardVideoAd == null) {
            Log.i(AdConstants.TAG, "请先加载广告或等待广告加载完毕后再调用show方法");
            return;
        }
        /** 5、设置展示监听器，展示广告 */
        mTTRewardVideoAd.setRewardAdInteractionListener(mRewardVideoAdInteractionListener);
        mTTRewardVideoAd.showRewardVideoAd(activity);
    }

    @Override
    public void onDestroy() {
        if (mTTRewardVideoAd != null && mTTRewardVideoAd.getMediationManager() != null) {
            mTTRewardVideoAd.getMediationManager().destroy();
        }
        TTAdManagerHolder.clear();
        mRewardVideoListener = null;
        mRewardVideoAdInteractionListener = null;
        adStateListener = null;
        activity = null;
    }
}
