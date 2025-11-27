package com.example.spring3.dto.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    /**
     * ID của người dùng đang thực hiện đặt vé (hoặc Guest ID nếu cho phép đặt ẩn
     * danh)
     */
    private String userId;

    /**
     * ID của suất chiếu mà người dùng đang chọn vé
     */
    private String showtimeId;

    /**
     * Danh sách các ID của ghế mà người dùng muốn đặt (cần phải là AVAILABLE)
     */
    private List<String> seatIds;

    // Có thể bổ sung thêm các trường khác nếu cần (ví dụ: loaiVe/ticketType,
    // couponCode, v.v.)

    private long amount;

    private String name;
    private String email;
    private String phone;
}
