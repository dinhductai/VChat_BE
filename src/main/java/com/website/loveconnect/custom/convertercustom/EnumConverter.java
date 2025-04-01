package com.website.loveconnect.custom.convertercustom;


import org.apache.commons.beanutils.converters.AbstractConverter;

import java.util.Locale;

public class EnumConverter extends AbstractConverter {
    private final Class<? extends Enum> enumType;

    public EnumConverter(Class<? extends Enum> enumType) {
        this.enumType = enumType;
    }

    @Override
    protected String convertToString(Object value) throws Throwable {
        return ((Enum) value).name();
    }

    @Override
    protected Object convertToType(Class type, Object value) throws Throwable {
        if (value == null) {
            return null;
        }
        return Enum.valueOf(enumType, value.toString().toLowerCase());
    }

    @Override
    protected Class<?> getDefaultType() {
        return this.enumType;
    }

}