package com.website.loveconnect.util;

import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//lớp này dùng để lấy các phần tử trong map (được lấy từ giao diện) và chuyển đổi kiểu dữ liệu
public class MapUtil {
    //xử lý đơn và đa giá trị
    public static <T> T getObject(MultiValueMap<String, Object> params, String key, Class<T> tClass) {
        List<Object> values = params.get(key);  // Lấy danh sách giá trị theo key
        if (values == null || values.isEmpty()) {
            return null;
        }

        if (tClass.equals(List.class)) {
            return tClass.cast(values);  // Trả về danh sách nếu kiểu là List
        }

        Object value = values.get(0);  // Lấy giá trị đầu tiên nếu không phải danh sách

        if (value != null) {
            if (tClass.equals(Long.class)) {
                return tClass.cast(Long.valueOf(value.toString()));
            } else if (tClass.equals(Integer.class)) {
                return tClass.cast(Integer.valueOf(value.toString()));
            } else if (tClass.equals(Double.class)) {
                return tClass.cast(Double.valueOf(value.toString()));
            } else if (tClass.equals(Date.class)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return tClass.cast(sdf.parse(value.toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (tClass.equals(BigDecimal.class)) {
                return tClass.cast(new BigDecimal(value.toString()));
            } else if (tClass.equals(String.class)) {
                return tClass.cast(value.toString());
            }
        }
        return null;
    }
}