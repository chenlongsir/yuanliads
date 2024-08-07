package com.yuanli.base;

public interface AdStateListener {
    /*
    * 初始化成功之后回调接口
    * */
    default void real(Ad ad){
        ad.showAd();
    }
    void success();
    void onError();
    default void onClose(){

    }

    default void successClose(){

    }
    void loading();
    void stopLoading();
   default void toast(String msg){

   }
}
