package com.yuanli.menta;//package com.yuanli.mentaAd;
//
//import android.content.Context;
//
//import com.bun.miitmdid.core.MdidSdkHelper;
//import com.bun.miitmdid.interfaces.IIdentifierListener;
//import com.bun.miitmdid.interfaces.IdSupplier;
//
//
//import cn.vlion.ad.inland.base.util.log.LogVlion;
//
//
//public final class XNMSAHelper implements IIdentifierListener {
//    private AppIdsUpdater listener;
//
//    public XNMSAHelper(AppIdsUpdater callback) {
//        listener = callback;
//    }
//
//
//    /**
//     * 方法调用
//     */
//    public int callFromReflect(Context cxt) {
//        try {
//            return MdidSdkHelper.InitSdk(cxt, true, this);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogVlion.e("MyApplication callFromReflect Exception=" + e.getMessage());
//        }
//        return -1;
//    }
//
//    /**
//     * 获取相应id
//     */
//    @Override
//    public void OnSupport(boolean isSupport, IdSupplier _supplier) {
//        if (_supplier == null) {
//            return;
//        }
//        String oaid = _supplier.getOAID();
//        LogVlion.e("MyApplication OnSupport oaid=" + oaid);
//        if (null != listener) {
//            listener.OnIdsAvalid(oaid);
//        }
//    }
//
//    public interface AppIdsUpdater {
//        void OnIdsAvalid(String ids);
//    }
//
//}
