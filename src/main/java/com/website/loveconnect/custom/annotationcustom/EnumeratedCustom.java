package com.website.loveconnect.custom.annotationcustom;
import jakarta.persistence.EnumType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface EnumeratedCustom {
    EnumType type() default EnumType.STRING;

}