package com.example.spring3.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.spring3.dto.response.SeatResponse;
@Mapper(componentModel = "spring")
public interface SeatMapper {

    SeatResponse toSeatResponse(SeatResponse dto);

    List<SeatResponse> toSeatResponseList(List<SeatResponse> dtoList);
}

