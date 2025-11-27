package com.example.spring3.mapper;

import com.example.spring3.dto.request.payment.PaymentCreateRequest;
import com.example.spring3.dto.response.payment.PaymentResponse;
import com.example.spring3.entity.Payment;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toPayment(PaymentCreateRequest request);

    PaymentResponse toPaymentResponse(Payment payment);
}