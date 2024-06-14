package com.yuanli.base;

public class BaseAdBean {
    protected String appName;
    protected String appId;
    protected String appKey;
    protected String splashId;
    protected String insertId;
    protected String rewardId;

    public BaseAdBean(String appName, String appId, String appKey, String splashId, String insertId, String rewardId) {
        this.appName = appName;
        this.appId = appId;
        this.appKey = appKey;
        this.splashId = splashId;
        this.insertId = insertId;
        this.rewardId = rewardId;
    }
    public String getAppName() {
        return appName;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getSplashId() {
        return splashId;
    }

    public String getInsertId() {
        return insertId;
    }

    public String getRewardId() {
        return rewardId;
    }
}
