package com.yuanli.menta.utils;

import android.app.Activity;


import com.yuanli.base.Ad;
import com.yuanli.base.AdStateListener;
import com.yuanli.base.log.Logger;
import com.yuanli.menta.MentaAdManagerHold;

import cn.vlion.ad.inland.base.util.config.VlionScaleType;
import cn.vlion.ad.inland.core.config.VlionAdError;
import cn.vlion.ad.inland.core.config.VlionSlotConfig;
import cn.vlion.ad.inland.core.reward.VlionRewardVideoListener;
import cn.vlion.ad.inland.core.reward.VlionRewardedVideoAd;

public class MentaRewardAdUtils implements Ad {
    private static final String TAG = "MentaRewardAdUtils";
    private VlionSlotConfig vlionSlotConfig;
    private Activity mActivity;
    private VlionRewardedVideoAd vlionRewardedVideoAd;
    private AdStateListener adStateListener;
    private boolean isVertical;
    public MentaRewardAdUtils(Activity activity) {
        mActivity = activity;
        this.isVertical = false;
    }

    public MentaRewardAdUtils(Activity activity,boolean isVertical) {
        mActivity = activity;
        this.isVertical = isVertical;
    }

    private void loadRewardAd() {
        adStateListener.loading();
        vlionRewardedVideoAd = new VlionRewardedVideoAd(mActivity, vlionSlotConfig);
        vlionRewardedVideoAd.setVlionRewardVideoListener(new VlionRewardVideoListener() {

            private boolean isLoadSuccess = false;
            /**
             *  激励视频加载成功
             */
            @Override
            public void onAdLoadSuccess(double price) {
                Logger.i(TAG, "onAdLoadSuccess: ");
                vlionRewardedVideoAd.notifyWinPrice();
            }
            /**
             *  激励视频加载失败
             *
             * @param vlionAdError  错误
             */
            @Override
            public void onAdLoadFailure(VlionAdError vlionAdError) {
                Logger.i(TAG, "onAdLoadFailure: " + vlionAdError.toString());
                Logger.i(TAG, "onAdLoadFailure: " + vlionAdError.getCode() + vlionAdError.getPlatformMSG());
                adStateListener.stopLoading();
                adStateListener.onError("onAdLoadFailure:" + "激励视频加载失败");
            }
            /**
             *  激励视频视图渲染成功
             *
             */
            @Override
            public void onAdRenderSuccess() {
                Logger.i(TAG, "onAdRenderSuccess: ");
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
                Logger.i(TAG, "onAdRenderFailure: " + vlionAdError.getCode() + vlionAdError.getPlatformMSG());
                Logger.i(TAG, "onAdRenderFailure: ");
                adStateListener.stopLoading();
                adStateListener.onError("onAdRenderFailure" + "激励视频视图渲染失败");
            }
            /**
             *  激励视频开始播放
             *
             */
            @Override
            public void onVideoStart() {
                Logger.i(TAG, "onVideoStart: ");
            }
            /**
             *  激励视频播放失败
             *
             * @param vlionAdError  错误
             */
            @Override
            public void onAdPlayFailure(VlionAdError vlionAdError) {
                Logger.i(TAG, "onAdPlayFailure: ");
                adStateListener.stopLoading();
                adStateListener.onError("onAdPlayFailure" + "激励视频播放失败");
            }
            /**
             * 激励视频曝光
             */
            @Override
            public void onAdExposure() {
                Logger.i(TAG, "onAdExposure: ");
            }

            @Override
            public void onAdShowFailure(VlionAdError vlionAdError) {
                Logger.i(TAG, "onAdShowFailure: " + vlionAdError.getCode() + vlionAdError.getPlatformMSG());
                Logger.i(TAG, "onAdShowFailure: ");
                adStateListener.stopLoading();
                adStateListener.onError("onAdShowFailure" + "激励视频播放失败");
            }

            /**
             * 激励视频播放完成
             */
            @Override
            public void onVideoCompleted() {
                Logger.i(TAG, "onVideoCompleted: ");
            }
            /**
             * 激励视频奖励发放
             */
            @Override
            public void onAdReward() {
                Logger.i(TAG, "onAdReward: ");
                isLoadSuccess = true;
                adStateListener.success();
            }
            /**
             * 激励视频跳过
             */
            @Override
            public void onAdVideoSkip() {
                Logger.i(TAG, "onAdVideoSkip: ");

            }
            /**
             * 激励视频点击
             */
            @Override
            public void onAdClick() {
                Logger.i(TAG, "onAdClick: ");
            }
            /**
             * 激励视频关闭
             */
            @Override
            public void onAdClose() {
                Logger.i(TAG, "onAdClose: ");
                adStateListener.onClose();
                if (isLoadSuccess){
                    adStateListener.successClose();
                }
            }
        });
        //加载广告
        vlionRewardedVideoAd.loadAd();
    }

    @Override
    public void loadAd(AdStateListener adStateListener) {
        if (!MentaAdManagerHold.isIsInitSuccess()){
            adStateListener.onError("初始化失败");
            return;
        }
        this.adStateListener = adStateListener;
        int width = mActivity.getResources().getDisplayMetrics().widthPixels;
        int height = mActivity.getResources().getDisplayMetrics().heightPixels;
        vlionSlotConfig = new VlionSlotConfig.Builder()
                .setSlotID(InitUtils.getBean().getRewardId())
                .setSize(isVertical ? width : height,isVertical? height : width) //模版广告尺寸宽高，单位px
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
