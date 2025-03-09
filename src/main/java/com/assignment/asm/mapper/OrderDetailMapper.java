package com.assignment.asm.mapper;

import com.assignment.asm.dto.request.orderDetail.CreateOrderDetailRequest;
import com.assignment.asm.dto.response.orderDetail.CreateOrderDetailResponse;
import com.assignment.asm.model.OrderDetail;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailMapper {
    ModelMapper modelMapper;

    public OrderDetail toModel(CreateOrderDetailRequest createOrderDetailRequest) {
        return modelMapper.map(createOrderDetailRequest, OrderDetail.class);
    }

    public CreateOrderDetailResponse toResponse(OrderDetail orderDetail) {
        return modelMapper.map(orderDetail, CreateOrderDetailResponse.class);
    }

}
