package com.yuanli.baidu.utils;


import com.yuanli.baidu.constants.BaiduBean;

public class InitUtils {
   private static BaiduBean baiduBean;
   
   public static void init(BaiduBean baiduBean){
       InitUtils.baiduBean = baiduBean;
   }

   public static BaiduBean getBean(){
       if (baiduBean == null){
           throw new RuntimeException("constants is empty");
       }
       return baiduBean;
   }

}
