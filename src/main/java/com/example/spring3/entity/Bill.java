package com.example.spring3.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity @Table(name="bills")
@Data @NoArgsConstructor @AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bill_id")
    private String billId;

    @ManyToOne
    @JoinColumn(name="payment_id")
    private Payment payment;


    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "total_amount")
    private Double totalAmount;
}
