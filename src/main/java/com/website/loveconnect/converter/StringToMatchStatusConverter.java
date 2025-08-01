package com.website.loveconnect.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.website.loveconnect.enumpackage.MatchStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToMatchStatusConverter implements Converter<String, MatchStatus> {
    @Override
    public MatchStatus convert(String stringStatus) {
        try{
            return  MatchStatus.valueOf(stringStatus.toUpperCase());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
