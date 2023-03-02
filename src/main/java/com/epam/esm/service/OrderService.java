package com.epam.esm.service;

import com.epam.esm.dto.request.OrderDtoRequest;
import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import com.epam.esm.dto.response.OrderDetailsDtoResponse;
import com.epam.esm.dto.response.OrderDtoResponse;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.util.Pagination;

import java.util.List;

/**
 * This interface represents Service implementation that connected controller with Data Access Object.
 *
 * @param <T> has to implement {@link Order} interface
 * @author Vitaly Karpii
 * @see Order
 */
public interface OrderService {

    /**
     * This method create new order.
     */
    OrderDtoResponse createNewOrder(OrderDtoRequest order);

    /**
     * This method return order by his id.
     *
     * @return {@link OrderDtoResponse}
     * @throws {@link ApplicationException} in case if order not found with searched id.
     */
    OrderDtoResponse getOrderDtoById(int id);

    /**
     * This method return all active orders by user id with criteria.
     *
     * @return list of{@link OrderDtoResponse}
     */
    List<OrderDtoResponse> getAllOrders(int id, Pagination pagination);

    /**
     * This method return order details by order id.
     *
     * @return {@link OrderDetailsDtoResponse}
     * @throws {@link ApplicationException} in case if order not found with searched id.
     */
    OrderDetailsDtoResponse getOrderDetailsById(int id);

    /**
     * This method return the most widely used tag of a user with the highest cost of all orders.
     *
     * @return {@link Tag}
     * @throws {@link ApplicationException} in case if order not found with searched id, or user not found.
     */
    Tag mostWidelyUsedTag(int id);
}
