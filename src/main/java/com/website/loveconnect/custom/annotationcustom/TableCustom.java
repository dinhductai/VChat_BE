package com.website.loveconnect.custom.annotationcustom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//set phạm vi tồn tại
@Retention(RetentionPolicy.RUNTIME)
//set các loại phần tử mà anno có thể áp dụng
@Target({ElementType.TYPE})
public @interface TableCustom {
    String name() default "";
}