package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.dto.request.OrderDtoRequest;
import com.epam.esm.dto.response.OrderDetailsDtoResponse;
import com.epam.esm.dto.response.OrderDtoResponse;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.Pagination;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService service;
    private final OrderAssembler assembler;

    @Autowired
    public OrderController(OrderService service, OrderAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping
    public OrderDtoResponse create(@RequestBody @Valid OrderDtoRequest order) {
        return assembler.toModel(service.createNewOrder(order));
    }

    @GetMapping("/{id}")
    public OrderDtoResponse find(@PathVariable int id) {
        return assembler.toModel(service.getOrderDtoById(id));
    }
    @GetMapping("/details/{id}")
    public OrderDetailsDtoResponse findDetails(@PathVariable int id) {
        return service.getOrderDetailsById(id);
    }

    @GetMapping(value = "/user/{id}")
    public List<OrderDtoResponse> findAll(@PathVariable int id, Pagination pagination) {
        return assembler.toCollectionModel(service.getAllOrders(id,pagination))
                .getContent()
                .stream()
                .toList();
    }
    @GetMapping(value = "/top-tag/user/{id}")
    public Tag mostWidelyUsedTag(@PathVariable int id){
        return service.mostWidelyUsedTag(id);
    }
}
