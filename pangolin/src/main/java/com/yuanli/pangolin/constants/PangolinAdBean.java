package com.yuanli.pangolin.constants;

import com.yuanli.base.BaseAdBean;

public class PangolinAdBean extends BaseAdBean {

    private final String bannerId;

    public PangolinAdBean(String appName, String appId, String appKey, String splashId, String insertId, String rewardId,String bannerId) {
        super(appName, appId, appKey, splashId, insertId, rewardId);
        this.bannerId = bannerId;
    }

    public String getBannerId() {
        return bannerId;
    }
}
