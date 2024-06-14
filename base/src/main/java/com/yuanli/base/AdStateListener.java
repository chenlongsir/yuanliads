package com.yuanli.base;

public interface AdStateListener {
    void real();
    void success();
    void onError();
    void loading();
    void stopLoading();
   default void toast(String msg){

   };
}
