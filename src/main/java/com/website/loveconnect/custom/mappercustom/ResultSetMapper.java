package com.website.loveconnect.custom.mappercustom;

import com.website.loveconnect.custom.annotationcustom.ColumnCustom;
import com.website.loveconnect.custom.annotationcustom.EntityCustom;
import com.website.loveconnect.custom.configcustom.BeanUtilsConfig;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetMapper<T> {

    public List<T> mapRowList(ResultSet rs, Class<T> tClass) throws SQLException {
        BeanUtilsConfig.registerEnumConverter(tClass);
        List<T> result = new ArrayList<>();
        try{
            if(tClass.isAnnotationPresent(EntityCustom.class)){
                //lấy tên các cột mà rs trả về
                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                Field[] fields = tClass.getDeclaredFields();
                while (rs.next()) {
                    T obj = tClass.newInstance();
                    for(int i=1 ; i<=resultSetMetaData.getColumnCount(); i++){
                        String columnName = resultSetMetaData.getColumnName(i);
                        Object columnValue = rs.getObject(i);
                        for(Field field : fields){
                            field.setAccessible(true);
                            if(field.isAnnotationPresent(ColumnCustom.class)) {
                                ColumnCustom columnCustom = field.getAnnotation(ColumnCustom.class);
                                if(columnCustom.name().equals(columnName)){
                                    BeanUtils.setProperty(obj, field.getName(), columnValue);
                                    break;
                                }
                            }

                        }
                    }
                    result.add(obj);
                }
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public T mapRowOne(ResultSet rs,Class<T> tClass) throws SQLException {
        BeanUtilsConfig.registerEnumConverter(tClass);
        try {
            T result = tClass.newInstance();
            if (tClass.isAnnotationPresent(EntityCustom.class)) {
                Field[] fields = tClass.getDeclaredFields();
                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                while (rs.next()) {
                    for(int i=1 ; i<=resultSetMetaData.getColumnCount(); i++){
                        String columnName = resultSetMetaData.getColumnName(i);
                        Object columnValue = rs.getObject(i);
                        for(Field field : fields) {
                            field.setAccessible(true);
                            if (field.isAnnotationPresent(ColumnCustom.class)) {
                                ColumnCustom columnCustom = field.getAnnotation(ColumnCustom.class);
                                if (columnCustom.name().equals(columnName)) {
                                    BeanUtils.setProperty(result, field.getName(), columnValue);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}