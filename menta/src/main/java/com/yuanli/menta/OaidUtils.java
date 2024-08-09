package com.yuanli.menta;

import android.app.Application;


import com.bun.miitmdid.core.MdidSdkHelper;
import com.yuanli.base.log.Logger;


public class OaidUtils {

    public static void getOaid(Application application, OaidListener oaidListener){
        try {
            MdidSdkHelper.InitSdk(application.getApplicationContext(), true, (b, idSupplier) -> {
                Logger.i("TAG", "init: oaid: " + b);
                if (null != idSupplier) {
                    Logger.i("TAG", "init: oaid: " + idSupplier.getOAID());
                    oaidListener.onOaid(idSupplier.getOAID());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logger.i("XXXX","Exception："+ e);
        }
    }

    interface OaidListener{
        void onOaid(String oaid);
    }


}
