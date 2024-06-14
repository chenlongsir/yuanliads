package com.yuanli.menta;

public interface MentaAdBack {
    void onFail();
    void onSuccess();
    void onSkip();
    void onClose();
    //激励视频 奖励发放
    void onAdReward();
}
