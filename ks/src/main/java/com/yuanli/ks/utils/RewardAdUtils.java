package com.yuanli.ks.utils;


import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsLoadManager;
import com.kwad.sdk.api.KsRewardVideoAd;
import com.kwad.sdk.api.KsScene;
import com.yuanli.base.AdListener;
import com.yuanli.base.AdStateListener;
import com.yuanli.ks.KsManagerHolder;
import com.yuanli.ks.constants.KsConstants;

import java.util.List;

public class RewardAdUtils implements AdListener {
    private Activity activity;
    private AdStateListener adStateListener;
    private boolean isLoadSuccess = false;
    private KsRewardVideoAd mRewardVideoAd;
    public RewardAdUtils(Activity activity){
        this.activity = activity;
    }

    public void loadAd(final AdStateListener adStateListener){
        isLoadSuccess = false;
        RewardAdUtils.this.adStateListener = adStateListener;
        KsManagerHolder.setInitListener(new KsManagerHolder.InitListener() {
            @Override
            public void onSuccess() {
                adStateListener.loading();
                KsScene scene = new KsScene.Builder(InitUtils.getBean().getRewardId()).build(); // 此为测试posId，请联系快手平台申请正式posId
                KsAdSDK.getLoadManager().loadRewardVideoAd(scene, new KsLoadManager.RewardVideoAdListener() {
                    @Override
                    public void onError(int code, String msg) {
                        Log.e(KsConstants.TAG, "onError: " + msg);
                        adStateListener.onError();
                        adStateListener.stopLoading();
                    }

                    @Override
                    public void onRewardVideoResult(@Nullable List<KsRewardVideoAd> adList) {
                        Log.e(KsConstants.TAG, "onRewardVideoResult: ");
                    }

                    @Override
                    public void onRewardVideoAdLoad(@Nullable List<KsRewardVideoAd> adList) {
                        Log.e(KsConstants.TAG, "onRewardVideoAdLoad: ");
                        if (adList != null && adList.size() > 0) {
                            mRewardVideoAd = adList.get(0);
                            adStateListener.stopLoading();
                            adStateListener.real(RewardAdUtils.this);
                        }
                    }
                });
            }

            @Override
            public void onError(String msg) {
                adStateListener.stopLoading();
                adStateListener.onError();
                adStateListener.toast(msg);
            }
        });
    }



    @Override
    public void showAd() {
        if (mRewardVideoAd != null && mRewardVideoAd.isAdEnable()) {
            mRewardVideoAd
                    .setRewardAdInteractionListener(new KsRewardVideoAd.RewardAdInteractionListener() {
                        @Override
                        public void onAdClicked() {
                            Log.d(KsConstants.TAG, "onAdClicked: ");
                        }

                        @Override
                        public void onPageDismiss() {
                            Log.d(KsConstants.TAG, "onPageDismiss: ");
                            if (isLoadSuccess){
                                adStateListener.successClose();
                            }
                        }

                        @Override
                        public void onVideoPlayError(int code, int extra) {
                            Log.d(KsConstants.TAG, "onVideoPlayError: ");
                            adStateListener.onError();
                        }

                        @Override
                        public void onVideoPlayEnd() {
                            Log.d(KsConstants.TAG, "onVideoPlayEnd: ");
                        }

                        @Override
                        public void onVideoSkipToEnd(long l) {
                            Log.d(KsConstants.TAG, "onVideoSkipToEnd: ");
                        }

                        @Override
                        public void onVideoPlayStart() {
                            Log.d(KsConstants.TAG, "onVideoPlayStart: ");
                        }

                        @Override
                        public void onRewardVerify() {
                            Log.d(KsConstants.TAG, "onRewardVerify: ");
                            isLoadSuccess = true;
                            adStateListener.success();
                        }

                        @Override
                        public void onRewardStepVerify(int taskType, int currentTaskStatus) {
                            Log.d(KsConstants.TAG, "onRewardVerify: ");
                        }

                        @Override
                        public void onExtraRewardVerify(int i) {
                            Log.d(KsConstants.TAG, "onExtraRewardVerify: ");

                        }
                    });

            mRewardVideoAd.showRewardVideoAd(activity, null);
        } else {
            Log.d("TAG", "showAd: " + "暂无可用激励视频广告，请等待缓存加载或者重新刷新");
            adStateListener.onError();
        }
    }

    @Override
    public void onDestroy() {
        KsManagerHolder.clear();
        mRewardVideoAd = null;
        adStateListener = null;
        activity = null;
    }
}
