package com.yuanli.pangolin.utils;

import com.yuanli.pangolin.constants.PangolinAdBean;

public class InitUtils {
   private static PangolinAdBean adConstants;
   
   public static void init(PangolinAdBean adConstants){
       InitUtils.adConstants = adConstants;
   }

   public static PangolinAdBean getBean(){
       if (adConstants == null){
           throw new RuntimeException("constants is empty");
       }
       return adConstants;
   }

}
