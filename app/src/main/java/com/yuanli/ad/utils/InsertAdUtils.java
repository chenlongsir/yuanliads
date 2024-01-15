package com.yuanli.ad.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.yuanli.ad.listence.AdListence;
import com.yuanli.ad.constants.AdConstants;

import java.util.List;

public class InsertAdUtils {
    private Context mContext;
    private TTAdNative mTTAdNative;
    public InsertAdUtils(Context context,AdListence listence) {
        this.mContext = context;
        this.listence = listence;
        initAdConfig(context);
    }

    private void initAdConfig(Context context) {
        mTTAdNative = TTAdSdk.getAdManager().createAdNative(context);
//        TTAdManagerHolder.get().requestPermissionIfNecessary(context);
    }

    public void loadInsertAd() {
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(AdConstants.AD_INSERT_ID) //广告位id
                .setSupportDeepLink(false)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(350, 500) //期望模板广告view的size,单位dp
                .setNativeAdType(AdSlot.TYPE_INTERACTION_AD)
                .build();

        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            //请求广告失败
            @Override
            public void onError(int code, String message) {
                if (listence != null){
                    listence.onError(message);
                }
            }

            //请求广告成功
            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads.get(0) == null) {
                    return;
                }
                final TTNativeExpressAd ad = ads.get(0);
                ad.showInteractionExpressAd((Activity) mContext);
                ad.render();
                ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {

                    @Override
                    public void onAdDismiss() {
                        if (listence != null){
                            listence.onClose();
                        }
                    }

                    @Override
                    public void onAdClicked(View view, int i) {
                        Log.d("xxx" ,"onAdClicked");
                    }

                    @Override
                    public void onAdShow(View view, int i) {
                        Log.d("xxx" ,"onAdShow");

                    }

                    @Override
                    public void onRenderFail(View view, String s, int i) {
                        Log.d("xxx" ,"onRenderFail");

                    }

                    @Override
                    public void onRenderSuccess(View view, float v, float v1) {
                        Log.d("xxx" ,"onRenderSuccess");

                    }
                });

            }
        });
    }

    private AdListence listence;


//    public void initTTF(Activity activity,AdListence listence){
//        this.mContext = activity;
//        this.listence = listence;
//        TTAdManagerHolder.init(activity, new TTAdSdk.InitCallback() {
//            @Override
//            public void success() {
//                initAdConfig(mContext);
//                loadInsertAd();
//            }
//
//            @Override
//            public void fail(int i, String s) {
//
//            }
//        });
//    }

    public void onDestroy() {
        if (mTTAdNative != null) {
            mTTAdNative = null;
        }
    }
}
