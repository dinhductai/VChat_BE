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
//
//    public static final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
//    public static final String vnp_Version = "2.1.0";
//    public static final String vnp_Command = "pay";
//    public static final String orderType = "other";
//
//    @Value("${vnpay.tmn-code}")
//    private String vnpTmnCode;
//
//    @Value("${vnpay.secret-key}")
//    private String secretKey;
//
//    @Value("${vnpay.return-url}")
//    private String vnpReturnUrl;
//
//    public static String hmacSHA512(final String key, final String data) {
//        try {
//            final Mac hmac512 = Mac.getInstance("HmacSHA512");
//            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
//            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
//            hmac512.init(secretKey);
//            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
//            byte[] result = hmac512.doFinal(dataBytes);
//            StringBuilder sb = new StringBuilder(2 * result.length);
//            for (byte b : result) {
//                sb.append(String.format("%02x", b & 0xff));
//            }
//            return sb.toString();
//        } catch (Exception ex) {
//            return "";
//        }
//    }
//
//    public static String getRandomNumber(int len) {
//        Random rnd = new Random();
//        String chars = "0123456789";
//        StringBuilder sb = new StringBuilder(len);
//        for (int i = 0; i < len; i++) {
//            sb.append(chars.charAt(rnd.nextInt(chars.length())));
//        }
//        return sb.toString();
//    }

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