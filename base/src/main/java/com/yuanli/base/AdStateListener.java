package com.yuanli.base;

public interface AdStateListener {
    /*
     * 初始化成功之后回调接口
     * */
    default void real(Ad ad) {
        ad.showAd();
    }

    /*
    * success
    * 观看广告成功，但是这个不适合在activity 保存后之接关闭页面调用，如果清空了 listener 会报错
    * */
    default void success() {

    }
    /*
     * successClose
     * 观看广告成功并关闭
     * */
    default void successClose() {

    }

    void onError(String msg);

    default void onClose() {

    }

    void loading();

    void stopLoading();

    default void toast(String msg) {

    }
}
