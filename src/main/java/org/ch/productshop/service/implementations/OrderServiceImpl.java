package org.ch.productshop.service.implementations;

import org.ch.productshop.domain.entities.Order;
import org.ch.productshop.domain.entities.Product;
import org.ch.productshop.domain.entities.User;
import org.ch.productshop.domain.models.service.OrderServiceModel;
import org.ch.productshop.domain.models.service.OrderServiceModelNew;
import org.ch.productshop.error.OrderNotFoundException;
import org.ch.productshop.repository.OrderRepository;
import org.ch.productshop.service.OrderService;
import org.ch.productshop.service.ProductService;
import org.ch.productshop.service.UserService;
import org.ch.productshop.validations.ProductValidationService;
import org.ch.productshop.validations.UserValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserValidationService userValidationService;
    private final ProductValidationService productValidationService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, UserService userService, ModelMapper modelMapper, UserValidationService userValidationService, ProductValidationService productValidationService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userValidationService = userValidationService;
        this.productValidationService = productValidationService;
    }

    @Override
    public void createOrder(OrderServiceModelNew orderServiceModelNew) throws Exception {

        User user = this.modelMapper
                .map(this.userService.findUserByUsername(
                        orderServiceModelNew.getUser().getUsername()), User.class);

        if (!this.userValidationService.isValid(user)) {
            throw new Exception();
        }

        List<Product> productList = orderServiceModelNew
                .getProducts()
                .stream()
                .map(p -> this.modelMapper.map(p, Product.class))
                .collect(Collectors.toList());


        for (Product product : productList) {
            if (!productValidationService.isValid(product)) {
                throw new Exception();
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setProducts(productList);
        order.setTotalPrice(orderServiceModelNew.getTotalPrice());
        order.setFinishedOn(LocalDateTime.now());

        this.orderRepository
                .save(order);

//        User user = this.modelMapper
//                .map(this.userService.findUserByUsername(orderServiceModel.getUsername()), User.class);
//
//        if(!this.userValidationService.isValid(user)){
//            throw new Exception();
//        }
//
//        Product product = this.modelMapper
//                .map(this.productService
//                        .findProductById(orderServiceModel.getProductId()), Product.class);
//
//        if(!productValidationService.isValid(product)){
//            throw new Exception();
//        }
//
//        Order order = new Order(product, user, orderServiceModel.getPrice(), orderServiceModel.getDate());
//
//        this.orderRepository.save(order);
    }

    @Override
    public List<Order> findAllOrdersByUserUsername(String username) {
        return this.orderRepository
                .findAllByUserUsernameOrderByFinishedOnDesc(username);
    }

    @Override
    public List<Order> findAll() {
        return this.orderRepository
                .findAll();
    }

    @Override
    public OrderServiceModelNew findById(String id) throws OrderNotFoundException {
        return this.orderRepository
                .findById(id)
                .map(o-> this.modelMapper.map(o, OrderServiceModelNew.class))
                .orElseThrow(()-> new OrderNotFoundException());
    }
}
