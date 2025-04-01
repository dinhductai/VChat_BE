package com.website.loveconnect.custom.annotationcustom;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD , ElementType.FIELD, ElementType.CONSTRUCTOR})
@JacksonAnnotationCustom
public @interface JsonIgnoreCustom {
    boolean value() default true;
}