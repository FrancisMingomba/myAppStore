package com.francis.store.mappers;

import com.francis.store.dtos.OrderDto;
import com.francis.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
