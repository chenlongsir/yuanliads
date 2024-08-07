package com.yuanli.ylh.utils;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;
import com.yuanli.base.AdListener;
import com.yuanli.base.AdStateListener;
import com.yuanli.ylh.YlhManagerHolder;
import com.yuanli.ylh.constants.YlhConstants;

import java.util.Map;

public class YlhRewardAdUtils implements AdListener {
    private Activity activity;
    private AdStateListener adStateListener;
    private boolean isLoadSuccess = false;
    private RewardVideoAD rewardVideoAD; // 有声播放

    public YlhRewardAdUtils(Activity activity) {
        this.activity = activity;
    }

    public void loadAd(final AdStateListener adStateListener) {
        isLoadSuccess = false;
        YlhRewardAdUtils.this.adStateListener = adStateListener;
        YlhManagerHolder.setInitListener(new YlhManagerHolder.InitListener() {
            @Override
            public void onSuccess() {
                adStateListener.loading();
                rewardVideoAD = new RewardVideoAD(activity, InitUtils.getBean().getRewardId(), rewardAdInteractionListener);
                rewardVideoAD.loadAD();
            }

            @Override
            public void onError(String msg) {
                adStateListener.stopLoading();
                adStateListener.onError();
                adStateListener.toast(msg);
            }
        });
    }

    private final RewardVideoADListener rewardAdInteractionListener = new RewardVideoADListener() {
        @Override
        public void onADLoad() {
            adStateListener.stopLoading();
            Log.d(YlhConstants.TAG, "onADLoad: ");
            adStateListener.real(YlhRewardAdUtils.this);
        }

        @Override
        public void onVideoCached() {
            Log.d(YlhConstants.TAG, "onVideoCached: ");

        }

        @Override
        public void onADShow() {
            Log.d(YlhConstants.TAG, "onADShow: ");
        }

        @Override
        public void onADExpose() {
            Log.d(YlhConstants.TAG, "onADExpose: ");

        }

        @Override
        public void onReward(Map<String, Object> map) {
            Log.d(YlhConstants.TAG, "onReward: ");

        }

        @Override
        public void onADClick() {
            Log.d(YlhConstants.TAG, "onADClick: ");

        }

        @Override
        public void onVideoComplete() {
            Log.d(YlhConstants.TAG, "onVideoComplete: ");
            isLoadSuccess = true;
        }

        @Override
        public void onADClose() {
            Log.d(YlhConstants.TAG, "onADClose: ");
            adStateListener.success();
            if (isLoadSuccess) {
                adStateListener.successClose();
            }
        }

        @Override
        public void onError(AdError adError) {
            adStateListener.stopLoading();
            Log.d(YlhConstants.TAG, "onError: " + adError.getErrorMsg());
            adStateListener.onError();
        }
    };



    @Override
    public void showAd() {
        // 3.展示广告
        if (!rewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
            //广告展示检查3：展示广告前判断广告数据未过期
            if (rewardVideoAD.isValid()) {
                rewardVideoAD.showAD();
            } else {
                refresh();
                Toast.makeText(activity, "激励视频广告已过期，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
            }
        } else {
            refresh();
            Toast.makeText(activity, "此条广告已经展示过，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
        }
    }

    private void refresh(){
        adStateListener.loading();
        rewardVideoAD.loadAD();
    }

    @Override
    public void onDestroy() {
        rewardVideoAD = null;
        YlhManagerHolder.clear();
        adStateListener = null;
        activity = null;
    }
}
