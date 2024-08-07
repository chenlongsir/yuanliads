package com.yuanli.baidu.utils;

import static com.yuanli.baidu.constants.BaiduConstants.TAG;

import android.app.Activity;

import com.baidu.mobads.sdk.api.RewardVideoAd;
import com.yuanli.baidu.BaiduManagerHolder;
import com.yuanli.base.AdListener;
import com.yuanli.base.AdStateListener;

public class RewardAdUtils implements AdListener {
    private Activity activity;
    private AdStateListener adStateListener;
    private boolean isLoadSuccess = false;
    private RewardVideoAd rewardVideoAd;

    public RewardAdUtils(Activity activity){
        this.activity = activity;
    }

    public void loadAd(final AdStateListener adStateListener){
        RewardAdUtils.this.adStateListener = adStateListener;
        BaiduManagerHolder.setInitListener(new BaiduManagerHolder.InitListener() {
            @Override
            public void onSuccess() {
                if (rewardVideoAd == null){
                    rewardVideoAd = new RewardVideoAd(activity, InitUtils.getBean().getRewardId(), rewardListener);
                }
            }

            @Override
            public void onError(String msg) {
                adStateListener.stopLoading();
                adStateListener.onError();
                adStateListener.toast(msg);
            }
        });
    }


    private final RewardVideoAd.RewardVideoAdListener rewardListener = new RewardVideoAd.RewardVideoAdListener() {
        @Override
        public void onAdShow() {
            Log.d(TAG, "onAdShow: ");
            adStateListener.stopLoading();
        }

        @Override
        public void onAdClick() {
            Log.d(TAG, "onAdClick: ");
        }

        @Override
        public void onAdClose(float v) {
            Log.d(TAG, "onAdClose: ");
            if (isLoadSuccess){
                adStateListener.successClose();
            }
        }

        @Override
        public void onAdFailed(String s) {
            Log.e(TAG, "onAdFailed: " + s);
            adStateListener.onError();
        }

        @Override
        public void onVideoDownloadSuccess() {
            Log.d(TAG, "onVideoDownloadSuccess: ");
        }

        @Override
        public void onVideoDownloadFailed() {
            Log.d(TAG, "onVideoDownloadFailed: ");

        }

        @Override
        public void playCompletion() {
            Log.d(TAG, "playCompletion: ");

        }

        @Override
        public void onAdLoaded() {
            Log.d(TAG, "onAdLoaded: ");

        }

        @Override
        public void onAdSkip(float v) {
            Log.d(TAG, "onAdSkip: ");

        }

        @Override
        public void onRewardVerify(boolean b) {
            Log.d(TAG, "onRewardVerify: ");
            isLoadSuccess = b;
            if (b){
                adStateListener.success();
            }
        }
    };


    @Override
    public void showAd() {
        adStateListener.loading();
        rewardVideoAd.load();
    }

    @Override
    public void onDestroy() {
        BaiduManagerHolder.clear();
        adStateListener = null;
        activity = null;
    }
}
