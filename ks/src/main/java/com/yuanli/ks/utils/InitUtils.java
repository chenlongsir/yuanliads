package com.yuanli.ks.utils;


import com.yuanli.ks.constants.KsBean;

public class InitUtils {
   private static KsBean ksBean;
   
   public static void init(KsBean ksBean){
       InitUtils.ksBean = ksBean;
   }

   public static KsBean getBean(){
       if (ksBean == null){
           throw new RuntimeException("constants is empty");
       }
       return ksBean;
   }

}
