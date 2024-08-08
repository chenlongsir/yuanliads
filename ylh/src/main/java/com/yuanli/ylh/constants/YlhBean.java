package com.yuanli.ylh.constants;

public class YlhBean {

    private final String appId;
    private final String rewardId;

    public YlhBean(String appId, String rewardId) {
        this.appId = appId;
        this.rewardId = rewardId;
    }

    public String getAppId() {
        return appId;
    }

    public String getRewardId() {
        return rewardId;
    }
}
