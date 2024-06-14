package com.yuanli.menta.constants;

import com.yuanli.base.BaseAdBean;

public class MentaBean extends BaseAdBean {
    private final String appKey;
    public MentaBean(String appId, String appKey, String splashId, String insertId, String rewardId) {
        super(appId,  splashId, insertId, rewardId);
        this.appKey = appKey;
    }
    public String getAppKey() {
        return appKey;
    }
}
