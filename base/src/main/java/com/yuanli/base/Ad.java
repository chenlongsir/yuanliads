package com.yuanli.base;

/**
 *
 */
public interface Ad {
    void loadAd(AdStateListener adStateListener);

    void showAd();

    void onDestroy();
}
