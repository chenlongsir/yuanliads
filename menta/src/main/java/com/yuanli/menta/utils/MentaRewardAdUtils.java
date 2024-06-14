package com.yuanli.menta.utils;

import android.app.Activity;
import android.util.Log;

import com.yuanli.base.AdListener;
import com.yuanli.base.AdStateListener;
import com.yuanli.menta.MentaAdManagerHold;

import cn.vlion.ad.inland.base.util.config.VlionScaleType;
import cn.vlion.ad.inland.core.config.VlionAdError;
import cn.vlion.ad.inland.core.config.VlionSlotConfig;
import cn.vlion.ad.inland.core.reward.VlionRewardVideoListener;
import cn.vlion.ad.inland.core.reward.VlionRewardedVideoAd;

public class MentaRewardAdUtils implements AdListener {
    private static final String TAG = "MentaRewardAdUtils";
    private VlionSlotConfig vlionSlotConfig;
    private Activity mActivity;
    private VlionRewardedVideoAd vlionRewardedVideoAd;
    private AdStateListener adStateListener;

    public MentaRewardAdUtils(Activity activity) {
        mActivity = activity;
    }

    private void loadRewardAd() {
        adStateListener.loading();
        vlionRewardedVideoAd = new VlionRewardedVideoAd(mActivity, vlionSlotConfig);
        vlionRewardedVideoAd.setVlionRewardVideoListener(new VlionRewardVideoListener() {
            /**
             *  激励视频加载成功
             */
            @Override
            public void onAdLoadSuccess(double price) {
                Log.d(TAG, "onAdLoadSuccess: ");
                vlionRewardedVideoAd.notifyWinPrice();
            }
            /**
             *  激励视频加载失败
             *
             * @param vlionAdError  错误
             */
            @Override
            public void onAdLoadFailure(VlionAdError vlionAdError) {
                Log.d(TAG, "onAdLoadFailure: " + vlionAdError.toString());
                Log.d(TAG, "onAdLoadFailure: " + vlionAdError.getCode() + vlionAdError.getPlatformMSG());
                adStateListener.stopLoading();
                adStateListener.onError();
            }
            /**
             *  激励视频视图渲染成功
             *
             */
            @Override
            public void onAdRenderSuccess() {
                Log.d(TAG, "onAdRenderSuccess: ");
                adStateListener.stopLoading();
                adStateListener.real(MentaRewardAdUtils.this);
            }

            /**
             *  激励视频视图渲染失败
             *
             * @param vlionAdError  错误
             */
            @Override
            public void onAdRenderFailure(VlionAdError vlionAdError) {
                Log.d(TAG, "onAdRenderFailure: " + vlionAdError.getCode() + vlionAdError.getPlatformMSG());
                Log.d(TAG, "onAdRenderFailure: ");
                adStateListener.stopLoading();
                adStateListener.onError();
            }
            /**
             *  激励视频开始播放
             *
             */
            @Override
            public void onVideoStart() {
                Log.d(TAG, "onVideoStart: ");
            }
            /**
             *  激励视频播放失败
             *
             * @param vlionAdError  错误
             */
            @Override
            public void onAdPlayFailure(VlionAdError vlionAdError) {
                Log.d(TAG, "onAdPlayFailure: ");
                adStateListener.stopLoading();
                adStateListener.onError();
            }
            /**
             * 激励视频曝光
             */
            @Override
            public void onAdExposure() {
                Log.d(TAG, "onAdExposure: ");
            }

            @Override
            public void onAdShowFailure(VlionAdError vlionAdError) {
                Log.d(TAG, "onAdShowFailure: " + vlionAdError.getCode() + vlionAdError.getPlatformMSG());
                Log.d(TAG, "onAdShowFailure: ");
                adStateListener.stopLoading();
                adStateListener.onError();
            }

            /**
             * 激励视频播放完成
             */
            @Override
            public void onVideoCompleted() {
                Log.d(TAG, "onVideoCompleted: ");
            }
            /**
             * 激励视频奖励发放
             */
            @Override
            public void onAdReward() {
                Log.d(TAG, "onAdReward: ");
                adStateListener.success();
            }
            /**
             * 激励视频跳过
             */
            @Override
            public void onAdVideoSkip() {
                Log.d(TAG, "onAdVideoSkip: ");

            }
            /**
             * 激励视频点击
             */
            @Override
            public void onAdClick() {
                Log.d(TAG, "onAdClick: ");
            }
            /**
             * 激励视频关闭
             */
            @Override
            public void onAdClose() {
                Log.d(TAG, "onAdClose: ");

            }
        });
        //加载广告
        vlionRewardedVideoAd.loadAd();
    }

    @Override
    public void loadAd(AdStateListener adStateListener) {
        this.adStateListener = adStateListener;
        int width = mActivity.getResources().getDisplayMetrics().widthPixels;
        int height = mActivity.getResources().getDisplayMetrics().heightPixels;
        vlionSlotConfig = new VlionSlotConfig.Builder()
                .setSlotID(InitUtils.getBean().getRewardId())
                .setSize(width, height) //模版广告尺寸宽高，单位px
                .setTolerateTime(5f)//广告加载容忍时间1f~5f。如果设定的时间内没有竞价到广告，则判断竞价失败

                //设置图片的填充方式
                //AD_IMAGE_SCALING_FITCENTER   对标ImageView设置scaleType 的fitCenter
                //AD_IMAGE_SCALING_FITXY			 对标ImageView设置scaleType fitXY
                //AD_IMAGE_SCALING_CENTER_CROP 对标ImageView设置scaleType centerCrop
                .setImageScale(VlionScaleType.AD_IMAGE_SCALING_CENTER_CROP)
                .build();
        loadRewardAd();
    }

    @Override
    public void showAd() {
        if (!MentaAdManagerHold.isIsInitSuccess()){
            adStateListener.onError();
            return;
        }
        vlionRewardedVideoAd.showAd(mActivity);
    }

    @Override
    public void onDestroy() {
        if (null != vlionRewardedVideoAd) {
            vlionRewardedVideoAd.destroy();
            vlionRewardedVideoAd = null;
        }
        mActivity = null;
        adStateListener = null;
    }
}
