package com.example.spring3.controller;

import com.example.spring3.dto.response.ApiResponse;
import com.example.spring3.dto.response.TicketResponse;
import com.example.spring3.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; // Nhớ import thư viện này
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public ApiResponse<Page<TicketResponse>> getTicketBooked(
            @RequestParam(defaultValue = "0") int page, // Mặc định trang đầu tiên (số 0)
            @RequestParam(defaultValue = "3") int size  // Mặc định phần tử/trang theo ý bạn
    )
    {
        return ApiResponse.<Page<TicketResponse>>builder()
                .result(ticketService.getTicketsBooked(page, size))
                .build();
    }
}