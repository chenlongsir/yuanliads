package com.yuanli.ad.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.yuanli.ad.constants.AdConstants;
import com.yuanli.ad.holder.TTAdManagerHolder;

public class RewardAdUtils {
    private Context mContext;
    private TTAdNative mTTAdNative;
    private static TTRewardVideoAd mttRewardVideoAd;

    public RewardAdUtils(Context context){
        this.mContext = context;
        initAdConfig(context);
    }

    private void initAdConfig(Context context) {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(context);
        TTAdManagerHolder.get().requestPermissionIfNecessary(context);
    }

    public void loadRewardAd(final TTRewardVideoAd.RewardAdInteractionListener listener){

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(AdConstants.AD_REWARD_ID)
                .setSupportDeepLink(true)
                .setAdCount(1)
                //个性化模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可。仅模板广告需要设置此参数
                //.setExpressViewAcceptedSize(500,500)
                .setImageAcceptedSize(1080, 1920)
                //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传
                //可设置为空字符串
                .setUserID(null)
                .setOrientation(TTAdConstant.VERTICAL)  //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
                .build();

        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int errorCode, String msg) {
                //加载异常
                Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                mttRewardVideoAd = ttRewardVideoAd;
                mttRewardVideoAd.setRewardAdInteractionListener(listener);
                mttRewardVideoAd.showRewardVideoAd((Activity) mContext);
            }
            @Override
            public void onRewardVideoCached() {
                //缓存到本地
            }

            @Override
            public void onRewardVideoCached(TTRewardVideoAd ttRewardVideoAd) {

            }
        });
    }

    //销毁对象
    public void destruction(){
        if (mttRewardVideoAd != null) {
            mttRewardVideoAd = null;
        }
    }
}
