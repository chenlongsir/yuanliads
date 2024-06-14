package com.yuanli.menta.utils;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.yuanli.menta.MentaAdBack;
import com.yuanli.menta.MentaAdManagerHold;
import com.yuanli.menta.constants.MentaConstants;

import cn.vlion.ad.inland.base.util.config.VlionScaleType;
import cn.vlion.ad.inland.core.config.VlionAdError;
import cn.vlion.ad.inland.core.config.VlionSlotConfig;
import cn.vlion.ad.inland.core.splash.VlionSplashAd;
import cn.vlion.ad.inland.core.splash.VlionSplashListener;

public class MentaSplashAdUtils {
    private VlionSlotConfig vlionSlotConfig;
    private Activity mActivity;
    private ViewGroup mViewGroup;
    private VlionSplashAd vlionSplashAd;
    private MentaAdBack mMentaSplashBack;

    /**
     * 加载开屏广告
     * @param activity
     * @param viewGroup
     * @param mentaSplashBack
     */
    public void showSplashAd(Activity activity, ViewGroup viewGroup, MentaAdBack mentaSplashBack){
        if (!MentaAdManagerHold.isIsInitSuccess()){
            mentaSplashBack.onFail();
            return;
        }
        mActivity = activity;
        mViewGroup = viewGroup;
        mMentaSplashBack = mentaSplashBack;
        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;
        vlionSlotConfig = new VlionSlotConfig.Builder()
                .setSlotID(InitUtils.getBean().getSplashId())
                .setSize(width, height) //模版广告尺寸宽高，单位px
                .setTolerateTime(5f)//广告加载容忍时间1f~5f。如果设定的时间内没有竞价到广告，则判断竞价失败

                //设置图片的填充方式
                //AD_IMAGE_SCALING_FITCENTER   对标ImageView设置scaleType 的fitCenter
                //AD_IMAGE_SCALING_FITXY			 对标ImageView设置scaleType fitXY
                //AD_IMAGE_SCALING_CENTER_CROP 对标ImageView设置scaleType centerCrop
                .setImageScale(VlionScaleType.AD_IMAGE_SCALING_FITCENTER)
                .build();

        loadMentaSplashAd();




    }

    private void loadMentaSplashAd() {
        vlionSplashAd = new VlionSplashAd(mActivity, vlionSlotConfig);

        vlionSplashAd.setVlionSplashListener(new VlionSplashListener() {
            /**
             * 广告数据加载成功
             */
            @Override
            public void onAdLoadSuccess(double price) {
                Log.d(MentaConstants.TAG,"onAdLoadSuccess:"+price);
                //竞价成功调用
                vlionSplashAd.notifyWinPrice();
            }
            /**
             * 广告数据加载失败
             *
             * @param vlionAdError  错误
             */
            @Override
            public void onAdLoadFailure(VlionAdError vlionAdError) {
                Log.d(MentaConstants.TAG,"onAdLoadFailure:"+vlionAdError.code+"--msg:"+vlionAdError.getDesc());
                if (mMentaSplashBack != null){
                    mMentaSplashBack.onFail();
                }
            }
            /**
             * 广告视图渲染成功
             *
             */
            @Override
            public void onAdRenderSuccess() {
                Log.d(MentaConstants.TAG,"onAdRenderSuccess:");
                //广告视图渲染成功调用，添加到自己的容器中
                mViewGroup.removeAllViews();
                vlionSplashAd.showAd(mViewGroup);

            }
            /**
             * 广告视图渲染失败
             *
             * @param vlionAdError  错误
             */
            @Override
            public void onAdRenderFailure(VlionAdError vlionAdError) {

            }
            /**
             * 广告曝光
             */
            @Override
            public void onAdExposure() {

            }

            @Override
            public void onAdShowFailure(VlionAdError vlionAdError) {
                Log.d(MentaConstants.TAG,"onAdShowFailure:"+vlionAdError.code+"--msg:"+vlionAdError.getDesc());
            }

            /**
             * 广告点击
             */
            @Override
            public void onAdClick() {
                Log.d(MentaConstants.TAG,"onAdClick:");
            }

            @Override
            public void onAdSkip() {
                Log.d(MentaConstants.TAG,"onAdSkip:");
                if (mMentaSplashBack != null){
                    mMentaSplashBack.onSkip();
                }
            }

            /**
             * 广告关闭
             */
            @Override
            public void onAdClose() {
                Log.d(MentaConstants.TAG,"onAdClose:");
                if (mMentaSplashBack != null){
                    mMentaSplashBack.onClose();
                }
            }
        });
        //加载广告
        vlionSplashAd.loadAd();
    }

    public void onAdDestroy(){
        if (null != vlionSplashAd) {
            vlionSplashAd.destroy();
            vlionSplashAd=null;
        }
    }

}
