package com.yuanli.ksads.constants;

public class KsBean {

    private final String appId;
    private final String appName;
    private final long rewardId;

    public KsBean(String appName, String appId, long rewardId) {
        this.appName = appName;
        this.appId = appId;
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
