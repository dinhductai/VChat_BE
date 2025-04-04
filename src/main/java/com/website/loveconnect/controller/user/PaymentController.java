package com.website.loveconnect.controller.user;

import com.website.loveconnect.config.VnpayConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.website.loveconnect.dto.response.ApiResponse;
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

@RestController
@RequestMapping("/api/v1/payment") //  /api/v1/payment , chưa sửa
@RequiredArgsConstructor
public class PaymentController {

    private final VnpayConfig vnpayConfig;

    private final PaymentService paymentService;

//    test với url này,chỉ amount có thể thay đổi
//    http://localhost:8080/api/v1/payment/vn-pay?amount=10000&bankCode=NCB

    @GetMapping(value = "/vn-pay")
    //không dùng requestparam vì cần lấy cả IP address người dùng
//    VNPay yêu cầu tham số vnp_IpAddr trong yêu cầu thanh toán để ghi nhận địa chỉ IP của client,
//    nhằm mục đích bảo mật và theo dõi giao dịch
    public ResponseEntity<ApiResponse<PaymentResponse>> createPay(HttpServletRequest request) {
        PaymentResponse response = paymentService.createVnPayPayment(request);
        return ResponseEntity.ok(new ApiResponse<>(true,"Create payment successful",response));
    }

    @GetMapping(value = "/vn-pay-callback")
    public ResponseEntity<ApiResponse<PaymentResponse>>  payCallback(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if ("00".equals(status)) {
            return ResponseEntity.ok(new ApiResponse<>(true,"Callback successful",
                    PaymentResponse.builder()
                    .code("00")
                    .message("Success")
                    .paymentUrl("")
                    .build()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(true,"Callback successful",
                    PaymentResponse.builder()
                            .code(status)
                            .message("Failed")
                            .paymentUrl("")
                            .build()));

        }
    }


}