package com.example.spring3.controller;

import com.example.spring3.dto.response.BookingResultResponse;
import com.example.spring3.service.BookingService;

import javax.management.relation.RelationNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    BookingService bookingService;

    @GetMapping("/result")
    public ResponseEntity<BookingResultResponse> getBookingResult(
            @RequestParam("txnRef") String txnRef,
            @RequestParam("paymentId") String paymentId) throws RelationNotFoundException {

        try {
            // Gọi Service để thực hiện logic truy vấn và ánh xạ
            BookingResultResponse response = bookingService.getBookingResult(txnRef, paymentId);

            // Trả về kết quả 200 OK
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("Error retrieving booking result: {}", e.getMessage());
            // Xử lý các lỗi khác (như đơn hàng chưa hoàn tất) bằng 400 Bad Request hoặc 500
            // Internal Server Error
            return ResponseEntity.badRequest().build();
        }
    }

}
