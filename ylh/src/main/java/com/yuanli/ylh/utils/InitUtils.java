package com.yuanli.ylh.utils;


import com.yuanli.ylh.constants.YlhBean;

public class InitUtils {
   private static YlhBean bean;
   
   public static void init(YlhBean bean){
       InitUtils.bean = bean;
   }

   public static YlhBean getBean(){
       if (bean == null){
           throw new RuntimeException("constants is empty");
       }
       return bean;
   }

}
