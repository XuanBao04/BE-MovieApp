package com.example.spring3.mapper;
import com.example.spring3.dto.response.TicketResponse;
import com.example.spring3.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketResponse toTicketResponse(Ticket ticket);

}

