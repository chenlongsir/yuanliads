package com.yuanli.menta.utils;


import com.yuanli.menta.constants.MentaBean;

public class InitUtils {
   private static MentaBean mentaBean;
   
   public static void init(MentaBean mentaBean){
       InitUtils.mentaBean = mentaBean;
   }

   public static MentaBean getBean(){
       if (mentaBean == null){
           throw new RuntimeException("constants is empty");
       }
       return mentaBean;
   }

}
