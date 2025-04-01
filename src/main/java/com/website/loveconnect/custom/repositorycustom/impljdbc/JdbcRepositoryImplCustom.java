package com.website.loveconnect.custom.repositorycustom.impljdbc;

import com.website.loveconnect.custom.annotationcustom.ColumnCustom;
import com.website.loveconnect.custom.annotationcustom.IdCustom;
import com.website.loveconnect.custom.annotationcustom.OneToManyCustom;
import com.website.loveconnect.custom.annotationcustom.TableCustom;
import com.website.loveconnect.custom.mappercustom.ResultSetMapper;
import com.website.loveconnect.custom.repositorycustom.interfacejdbc.JdbcRepository;
import com.website.loveconnect.custom.utilcustom.ConnectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcRepositoryImplCustom<T, ID> implements JdbcRepository<T, ID> {
    @Override
    public List<T> findAllCustom() {
        Class<T> tClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        List<T> result = new ArrayList<T>();
        String tableName="";
        //kiểm tra xem entity có đc đánh dấu @table không
        if(tClass.isAnnotationPresent(TableCustom.class)) {
            //lấy tên bảng và set vào biến
            TableCustom tableCustom = tClass.getAnnotation(TableCustom.class);
            tableName = tableCustom.name();
        }
        String sql = "select * from " + tableName;
        ResultSetMapper<T> resultSetMapper = new ResultSetMapper<T>();
        try(Connection conn = ConnectionUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            result = resultSetMapper.mapRowList(rs,tClass);
            if(result != null){
                return result;
            }else return new ArrayList<>();
        }catch (Exception ex){
            System.out.println("Connect database error");
            return null;
        }
    }

    @Override
    public T findByIdCustom(ID id) {
        Class<T> tClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String tableName="";
        T result ;
        if(tClass.isAnnotationPresent(TableCustom.class)) {
            TableCustom tableCustom = tClass.getAnnotation(TableCustom.class);
            tableName = tableCustom.name();
        }
        String sql = "select * from " + tableName + " where " + getPrimaryKeyColumn(tClass) +"=" +id;
        ResultSetMapper<T> resultSetMapper = new ResultSetMapper<T>();
        try(Connection conn = ConnectionUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            result = resultSetMapper.mapRowOne(rs,tClass);
            if(result != null){
                return result;
            }else return null;
        }catch (Exception ex){
            System.out.println("Connect database error");
            return null;
        }

    }

    //hàm save entity,chưa có khả năng update dựa vào id
    @Override
    public void saveCustom(T tClass) {
        try(Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(createSqlInsert())){
            Field[] fields = tClass.getClass().getDeclaredFields();
            int index = 1;
            for(Field field : fields){
                field.setAccessible(true);
                if(!field.isAnnotationPresent(ColumnCustom.class)){
                    continue;
                }
                try {
                    Object value = field.get(tClass);
                    // Kiểm tra nếu là kiểu enum
                    if (field.getType().isEnum()) {
                        value = value != null ? value.toString() : null; // Chuyển enum thành chuỗi
                        ps.setObject(index, value);
                        index++;
                        continue;
                    }
                    ps.setObject(index, field.get(tClass));
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("PreparedStatement has error");
                }
                index++;
            }
            ps.executeUpdate();
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Connect database error");
        }
    }

    @Override
    public void saveCustomVer2(T tClass)  {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Object idEntity=null;
        String tableName=null;
        if (entityClass.isAnnotationPresent(TableCustom.class)) {
            tableName = entityClass.getAnnotation(TableCustom.class).name();
        } else {
            System.out.println("Entity class need add @TableCustom");
        }
        boolean isUpdate = false;
        try{
            for(Field field : tClass.getClass().getDeclaredFields()){
                field.setAccessible(true);
                if(field.isAnnotationPresent(IdCustom.class)){
                    idEntity = field.get(tClass);
                    isUpdate = true;
                    break;
                }
            }

            if(isUpdate && idEntity != null){
                updateEntity(tClass,entityClass,idEntity,tableName);
            }
            else {
                saveCustom(tClass);
            }
        }
        catch (Exception ex){
            System.out.println("Connect database error");
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteCustom(T tClass) {
        Class<T> entityClass =  (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String tableName="";
        if (entityClass.isAnnotationPresent(TableCustom.class)) {
            tableName = entityClass.getAnnotation(TableCustom.class).name();
        }

    }

    @Override
    public void deleteByIdCustom(ID id) {

    }

    public void updateEntity(T tClass, Class<T> entityClass, Object idEntity, String tableName){
        String sql = "select * from " + tableName + " where " + getPrimaryKeyColumn(entityClass) +"=" +idEntity;
        ResultSetMapper<T> resultSetMapper = new ResultSetMapper<T>();
        T result=null;
        try(Connection conn = ConnectionUtil.getConnection();
            //tìm kiếm entity với id đc truyền vào
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            result = resultSetMapper.mapRowOne(rs,entityClass);
            if(result != null){
                //cập nhật với thông tin mới
                try (PreparedStatement ps = conn.prepareStatement(createSqlUpdate(tClass, entityClass, idEntity, tableName))) {
                    Field[] fields = tClass.getClass().getDeclaredFields();
                    int index = 1;
                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (field.isAnnotationPresent(ColumnCustom.class) && !field.isAnnotationPresent(IdCustom.class)) {
                            Object value = field.get(tClass);
                            if (field.getType().isEnum() && value != null) {
                                value = value.toString();
                            }
                            ps.setObject(index++, value);
                        }
                    }
// Gán giá trị cho WHERE (khóa chính)
                    ps.setObject(index, idEntity);
                    ps.executeUpdate();
                }

                catch (Exception ex){
                    ex.printStackTrace();
                    System.out.println("Connect database error");
                }
            }
            else{
                System.out.println("not fould entity with id");
            }
        }catch (Exception ex){
            System.out.println("Connect database error");
        }
    }

    private String createSqlUpdate(T tClass, Class<T> entityClass, Object idEntity, String tableName) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE " + tableName + " SET ");
        Field[] fields = tClass.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ColumnCustom.class) && !field.isAnnotationPresent(IdCustom.class)) {
                sqlBuilder.append(field.getAnnotation(ColumnCustom.class).name()).append(" = ?, ");
            }
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 2); // Xóa dấu phẩy cuối cùng
        sqlBuilder.append("WHERE ").append(getPrimaryKeyColumn(entityClass)).append(" = ?");
        return sqlBuilder.toString();
    }

    //hàm tạo code sql insert bảng
    private String createSqlInsert(){
        Class<T> tClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String tableName = "";
        if(tClass.isAnnotationPresent(TableCustom.class)){
            TableCustom tableCustom = tClass.getAnnotation(TableCustom.class);
            tableName = tableCustom.name();
        }
        //tạo các dấu , ? để map dl
        StringBuilder fields = new StringBuilder("");
        StringBuilder params = new StringBuilder("");
        for(Field field : tClass.getDeclaredFields()){
            if(fields.length() > 0 && !field.isAnnotationPresent(OneToManyCustom.class)){
                fields.append(",");
                params.append(",");
            }
            if(field.isAnnotationPresent(ColumnCustom.class)){
                ColumnCustom columnCustom = field.getAnnotation(ColumnCustom.class);
                fields.append(columnCustom.name());
                params.append("?");
            }
        }
        String sql = " Insert into "+ tableName +" ( "+fields+" ) values ( "+params+" ) ";
        return sql;
    }

    //hàm lấy tên cột id của các entity
    private String getPrimaryKeyColumn(Class<T> tClass){
        try {
            for(Field field : tClass.getDeclaredFields()){
                field.setAccessible(true);
                if(field.isAnnotationPresent(IdCustom.class)){
                    return field.getName();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}