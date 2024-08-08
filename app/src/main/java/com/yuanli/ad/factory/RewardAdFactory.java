package com.yuanli.ad.factory;

import android.app.Activity;

import com.yuanli.ad.AdConstants;
import com.yuanli.ad.AdType;
import com.yuanli.baidu.utils.BaiduRewardAdUtils;
import com.yuanli.base.Ad;
import com.yuanli.ksads.utils.KsRewardAdUtils;
import com.yuanli.pangolin.utils.RewardAdUtils;
import com.yuanli.ylh.utils.YlhRewardAdUtils;

public class RewardAdFactory {
    public static Ad createRewardAd(@AdType int adType, Activity activity)
    {
        switch (adType)
        {
            case AdConstants.AD_TYPE_BAIDU:
                return new BaiduRewardAdUtils(activity);
            case AdConstants.AD_TYPE_KS:
                return new KsRewardAdUtils(activity);
            case AdConstants.AD_TYPE_PANGOLIN:
                return new RewardAdUtils(activity);
            case AdConstants.AD_TYPE_YLH:
                return new YlhRewardAdUtils(activity);
        }
        return new RewardAdUtils(activity);
    }
}
