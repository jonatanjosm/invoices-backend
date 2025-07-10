package com.example.invoicesbackend.mapper;

import com.example.invoicesbackend.dto.request.LineItemRequestDto;
import com.example.invoicesbackend.dto.response.LineItemResponseDto;
import com.example.invoicesbackend.model.LineItem;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LineItemMapper {

    LineItemMapper INSTANCE = Mappers.getMapper(LineItemMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "totalAmount", ignore = true) // totalAmount will be calculated by the entity's calculateTotalAmount method
    LineItem toEntity(LineItemRequestDto requestDto);

    LineItemRequestDto toDto(LineItem lineItem);

    List<LineItemResponseDto> toDtoList(List<LineItem> lineItems);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "totalAmount", ignore = true) // totalAmount will be recalculated by the entity's calculateTotalAmount method
    void updateEntityFromDto(LineItemRequestDto requestDto, @MappingTarget LineItem lineItem);
}
