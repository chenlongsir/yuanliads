package com.yuanli.ad.listence;

import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;

/**
 * Author by Longgege, Email 2657450210@qq.com, Date on 2021/11/3$.
 * PS: Not easy to write code, please indicate.
 */
public interface AdListence {
    void onSuccses(TTFullScreenVideoAd listence);

    void onError(String msg);

    void onClose();
}
