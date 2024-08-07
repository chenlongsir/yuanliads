package com.yuanli.baidu.constants;

import com.yuanli.base.BaseAdBean;

public class BaiduBean {
    private final String appName;
    private final String appId;
    private final String rewardId;
    public BaiduBean(String appName, String appId, String rewardId) {
        this.appName = appName;
        this.appId = appId;
        this.rewardId = rewardId;
    }
    public String getAppName() {
        return appName;
    }

    public String getAppId() {
        return appId;
    }

    public String getRewardId() {
        return rewardId;
    }
}
