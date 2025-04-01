package com.website.loveconnect.custom.repositorycustom.interfacejdbc;

import java.util.List;

//repository custom
public interface JdbcRepository<T,ID> {
    List<T> findAllCustom();
    T findByIdCustom(ID id);
    void saveCustom(T tClass);
    void saveCustomVer2(T tClass);
    void deleteCustom(T tClass);
    void deleteByIdCustom(ID id);
}