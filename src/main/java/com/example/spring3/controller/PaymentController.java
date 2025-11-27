package com.example.spring3.controller;

import com.example.spring3.dto.request.ApiResponse;
import com.example.spring3.dto.request.BookingRequest;
import com.example.spring3.dto.request.payment.PaymentCreateRequest;
import com.example.spring3.dto.response.payment.VNPayResponse;
import com.example.spring3.service.BookingService;
import com.example.spring3.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    BookingService bookingService;

    @PostMapping("/create-payment")
    public ApiResponse<VNPayResponse> createPayment(@RequestBody BookingRequest bookingRequest,
            HttpServletRequest request) throws UnsupportedEncodingException {

        try {
            // Gọi Service để xử lý logic, tạo đơn hàng tạm thời và sinh URL
            VNPayResponse response = bookingService.createBookingAndVnPayUrl(bookingRequest, request);

            return ApiResponse.<VNPayResponse>builder().message("Tạo thanh toán thành công").result(response).build();

        } catch (Exception e) {
            // Log lỗi
            return ApiResponse.<VNPayResponse>builder().message("Lỗi tạo toán " + e).result(null).build();
        }
    }

    @GetMapping("")
    public void processVNPayReturn(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws IOException {

        // Gọi Service để xử lý callback (xác thực Hash, kiểm tra mã lỗi, cập nhật DB)
        String clientRedirectUrl = bookingService.handleVnPayCallback(params);

        // Chuyển hướng người dùng về Client (React)
        response.sendRedirect(clientRedirectUrl);
    }

}
