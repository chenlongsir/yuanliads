package com.yuanli.ad.utils;

import com.yuanli.ad.constants.AdConstants;

public class InitUtils {
   private static AdConstants constants;
   
   public static void init(AdConstants adConstants){
       constants = adConstants;
   }

   public static AdConstants getConstants(){
       if (constants == null){
           throw new RuntimeException("constants is empty");
       }
       return constants;
   }

}
