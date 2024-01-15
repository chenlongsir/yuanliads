package com.yuanli.ad.utils;

import android.app.Activity;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.yuanli.ad.listence.AdListence;
import com.yuanli.ad.constants.AdConstants;

import static com.bytedance.sdk.openadsdk.TTAdLoadType.PRELOAD;

/**
 * Author by Longgege, Email 2657450210@qq.com, Date on 2021/11/3$.
 * PS: Not easy to write code, please indicate.
 */
public class NewInsertAdUtlis {
    private Activity activity;
    private TTAdNative mTTAdNative;


    public void init(Activity activity, AdListence listence){
        this.activity = activity;
        mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity);
        this.listence = listence;
    }

    public void loadingAd(){
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(AdConstants.AD_NEW_INSERT_ID)
                //模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可
                .setExpressViewAcceptedSize(500,500)
                .setSupportDeepLink(true)
                .setOrientation(TTAdConstant.HORIZONTAL)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .setAdLoadType(PRELOAD)//推荐使用，用于标注此次的广告请求用途为预加载（当做缓存）还是实时加载，方便后续为开发者优化相关策略
                .build();

        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.d("xxx" ,"initerror");
                if (listence != null){
                    listence.onError(s);
                }
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
                Log.d("xxx" ,"inits");
            }

            @Override
            public void onFullScreenVideoCached() {

            }

            @Override
            public void onFullScreenVideoCached(TTFullScreenVideoAd ttFullScreenVideoAd) {
                Log.d("xxx" ,"init");
                if (ttFullScreenVideoAd != null){
                    ttFullScreenVideoAd.showFullScreenVideoAd(activity, TTAdConstant.RitScenes.GAME_GIFT_BONUS, null);
                    ttFullScreenVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
                        @Override
                        public void onAdShow() {
                            Log.d("xxx" ,"init");
                        }

                        @Override
                        public void onAdVideoBarClick() {

                        }

                        @Override
                        public void onAdClose() {
                            if (listence != null){
                                listence.onClose();
                            }
                        }

                        @Override
                        public void onVideoComplete() {
                            if (listence != null){
                                listence.onClose();
                            }
                        }

                        @Override
                        public void onSkippedVideo() {
                            if (listence != null){
                                listence.onClose();
                            }
                        }
                    });
                }

            }
        });
    }

    private AdListence listence;


    public void onDestory(){
    }
}
