package com.example.spring3.service;

import com.example.spring3.repository.ReveuneRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;     // Dùng LocalDate cho tham số
import java.time.LocalDateTime; // Dùng LocalDateTime để tính toán
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReveuneService {

    ReveuneRepository reveuneRepository;

    public Double getRevenueByPeriod(LocalDate fromDate, LocalDate toDate) {

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu không được lớn hơn ngày kết thúc!");
        }

        // Ngày bắt đầu -> 00:00:00
        LocalDateTime startDateTime = fromDate.atStartOfDay();

        // Ngày kết thúc -> 23:59:59.999999999 (Lấy trọn vẹn ngày cuối)
        LocalDateTime endDateTime = toDate.atTime(LocalTime.MAX);

        Double totalRevenue = reveuneRepository.calculateRevenue(startDateTime, endDateTime);

        return totalRevenue != null ? totalRevenue : 0.0;
    }
}