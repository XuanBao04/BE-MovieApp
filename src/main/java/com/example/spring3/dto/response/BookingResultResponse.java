package com.example.spring3.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResultResponse {
    // --- 0. Thông tin khách hàng ---
    String name;
    String email;
    String phone;

    // --- 1. Thông tin Đơn hàng/Tham chiếu ---

    /** Mã giao dịch/đơn hàng (vnp_TxnRef) để tham chiếu */
    String orderReferenceId;

    // --- 2. Thông tin Thanh toán (Payment) ---

    /** ID của bản ghi Payment */
    String paymentId;
    /** Số tiền thanh toán */
    long amount;
    /** Phương thức thanh toán (VNPay) */
    String paymentMethod;
    /** Thời điểm giao dịch thành công */
    LocalDateTime paymentTime;
    /** Trạng thái thanh toán (1 = Thành công) */
    Integer paymentStatus;

    // --- 3. Thông tin Phim & Suất chiếu (Showtime) ---

    /** Tên Phim */
    String movieTitle;
    /** Thời gian bắt đầu chiếu */
    LocalDateTime startTime;

    // --- 4. Thông tin Địa điểm (Theater & Room) ---

    /** Tên Rạp chiếu */
    String theaterName;
    /** Địa chỉ Rạp chiếu */
    String theaterAddress;
    /** Tên Phòng chiếu */
    String roomName;

    // --- 5. Danh sách Vé đã mua ---

    /** Danh sách chi tiết các vé đã mua */
    List<TicketDetailResponse> tickets;

    // (Bạn cần thêm getters/setters/constructors/builders nếu không dùng Lombok)
}
