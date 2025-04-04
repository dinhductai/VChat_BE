package com.website.loveconnect.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@Configuration
@Getter
public class VnpayConfig {

    @Getter
    @Value("${payment.vnpay.url}")
    private String vnpPayUrl;

    @Value("${payment.vnpay.returnUrl}")
    private String vnpReturnUrl;

    @Value("${payment.vnpay.tmnCode}")
    private String vnpTmnCode;

    @Getter
    @Value("${payment.vnpay.secretKey}")
    private String secretKey;

    @Value("${payment.vnpay.version}")
    private String vnpVersion;

    @Value("${payment.vnpay.command}")
    private String vnpCommand;

    @Value("${payment.vnpay.orderType}")
    private String orderType;

    //tạo các tham số cơ bản cho yêu cầu thanh toán
    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", vnpVersion);
        vnpParamsMap.put("vnp_Command", vnpCommand);
        vnpParamsMap.put("vnp_TmnCode", vnpTmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_TxnRef", getRandomNumber(8)); // Mã giao dịch ngẫu nhiên
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang: " + getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderType", orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", vnpReturnUrl);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);

        calendar.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnpExpireDate);

        return vnpParamsMap;
    }

    // Hàm tạo số ngẫu nhiên (tái sử dụng từ VNPayUtil)
    private String getRandomNumber(int len) {
        java.util.Random rnd = new java.util.Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}