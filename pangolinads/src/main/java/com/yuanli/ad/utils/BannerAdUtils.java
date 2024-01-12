package com.yuanli.ad.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdLoadType;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.yuanli.ad.constants.AdConstants;
import com.yuanli.ad.holder.TTAdManagerHolder;
import com.yuanli.ad.widget.DislikeDialog;

import java.util.List;

public class BannerAdUtils {
    private Context mContext;
    private TTAdNative mTTAdNative;
    private TTNativeExpressAd mTTAd;
    private RelativeLayout mExpressContainer;
    private TextView welcomeTv;

    private long startTime = 0;
    private boolean mHasShowDownloadActive = false;
    private static final String TAG = "BannerAdUtils";

    public BannerAdUtils(Context context) {
        this.mContext = context;
        initAdConfig(context);
    }

    public void setExpressContainer(RelativeLayout expressContainer, TextView welcomeTv) {
        this.mExpressContainer = expressContainer;
        this.welcomeTv = welcomeTv;
    }

    public void setExpressContainer(RelativeLayout expressContainer) {
        this.mExpressContainer = expressContainer;
    }

    private void initAdConfig(Context context) {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(context);
        TTAdManagerHolder.get().requestPermissionIfNecessary(context);
    }

    /**
     * 加载Banner广告
     */
    public void loadPicBannerAd() {
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(AdConstants.AD_BANNER_ID) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(300, 130) //期望个性化模板广告view的size,单位dp
                .setAdLoadType(TTAdLoadType.PRELOAD)
                .build();

        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                mExpressContainer.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30000); //不调用则不进行轮播
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
                mTTAd.render();
            }
        });
    }

    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.d(TAG, "onAdClicked: 广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.d(TAG, "onAdShow: 广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
                Log.d(TAG, "onRenderFail: " + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                Log.d(TAG, "onRenderSuccess: 渲染成功");
                //返回view的宽高 单位 dp
                mExpressContainer.setVisibility(View.VISIBLE);
                if (welcomeTv != null) {
                    welcomeTv.setVisibility(View.GONE);
                }
                mExpressContainer.removeAllViews();
                mExpressContainer.addView(view);
            }
        });
        //dislike设置
        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                //Log.d(TAG, "onIdle: 点击开始下载");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    Log.d(TAG, "onDownloadActive: 下载中，点击暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                Log.d(TAG, "onDownloadPaused: 下载暂停，点击继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                Log.d(TAG, "onDownloadFailed: 下载失败，点击重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                Log.d(TAG, "onInstalled: 安装完成，点击图片打开");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                Log.d(TAG, "onDownloadFinished: 点击安装");
            }
        });
    }

    /**
     * 设置广告的不喜欢不再推荐。
     * 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getDislikeInfo().getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(mContext, words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    mExpressContainer.removeAllViews();
                    mExpressContainer.setVisibility(View.GONE);
                    if (welcomeTv != null) {
                        welcomeTv.setVisibility(View.VISIBLE);
                    }
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback((Activity) mContext, new TTAdDislike.DislikeInteractionCallback() {

            @Override
            public void onShow() {

            }

            @Override
            public void onSelected(int i, String s, boolean b) {
                //用户选择不喜欢原因后，移除广告展示
                mExpressContainer.removeAllViews();
                mExpressContainer.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: 点击取消");
            }

        });
    }

    public void release() {
        if (mTTAd != null) {
            //调用destroy()方法释放
            mTTAd.destroy();
        }
    }
}