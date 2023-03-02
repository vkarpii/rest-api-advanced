package com.epam.esm.service.impl;


import com.epam.esm.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.dto.mapper.impl.OrderMapper;
import com.epam.esm.dto.request.OrderDtoRequest;
import com.epam.esm.dto.response.OrderDetailsDtoResponse;
import com.epam.esm.dto.response.OrderDtoResponse;
import com.epam.esm.dto.response.UserDtoResponse;
import com.epam.esm.entity.*;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    private final int DEFAULT_ID = 1;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private UserService userService;

    @Mock
    private GiftCertificateService certificateService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private GiftCertificateMapper certificateMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private OrderRepository orderRepository;

    @Test
    void getOrderDtoByIdTest(){
        Mockito.when(orderRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(new Order()));
        Mockito.when(orderMapper.toDTO(Mockito.any())).thenReturn(new OrderDtoResponse());
        OrderDtoResponse response = orderService.getOrderDtoById(DEFAULT_ID);
        Assertions.assertNotNull(response);
    }

    @Test
    void getOrdersDetailsByIdTest(){
        Order order = new Order();
        order.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
        //order.setDetails(new OrderDetails(BigDecimal.ONE, Timestamp.valueOf(LocalDateTime.now())));
        Mockito.when(orderRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(order));
        OrderDetailsDtoResponse dtoResponse = new OrderDetailsDtoResponse();
        dtoResponse.setPrice(BigDecimal.ONE);
        Mockito.when(orderMapper.toDetailsDTO(Mockito.any())).thenReturn(dtoResponse);
        OrderDetailsDtoResponse response = orderService.getOrderDetailsById(DEFAULT_ID);
        Assertions.assertNotNull(response.getPrice());
    }

    @Test
    void mostWidelyUsedTagTest(){
        Mockito.when(userService.getUserById(DEFAULT_ID)).thenReturn(new UserDtoResponse());
        Mockito.when(orderRepository.mostWidelyUsedTag(DEFAULT_ID)).thenReturn(Optional.of(new Tag()));
        Tag tag = orderService.mostWidelyUsedTag(DEFAULT_ID);
    }

    @Test
    void mostWidelyUsedTagShouldThrowException(){
        Mockito.when(userService.getUserById(DEFAULT_ID)).thenReturn(new UserDtoResponse());
        Mockito.when(orderRepository.mostWidelyUsedTag(DEFAULT_ID)).thenReturn(Optional.empty());
        assertThrows(ApplicationException.class, () -> orderService.mostWidelyUsedTag(DEFAULT_ID));
    }

    @Test
    void getOrderDtoByIdShouldThrowException(){
        Mockito.when(orderRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());
        assertThrows(ApplicationException.class, () -> orderService.getOrderDtoById(DEFAULT_ID));
    }

    @Test
    void getOrderDetailsByIdTest(){
        Pagination pagination = new Pagination(1,10);

        Mockito.when(userService.getUserById(DEFAULT_ID)).thenReturn(new UserDtoResponse());
        Mockito.when(orderRepository.findAll(DEFAULT_ID,pagination)).thenReturn(List.of(new Order()));
        List orders = orderService.getAllOrders(DEFAULT_ID,pagination);
        assertNotNull(orders);
    }

    @Test
    void createNewOrderTest(){
        OrderDtoRequest orderDtoRequest = new OrderDtoRequest();
        orderDtoRequest.setCertificateIds(List.of(DEFAULT_ID));
        Mockito.when(certificateService.getGiftCertificatesWithIds(Mockito.any()))
                .thenReturn(List.of(new GiftCertificate()));
        Mockito.when(certificateService.getFullCertificatesData(Mockito.any()))
                .thenReturn(List.of(new GiftCertificate()));
        OrderGiftCertificate orderGiftCertificate = new OrderGiftCertificate();
        orderGiftCertificate.setPrice(BigDecimal.ONE);
        orderGiftCertificate.setId(DEFAULT_ID);
        Mockito.when(certificateMapper.orderTransform(Mockito.any())).thenReturn(List.of(orderGiftCertificate));
        Mockito.when(userService.getUser(Mockito.anyInt())).thenReturn(new User());
        Order order = new Order();
        order.setCertificates(List.of(orderGiftCertificate));
        User user = new User();
        user.setId(DEFAULT_ID);
        order.setUser(user);
        Mockito.when(orderMapper.toEntity(orderDtoRequest)).thenReturn(order);
        Mockito.when(orderRepository.create(Mockito.any())).thenReturn(order);
        Mockito.when(orderMapper.toDTO(Mockito.any())).thenReturn(new OrderDtoResponse());
        OrderDtoResponse response = orderService.createNewOrder(orderDtoRequest);
        assertNotNull(response);
    }

}
