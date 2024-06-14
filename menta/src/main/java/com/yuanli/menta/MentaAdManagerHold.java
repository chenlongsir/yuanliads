package com.yuanli.menta;

import android.app.Application;
import android.util.Log;

import com.yuanli.menta.constants.MentaBean;
import com.yuanli.menta.constants.MentaConstants;
import com.yuanli.menta.utils.InitUtils;

import cn.vlion.ad.inland.base.util.init.VlionLocation;
import cn.vlion.ad.inland.base.util.init.VlionPrivateController;
import cn.vlion.ad.inland.base.util.init.VlionSdkConfig;
import cn.vlion.ad.inland.core.init.VlionSDk;

public class MentaAdManagerHold {
    private static Application mApplication;
    private static String oaid;

    private static boolean isInitSuccess = false;

    public static void init(Application application, MentaBean mentaBean){
        OaidUtils.getOaid(application,(oaid)->{
            Log.d("TAG", "init: oaid" + oaid);
            InitUtils.init(mentaBean);
            initMentaAd(application,oaid);
        });
    }
    public static void initMentaAd(Application application,String oaid) {
        isInitSuccess = true;
        mApplication = application;
        MentaAdManagerHold.oaid = oaid;
        VlionSdkConfig config = new VlionSdkConfig
                .Builder()
                .setAppId(InitUtils.getBean().getAppId())//媒体在平台申请的 APP_ID
                .setAppKey(InitUtils.getBean().getAppKey())//媒体在平台申请的 APP_KEY
                .setEnableLog(true)//测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .setPrivateController(createPrivateController)//隐私信息控制设置，此项必须设置！！
                .build();
        //个性化推荐广告
        VlionSDk.setPersonalizedAdState(true);
        //初始化广告SDK
        VlionSDk.init(application, config);
    }

    public static boolean isIsInitSuccess() {
        return isInitSuccess;
    }

    private static VlionPrivateController createPrivateController = new VlionPrivateController() {
        /**
         * 是否允许SDK主动使用手机硬件参数，如：imei
         *
         * @return true可以使用，false禁止使用。false
         */
        @Override
        public boolean isCanUsePhoneState() {
            return true;
        }

        /**
         * 当 isCanUsePhoneState=false 时，
         * 可传入 imei 信息，sdk使用您传入的 imei 信息
         *
         * @return imei信息
         */
        @Override
        public String getImei() {
            return "";
        }

        /**
         * 开发者可以传入自己获取OAID 如果没有获取OAID 请务必在主module的build.gradle文件添加瑞狮SDK依赖
         *  api 'cn.vlion.inland:oaid_sdk:1.0.25'
         * 并导入 supplierconfig.json 文件到assets 目录里，这样瑞狮SDK会自动获取OAID
         *
         * @return oaid
         */
        @Override
        public String getOaid() {
//            try {
//                XNMSAHelper XNMSAHelper = new XNMSAHelper(new XNMSAHelper.AppIdsUpdater() {
//                    @Override
//                    public void OnIdsAvalid(String ids) {
//                        if (!TextUtils.isEmpty(ids)) {
//                            oaid = ids;
//                        }
//                        LogVlion.e("MyApplication OnIdsAvalid ids=" + ids);
//                        LogVlion.e("MyApplication OnIdsAvalid MD5Utils=" + MD5Utils.encode(ids));
//
//                    }
//                });
//                XNMSAHelper.callFromReflect(mApplication);
//            } catch (Exception e) {
//                LogVlion.e("MyApplication OnIdsAvalid Exception=" + e);
//            } catch (Throwable e) {
//                LogVlion.e("MyApplication OnIdsAvalid Throwable=" + e);
//            }

            return oaid;
        }

        /**
         * 是否允许SDK主动使用地理位置信息
         *
         * @return true可以获取，false禁止获取。默认为false
         */
        @Override
        public boolean isCanUseLocation() {
            return false;
        }

        /**
         * 当isCanUseLocation=false时，可传入地理位置信息，sdk使用您传入的地理位置信息
         *
         * @return 地理位置参数
         */
        @Override
        public VlionLocation getLocation() {
            return new VlionLocation();
        }

        @Override
        public boolean isCanUseGaid() {
            return true;
        }

        /**
         *  isCanReadAppList=false时，不能读取应用安装列表，true的时候可以读取应用安装列表,默认为false
         * @return
         */
        @Override
        public boolean isCanReadAppList() {
            return false;
        }
    };





}
