package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.dto.request.OrderDtoRequest;
import com.epam.esm.dto.response.OrderDetailsDtoResponse;
import com.epam.esm.dto.response.OrderDtoResponse;
import com.epam.esm.dto.response.OrderGiftCertificateResponse;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.util.IsoDateFormatter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Component
public class OrderMapper implements DtoMapper<OrderDtoResponse, OrderDtoRequest, Order> {

    private final IsoDateFormatter dateFormatter;

    @Autowired
    public OrderMapper(IsoDateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    @Override
    public OrderDtoResponse toDTO(Order entity) {
        OrderDtoResponse response = new OrderDtoResponse();
        response.setId(entity.getId());
        response.setPrice(calculatePrice(entity));
        List<OrderGiftCertificateResponse> certificateResponse =  new ArrayList<>();
        entity.getCertificates().forEach(entityCertificate -> {
            OrderGiftCertificateResponse orderCertificate = new OrderGiftCertificateResponse();
            orderCertificate.setName(entityCertificate.getName());
            orderCertificate.setTags(entityCertificate.getTags());
            orderCertificate.setId(entityCertificate.getId());
            orderCertificate.setDescription(entityCertificate.getDescription());
            orderCertificate.setEndValidDate(
                    dateFormatter.convertTimesTampToISOFormat(entityCertificate.getEndValidDate()));
            orderCertificate.setStartValidDate(
                    dateFormatter.convertTimesTampToISOFormat(entityCertificate.getStartValidDate()));
            orderCertificate.setPrice(entityCertificate.getPrice());
            certificateResponse.add(orderCertificate);
        });
        response.setCertificates(certificateResponse);
        response.setUser(entity.getUser());
        response.setPurchaseDate(dateFormatter.convertTimesTampToISOFormat(entity.getPurchaseDate()));
        return response;
    }

    private BigDecimal calculatePrice(Order order) {
        List<BigDecimal> prices = new ArrayList<>();
        order.getCertificates().forEach(certificate -> prices.add(certificate.getPrice()));
        return prices.stream().reduce(BigDecimal::add).orElseThrow(() -> {
            log.error(ExceptionMessage.ORDER_CREATING_ERROR);
            return new ApplicationException(ExceptionMessage.ORDER_CREATING_ERROR);
        });
    }

    public OrderDetailsDtoResponse toDetailsDTO(Order order) {
        OrderDetailsDtoResponse response = new OrderDetailsDtoResponse();
        response.setPrice(calculatePrice(order));
        response.setPurchaseDate(dateFormatter.convertTimesTampToISOFormat(Timestamp.valueOf(LocalDateTime.now())));
        return response;
    }

    @Override
    public Order toEntity(OrderDtoRequest request) {
        Order order = new Order();
        User user = new User();
        user.setId(request.getUserId());
        order.setUser(user);
        return order;
    }

}
