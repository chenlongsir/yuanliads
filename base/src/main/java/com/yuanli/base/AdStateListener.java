package com.yuanli.base;

public interface AdStateListener {
    void real(AdListener adListener);
    void success();
    void onError();
    default void onClose(){

    };
    void loading();
    void stopLoading();
   default void toast(String msg){

   };
}
