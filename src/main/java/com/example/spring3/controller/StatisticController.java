package com.example.spring3.controller;

import com.example.spring3.dto.response.ApiResponse;
import com.example.spring3.service.ReveuneService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate; // CHÚ Ý: Dùng LocalDate

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticController {

    ReveuneService reveuneService;

    @GetMapping("/revenue")
    public ApiResponse<Double> getRevenue(
            // 1. Sửa LocalDateTime thành LocalDate
            // 2. Thêm @DateTimeFormat để Spring hiểu chuỗi "yyyy-MM-dd"
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {

        Double revenue = reveuneService.getRevenueByPeriod(fromDate, toDate);

        return ApiResponse.<Double>builder()
                .result(revenue)
                .message("Lấy doanh thu thành công")
                .build();
    }
}