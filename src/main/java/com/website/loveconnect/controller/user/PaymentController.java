package com.website.loveconnect.controller.user;

import com.website.loveconnect.config.VnpayConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.website.loveconnect.dto.response.PaymentResponse;
import com.website.loveconnect.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api")
@RestController
@RequestMapping("${spring.application.api-prefix}/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final VnpayConfig vnpayConfig;

    private final PaymentService paymentService;

    @GetMapping(value = "/vn-pay")
    public ResponseEntity<PaymentResponse> pay(HttpServletRequest request) {
        PaymentResponse response = paymentService.createVnPayPayment(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/vn-pay-callback")
    public ResponseEntity<PaymentResponse> payCallback(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if ("00".equals(status)) {
            return new ResponseEntity<>(PaymentResponse.builder()
                    .code("00")
                    .message("Success")
                    .paymentUrl("")
                    .build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(PaymentResponse.builder()
                    .code(status)
                    .message("Failed")
                    .paymentUrl("")
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }



//
//    @GetMapping("/payment")
//    public ResponseEntity<String> createPayment(
//            @RequestParam(defaultValue = "1000000") long amount,
//            @RequestParam(required = false) String bankCode,
//            @RequestParam(required = false) String language,
//            @RequestParam String ipAddress) {
//
//        try {
//            Map<String, String> vnpParams = new HashMap<>();
//
//            // Static config values
//            vnpParams.put("vnp_Version", VnpayConfig.vnp_Version);
//            vnpParams.put("vnp_Command", VnpayConfig.vnp_Command);
//            vnpParams.put("vnp_TmnCode", vnpayConfig.getVnpTmnCode());
//            vnpParams.put("vnp_CurrCode", "VND");
//            vnpParams.put("vnp_OrderType", VnpayConfig.orderType);
//
//            // Dynamic values
//            vnpParams.put("vnp_Amount", String.valueOf(amount * 100));
//            String vnpTxnRef = VnpayConfig.getRandomNumber(8);
//            vnpParams.put("vnp_TxnRef", vnpTxnRef);
//            vnpParams.put("vnp_OrderInfo", "Thanh toan don hang:" + vnpTxnRef);
//            vnpParams.put("vnp_Locale", language != null && !language.isEmpty() ? language : "vn");
//            vnpParams.put("vnp_ReturnUrl", vnpayConfig.getVnpReturnUrl());
//            vnpParams.put("vnp_IpAddr", ipAddress);
//
//            if (bankCode != null && !bankCode.isEmpty()) {
//                vnpParams.put("vnp_BankCode", bankCode);
//            }
//
//            // Date handling
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//            String vnpCreateDate = formatter.format(cld.getTime());
//            vnpParams.put("vnp_CreateDate", vnpCreateDate);
//            cld.add(Calendar.MINUTE, 15);
//            String vnpExpireDate = formatter.format(cld.getTime());
//            vnpParams.put("vnp_ExpireDate", vnpExpireDate);
//
//            // Build query string for URL (with URL encoding)
//            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
//            Collections.sort(fieldNames);
//            StringBuilder query = new StringBuilder();
//            for (String fieldName : fieldNames) {
//                String fieldValue = vnpParams.get(fieldName);
//                if (fieldValue != null && !fieldValue.isEmpty()) {
//                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
//                            .append('=')
//                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                    if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
//                        query.append('&');
//                    }
//                }
//            }
//
//            // Build hash data for vnp_SecureHash (without URL encoding, join by |)
//            String hashData = String.join("|",
//                    vnpParams.get("vnp_Amount"),
//                    vnpParams.get("vnp_BankCode") != null ? vnpParams.get("vnp_BankCode") : "",
//                    vnpParams.get("vnp_CreateDate"),
//                    vnpParams.get("vnp_CurrCode"),
//                    vnpParams.get("vnp_ExpireDate"),
//                    vnpParams.get("vnp_IpAddr"),
//                    vnpParams.get("vnp_Locale"),
//                    vnpParams.get("vnp_OrderInfo"),
//                    vnpParams.get("vnp_OrderType"),
//                    vnpParams.get("vnp_ReturnUrl"),
//                    vnpParams.get("vnp_TmnCode"),
//                    vnpParams.get("vnp_TxnRef"),
//                    vnpParams.get("vnp_Version"),
//                    vnpParams.get("vnp_Command")
//            );
//
//            // Log để debug
//            System.out.println("hashData: " + hashData);
//
//            String vnpSecureHash = VnpayConfig.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
//            System.out.println("vnp_SecureHash: " + vnpSecureHash);
//
//            String queryUrl = query + "&vnp_SecureHash=" + vnpSecureHash;
//            String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
//
//            // Log URL cuối cùng
//            System.out.println("Payment URL: " + paymentUrl);
//
//            // Prepare response
//            JsonObject response = new JsonObject();
//            response.addProperty("code", "00");
//            response.addProperty("message", "success");
//            response.addProperty("data", paymentUrl);
//
//            return ResponseEntity.ok(new Gson().toJson(response));
//
//        } catch (Exception e) {
//            JsonObject errorResponse = new JsonObject();
//            errorResponse.addProperty("code", "99");
//            errorResponse.addProperty("message", "Error: " + e.getMessage());
//            return ResponseEntity.status(500).body(new Gson().toJson(errorResponse));
//        }
//    }
}