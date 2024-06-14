package com.yuanli.menta;

import android.app.Application;
import android.util.Log;

import com.bun.miitmdid.core.MdidSdkHelper;


public class OaidUtils {

    public static void getOaid(Application application, OaidListener oaidListener){
        try {
            MdidSdkHelper.InitSdk(application.getApplicationContext(), true, (b, idSupplier) -> {
                Log.d("TAG", "init: oaid: " + b);
                if (null != idSupplier) {
                    Log.d("TAG", "init: oaid: " + idSupplier.getOAID());
                    oaidListener.onOaid(idSupplier.getOAID());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("XXXX","Exception："+ e);
        }
    }

    interface OaidListener{
        void onOaid(String oaid);
    }


}
