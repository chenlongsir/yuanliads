package com.yuanli.base;

/**
 *
 */
public interface AdListener {
    void loadAd(AdStateListener adStateListener);

    void showAd();

    void onDestroy();
}
