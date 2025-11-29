package com.example.spring3.repository;

import com.example.spring3.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReveuneRepository extends JpaRepository<Bill, String> {

    @Query(value = """
            SELECT SUM(p.amount)
            FROM bills b 
            JOIN payments p ON b.payment_id = p.payment_id
            WHERE p.payment_time >= :startDate 
              AND p.payment_time <= :endDate
            """, nativeQuery = true)
    Double calculateRevenue(@Param("startDate") LocalDateTime startDate,
                            @Param("endDate") LocalDateTime endDate);

}

