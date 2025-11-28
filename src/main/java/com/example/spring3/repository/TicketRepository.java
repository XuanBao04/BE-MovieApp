package com.example.spring3.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spring3.entity.Ticket;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    @Query("""
            SELECT t
            FROM Ticket t
            WHERE t.status = 1
            """)
    Page<Ticket> getTicketsBooked(Pageable pageable);

  @Query("""
      SELECT t
      FROM Ticket t
      WHERE t.showtime.showtimeId = :showtimeId
        AND t.status = 1
      """)
  List<Ticket> getBookedSeats(@Param("showtimeId") String showtimeId);

  List<Ticket> findByVnpTxnRef(String vnpTxnRef);
}
