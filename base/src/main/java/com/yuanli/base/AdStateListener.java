package com.yuanli.base;

public interface AdStateListener {
    /*
    * 绑定接口到
    * */
    void real(AdListener adListener);
    void success();
    void onError();
    default void onClose(){

    };

    default void successClose(){

    };
    void loading();
    void stopLoading();
   default void toast(String msg){

   };
}
