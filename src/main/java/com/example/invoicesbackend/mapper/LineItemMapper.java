package com.example.invoicesbackend.mapper;

import com.example.invoicesbackend.dto.request.LineItemRequestDto;
import com.example.invoicesbackend.dto.response.LineItemResponseDto;
import com.example.invoicesbackend.model.LineItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LineItemMapper {

    public LineItem toEntity(LineItemRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        LineItem lineItem = new LineItem();
        lineItem.setDescription(requestDto.getDescription());
        lineItem.setPrice(requestDto.getPrice());
        lineItem.setQuantity(requestDto.getQuantity());
        // totalAmount will be calculated by the entity's calculateTotalAmount method

        return lineItem;
    }

    public LineItemResponseDto toDto(LineItem lineItem) {
        if (lineItem == null) {
            return null;
        }

        LineItemResponseDto responseDto = new LineItemResponseDto();
        responseDto.setId(lineItem.getId());
        responseDto.setDescription(lineItem.getDescription());
        responseDto.setPrice(lineItem.getPrice());
        responseDto.setQuantity(lineItem.getQuantity());
        responseDto.setTotalAmount(lineItem.getTotalAmount());

        return responseDto;
    }

    public List<LineItemResponseDto> toDtoList(List<LineItem> lineItems) {
        return lineItems.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void updateEntityFromDto(LineItemRequestDto requestDto, LineItem lineItem) {
        if (requestDto == null) {
            return;
        }

        lineItem.setDescription(requestDto.getDescription());
        lineItem.setPrice(requestDto.getPrice());
        lineItem.setQuantity(requestDto.getQuantity());
        // totalAmount will be recalculated by the entity's calculateTotalAmount method
    }
}