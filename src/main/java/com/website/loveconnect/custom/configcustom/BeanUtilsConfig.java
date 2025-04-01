package com.website.loveconnect.custom.configcustom;

import com.website.loveconnect.custom.annotationcustom.EnumeratedCustom;
import com.website.loveconnect.custom.convertercustom.EnumConverter;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.Field;

public class BeanUtilsConfig {
    public static void registerEnumConverter(Class<?> tClass) {
        for(Field field : tClass.getDeclaredFields()) {
            field.setAccessible(true);
            if(field.isAnnotationPresent(EnumeratedCustom.class)) {
                Class<?> type = field.getType();
                if(type.isEnum()){
                    Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) type;
                    ConvertUtils.register(new EnumConverter(enumClass), enumClass);
                }
            }
        }

    }
}