package com.yuanli.ad;

import static com.yuanli.ad.AdTypeConstants.AD_TYPE_BAIDU;
import static com.yuanli.ad.AdTypeConstants.AD_TYPE_KS;
import static com.yuanli.ad.AdTypeConstants.AD_TYPE_MEN_TA;
import static com.yuanli.ad.AdTypeConstants.AD_TYPE_PANGOLIN;
import static com.yuanli.ad.AdTypeConstants.AD_TYPE_YLH;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
@IntDef(value = {AD_TYPE_PANGOLIN,AD_TYPE_BAIDU,AD_TYPE_KS,AD_TYPE_YLH,AD_TYPE_MEN_TA})
public @interface AdType {

}
