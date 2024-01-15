package com.yuanli.ad.listener;

/**
 *
 */
public interface AdListener {
    void loadAd(AdStateListener adStateListener);

    void showAd();

    void onDestroy();
}
