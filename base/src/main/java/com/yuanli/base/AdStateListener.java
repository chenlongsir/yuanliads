package com.yuanli.base;

public interface AdStateListener {
    void real(AdListener adListener);
    void success();
    void onError();
    void loading();
    void stopLoading();
   default void toast(String msg){

   };
}
