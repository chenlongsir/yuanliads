package com.yuanli.pangolin.utils;

import android.app.Activity;


import com.yuanli.base.Ad;
import com.yuanli.base.AdStateListener;

import java.util.HashMap;
import java.util.Map;

public class InsertOrRewardUtils implements Ad {

    private Ad adUtils;

    private Map<AdType, Ad> map = new HashMap<>();

    public InsertOrRewardUtils(Activity activity, AdType adType) {
        map.put(AdType.INSERT,new InsertAdUtils(activity));
        map.put(AdType.REWARD,new RewardAdUtils(activity));

        adUtils = map.get(adType);
    }

    @Override
    public void loadAd(AdStateListener adStateListener) {
        if (adUtils != null){
            adUtils.loadAd(adStateListener);
        }
    }

    @Override
    public void showAd() {
        if (adUtils != null){
            adUtils.showAd();
        }
    }

    @Override
    public void onDestroy() {
        if (adUtils != null){
            adUtils.onDestroy();
        }
        adUtils = null;
    }

    public enum AdType {
        INSERT,
        REWARD
    }

}
