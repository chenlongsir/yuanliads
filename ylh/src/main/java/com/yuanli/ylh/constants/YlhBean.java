package com.yuanli.ylh.constants;

public class YlhBean {

    private final String appId;
    private final String appName;
    private final String rewardId;

    public YlhBean(String appId, String appName, String rewardId) {
        this.appId = appId;
        this.appName = appName;
        this.rewardId = rewardId;
    }
    public String getAppId() {
        return appId;
    }

    public String getAppName() {
        return appName;
    }

    public String getRewardId() {
        return rewardId;
    }

}
