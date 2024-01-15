package com.yuanli.ad.listener;

public interface AdStateListener {
    void real();
    void success();

    void onError();

    void loading();

    void stopLoading();
}
