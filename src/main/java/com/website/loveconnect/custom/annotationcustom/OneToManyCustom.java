package com.website.loveconnect.custom.annotationcustom;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface OneToManyCustom {
    String mappedBy() default "";
    FetchType fetch() default FetchType.LAZY;
    CascadeType[] cascade() default {};
    boolean orphanRemoval() default false;
}