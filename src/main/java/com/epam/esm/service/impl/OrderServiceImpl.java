package com.epam.esm.service.impl;

import com.epam.esm.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.dto.mapper.impl.OrderMapper;
import com.epam.esm.dto.request.OrderDtoRequest;
import com.epam.esm.dto.response.OrderDetailsDtoResponse;
import com.epam.esm.dto.response.OrderDtoResponse;
import com.epam.esm.entity.*;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final GiftCertificateService certificateService;

    private final UserService userService;

    private final OrderRepository repository;

    private final OrderMapper orderMapper;

    private final GiftCertificateMapper certificateMapper;

    private final MessageSource messageSource;

    @Autowired
    public OrderServiceImpl(GiftCertificateService certificateService, UserService userService,
                            OrderRepository repository, OrderMapper orderMapper,
                            GiftCertificateMapper certificateMapper, MessageSource messageSource) {
        this.certificateService = certificateService;
        this.userService = userService;
        this.repository = repository;
        this.orderMapper = orderMapper;
        this.certificateMapper = certificateMapper;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public OrderDtoResponse createNewOrder(OrderDtoRequest request) {
        Order order = orderMapper.toEntity(request);
        List<GiftCertificate> certificates = certificateService.getGiftCertificatesWithIds(request.getCertificateIds());
        List<GiftCertificate> updatedCertificates = certificateService.getFullCertificatesData(certificates);
        List<OrderGiftCertificate> orderCertificates =certificateMapper.orderTransform(updatedCertificates);
        log.info(orderCertificates.toString());
        order.setCertificates(orderCertificates);
        order.setUser(userService.getUser(order.getUser().getId()));
        order.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
        return orderMapper.toDTO(repository.create(order));
    }

    private Order getOrderById(int id){
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageSource.getMessage(ExceptionMessage.ORDER_NOT_FOUND, new Object[]{},
                    LocaleContextHolder.getLocale()));
            return new ApplicationException(ExceptionMessage.ORDER_NOT_FOUND);
        });
    }

    @Override
    public OrderDtoResponse getOrderDtoById(int id) {
        return orderMapper.toDTO(getOrderById(id));
    }

    @Override
    public OrderDetailsDtoResponse getOrderDetailsById(int id) {
        return orderMapper.toDetailsDTO(getOrderById(id));
    }

    @Override
    public List<OrderDtoResponse> getAllOrders(int id, Pagination pagination) {
        userService.getUserById(id);
        List<Order> orders = repository.findAll(id,pagination);
        return orders.stream().map(orderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Tag mostWidelyUsedTag(int id) {
        userService.getUserById(id);
        return repository.mostWidelyUsedTag(id).orElseThrow(() ->{
            log.error(messageSource.getMessage(ExceptionMessage.USER_WITHOUT_ORDER, new Object[]{},
                    LocaleContextHolder.getLocale()));
            return new ApplicationException(ExceptionMessage.USER_WITHOUT_ORDER);
        });
    }

}
