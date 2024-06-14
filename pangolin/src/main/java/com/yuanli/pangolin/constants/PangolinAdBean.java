package com.yuanli.pangolin.constants;

import com.yuanli.base.BaseAdBean;

public class PangolinAdBean extends BaseAdBean {

    private String bannerId;
    private final String appName;

    public PangolinAdBean(String appName, String appId,  String splashId, String insertId, String rewardId) {
        super(appId, splashId, insertId, rewardId);
        this.appName = appName;
    }

    public PangolinAdBean(String appName, String appId,  String splashId, String insertId, String rewardId,String bannerId) {
        super(appId, splashId, insertId, rewardId);
        this.bannerId = bannerId;
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public String getBannerId() {
        return bannerId;
    }
}
