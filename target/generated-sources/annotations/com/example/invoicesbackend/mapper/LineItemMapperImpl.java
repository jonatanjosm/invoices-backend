package com.example.invoicesbackend.mapper;

import com.example.invoicesbackend.dto.request.LineItemRequestDto;
import com.example.invoicesbackend.dto.response.LineItemResponseDto;
import com.example.invoicesbackend.model.LineItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-10T10:02:21-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Homebrew)"
)
@Component
public class LineItemMapperImpl implements LineItemMapper {

    @Override
    public LineItem toEntity(LineItemRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        LineItem lineItem = new LineItem();

        lineItem.setDescription( requestDto.getDescription() );
        lineItem.setPrice( requestDto.getPrice() );
        lineItem.setQuantity( requestDto.getQuantity() );

        return lineItem;
    }

    @Override
    public LineItemRequestDto toDto(LineItem lineItem) {
        if ( lineItem == null ) {
            return null;
        }

        LineItemRequestDto lineItemRequestDto = new LineItemRequestDto();

        lineItemRequestDto.setDescription( lineItem.getDescription() );
        lineItemRequestDto.setPrice( lineItem.getPrice() );
        lineItemRequestDto.setQuantity( lineItem.getQuantity() );

        return lineItemRequestDto;
    }

    @Override
    public List<LineItemResponseDto> toDtoList(List<LineItem> lineItems) {
        if ( lineItems == null ) {
            return null;
        }

        List<LineItemResponseDto> list = new ArrayList<LineItemResponseDto>( lineItems.size() );
        for ( LineItem lineItem : lineItems ) {
            list.add( lineItemToLineItemResponseDto( lineItem ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(LineItemRequestDto requestDto, LineItem lineItem) {
        if ( requestDto == null ) {
            return;
        }

        if ( requestDto.getDescription() != null ) {
            lineItem.setDescription( requestDto.getDescription() );
        }
        if ( requestDto.getPrice() != null ) {
            lineItem.setPrice( requestDto.getPrice() );
        }
        if ( requestDto.getQuantity() != null ) {
            lineItem.setQuantity( requestDto.getQuantity() );
        }
    }

    protected LineItemResponseDto lineItemToLineItemResponseDto(LineItem lineItem) {
        if ( lineItem == null ) {
            return null;
        }

        LineItemResponseDto lineItemResponseDto = new LineItemResponseDto();

        lineItemResponseDto.setId( lineItem.getId() );
        lineItemResponseDto.setDescription( lineItem.getDescription() );
        lineItemResponseDto.setPrice( lineItem.getPrice() );
        lineItemResponseDto.setQuantity( lineItem.getQuantity() );
        lineItemResponseDto.setTotalAmount( lineItem.getTotalAmount() );

        return lineItemResponseDto;
    }
}
