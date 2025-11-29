package com.example.spring3.dto.request.bill;

import com.example.spring3.entity.Payment;
import com.example.spring3.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillCreateRequest {
    String billId;
    String paymentId;
    String userIdId;
    long totalAmount;
}
