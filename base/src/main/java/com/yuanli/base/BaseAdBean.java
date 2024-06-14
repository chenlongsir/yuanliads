package com.yuanli.base;

public class BaseAdBean {
    protected String appId;
    protected String splashId;
    protected String insertId;
    protected String rewardId;

    public BaseAdBean(String appId, String splashId, String insertId, String rewardId) {
        this.appId = appId;
        this.splashId = splashId;
        this.insertId = insertId;
        this.rewardId = rewardId;
    }

    public String getAppId() {
        return appId;
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
