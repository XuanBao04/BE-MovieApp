package com.example.spring3.repository;

import com.example.spring3.entity.Bill;
import com.example.spring3.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, String> {
    List<Bill> findByBillId(String billId);

    List<Bill> findByUser_Id (String userId);

    List<Bill> findByPayment_PaymentId (String paymentId);
}
