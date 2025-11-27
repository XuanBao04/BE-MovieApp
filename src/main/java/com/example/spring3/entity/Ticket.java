package com.example.spring3.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_id")
    String ticketId;

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    Seat seat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    Payment payment;

    String type;

    @Column(name = "booking_time")
    LocalDateTime bookingTime;

    @Column(name = "expire_time")
    LocalDateTime expireTime;

    // Trạng thái vé: 0=Pending/Locked, 1=Completed/Sold
    Integer status;

    // --- Các trường bổ sung cho VNPay & Quản lý Đơn hàng ---

    // Mã giao dịch VNPay (đại diện cho nhóm vé/đơn hàng)
    @Column(name = "vnp_txn_ref")
    String vnpTxnRef;

    // Trạng thái đơn hàng: PENDING, COMPLETED, FAILED
    @Column(name = "order_status")
    String orderStatus;

    // Thông tin khách hàng (Dùng khi khách không đăng nhập)
    @Column(name = "customer_name")
    String customerName;

    @Column(name = "customer_email")
    String customerEmail;

    @Column(name = "customer_phone")
    String customerPhone;

    // --------------------------------------------------------
}