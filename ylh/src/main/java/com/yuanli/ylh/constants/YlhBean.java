package com.yuanli.ylh.constants;

public class YlhBean {

    private final String appId;
    private final String appName;
    private final long rewardId;

    public YlhBean(String appId, String appName, long rewardId) {
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

    public long getRewardId() {
        return rewardId;
    }

}
