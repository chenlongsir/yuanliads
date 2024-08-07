package com.yuanli.menta.utils;

import android.app.Activity;

import com.yuanli.base.Ad;
import com.yuanli.base.AdStateListener;
import com.yuanli.menta.MentaAdManagerHold;

import cn.vlion.ad.inland.base.util.config.VlionScaleType;
import cn.vlion.ad.inland.core.config.VlionAdError;
import cn.vlion.ad.inland.core.config.VlionSlotConfig;
import cn.vlion.ad.inland.core.interstitial.VlionInterstitialAd;
import cn.vlion.ad.inland.core.interstitial.VlionInterstitialListener;

public class MentaInsertAdUtils implements Ad {
    private VlionSlotConfig vlionSlotConfig;
    private Activity mActivity;
    private VlionInterstitialAd vlionInterstitialAd;
    private AdStateListener adStateListener;

    public MentaInsertAdUtils(Activity activity) {
        mActivity = activity;
    }


    private void loadMentaInsertAd() {
        adStateListener.loading();
        vlionInterstitialAd = new VlionInterstitialAd(mActivity, vlionSlotConfig);
        vlionInterstitialAd.setVlionInterstitialListener(new VlionInterstitialListener() {
            /**
             * 广告数据加载成功
             */
            @Override
            public void onAdLoadSuccess(double price) {
                //竞价成功调用
                vlionInterstitialAd.notifyWinPrice();
            }

            /**
             * 广告数据加载失败
             *
             * @param vlionAdError  错误
             */
            @Override
            public void onAdLoadFailure(VlionAdError vlionAdError) {
                adStateListener.stopLoading();
                adStateListener.onError();
            }

            /**
             * 广告视图渲染成功
             *
             */
            @Override
            public void onAdRenderSuccess() {
                adStateListener.stopLoading();
                adStateListener.real(MentaInsertAdUtils.this);
            }

            /**
             * 广告视图渲染失败
             *
             * @param vlionAdError  错误
             */
            @Override
            public void onAdRenderFailure(VlionAdError vlionAdError) {
                adStateListener.stopLoading();
                adStateListener.onError();
            }

            /**
             * 广告曝光
             */
            @Override
            public void onAdExposure() {

            }

            @Override
            public void onAdShowFailure(VlionAdError vlionAdError) {
                adStateListener.stopLoading();
                adStateListener.onError();
            }

            /**
             * 广告点击
             */
            @Override
            public void onAdClick() {

            }

            /**
             * 广告关闭
             */
            @Override
            public void onAdClose() {
               adStateListener.success();
               adStateListener.onClose();
            }
        });
        //加载广告
        vlionInterstitialAd.loadAd();
    }


    @Override
    public void loadAd(AdStateListener adStateListener) {
        if(MentaAdManagerHold.isIsInitSuccess()){
            adStateListener.onError();
            return;
        }
        this.adStateListener = adStateListener;
        int width = mActivity.getResources().getDisplayMetrics().widthPixels;
        int height = mActivity.getResources().getDisplayMetrics().heightPixels;
        vlionSlotConfig = new VlionSlotConfig.Builder()
                .setSlotID(InitUtils.getBean().getInsertId())
                .setSize(width, height) //模版广告尺寸宽高，单位px
                .setTolerateTime(5f)//广告加载容忍时间1f~5f。如果设定的时间内没有竞价到广告，则判断竞价失败

                //设置图片的填充方式
                //AD_IMAGE_SCALING_FITCENTER   对标ImageView设置scaleType 的fitCenter
                //AD_IMAGE_SCALING_FITXY			 对标ImageView设置scaleType fitXY
                //AD_IMAGE_SCALING_CENTER_CROP 对标ImageView设置scaleType centerCrop
                .setImageScale(VlionScaleType.AD_IMAGE_SCALING_CENTER_CROP)
                .build();
        loadMentaInsertAd();
    }

    @Override
    public void showAd() {
        //广告视图渲染成功展示
        vlionInterstitialAd.showAd();
    }

    @Override
    public void onDestroy() {
        if (null != vlionInterstitialAd) {
            vlionInterstitialAd.destroy();
            vlionInterstitialAd = null;
        }
    }
}
