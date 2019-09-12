package org.ch.productshop.service;


import org.ch.productshop.domain.entities.Order;
import org.ch.productshop.domain.models.service.OrderServiceModel;
import org.ch.productshop.domain.models.service.OrderServiceModelNew;
import org.ch.productshop.error.OrderNotFoundException;

import java.util.List;

public interface OrderService {

    void createOrder(OrderServiceModelNew orderServiceModelNew) throws Exception;

    List<Order> findAllOrdersByUserUsername(String username);

    List<Order> findAll();

    OrderServiceModelNew findById(String id) throws OrderNotFoundException;
}
