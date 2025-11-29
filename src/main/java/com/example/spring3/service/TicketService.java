package com.example.spring3.service;

import com.example.spring3.dto.response.TicketDetailResponse;
import com.example.spring3.dto.response.TicketResponse;
import com.example.spring3.entity.Ticket;
import com.example.spring3.mapper.TicketMapper;
import com.example.spring3.repository.TicketRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    TicketRepository ticketRepository;
    TicketMapper ticketMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public Page<TicketResponse> getTicketsBooked(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Ticket> ticketPage = ticketRepository.getTicketsBooked(pageable);

        return ticketPage.map(ticketMapper::toTicketResponse);
    }

    public List<TicketDetailResponse> getAllTicketsByPaymentId(String paymentId) {
        List<Ticket> tickets = ticketRepository.findByPayment_PaymentId(paymentId);
        List<TicketDetailResponse> list = new ArrayList<>();
        for (var t : tickets) {
            TicketDetailResponse ticketDetailResponse =
                    TicketDetailResponse.builder()
                            .ticketId(t.getTicketId())
                            .seatName(t.getSeat().getSeatRow() + t.getSeat().getSeatNumber())
                    .build();
            list.add(ticketDetailResponse);
        }
        
        return list;
    }
}