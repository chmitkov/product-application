package org.ch.productshop.integration.services;

import org.ch.productshop.domain.entities.Order;
import org.ch.productshop.domain.entities.Product;
import org.ch.productshop.domain.entities.User;
import org.ch.productshop.domain.models.service.OrderServiceModel;
import org.ch.productshop.domain.models.service.OrderServiceModelNew;
import org.ch.productshop.domain.models.service.ProductServiceModel;
import org.ch.productshop.domain.models.service.UserServiceModel;
import org.ch.productshop.repository.OrderRepository;
import org.ch.productshop.service.OrderService;
import org.ch.productshop.service.ProductService;
import org.ch.productshop.service.UserService;
import org.ch.productshop.validations.ProductValidationService;
import org.ch.productshop.validations.UserValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTests {

    private static final String USER_USERNAME = "TestUsername";
    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(5.55);
    private static final String PRODUCT_IMAGE_URL = "productImageUrl";


    @Autowired
    OrderService orderService;

    @MockBean
    OrderRepository mockOrderRepository;

    @MockBean
    UserValidationService mockUserValidationService;

    @MockBean
    ProductValidationService mockProductValidationService;

    @MockBean
    UserService mockUserService;

    @MockBean
    ProductService mockProductService;

    private List<Order> orders;
    private List<Product> products;

    @Before
    public void setUpTest() {
        orders = new ArrayList<>();
        products = new ArrayList<>(){{
            add(new Product() {{
                setImageUrl(PRODUCT_IMAGE_URL);
                setPrice(TOTAL_PRICE);
            }});
        }};
        Order order = new Order(){{
            setProducts(products);
            setUser(new User());
        }};
        orders.add(order);

        Mockito.when(mockOrderRepository.findAll())
                .thenReturn(orders);
    }

    @Test
    public void findAllOrders_when1Orders_return1Order() {

        List<Order> result = this.orderService.findAll();
        Order order = result.get(0);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(order.getUser().getUsername(), USER_USERNAME);
        Assert.assertEquals(order.getTotalPrice(), TOTAL_PRICE);

    }

    @Test
    public void findAllOrders_whenNoOrders_returnsEmptyList() {
        this.orders.clear();

        List<Order> result = this.orderService.findAll();

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void createOrder_whenUserAndProductAreValid_orderCreate() throws Exception {
        Mockito.when(mockUserValidationService
                .isValid(any()))
                .thenReturn(true);
        Mockito.when(mockProductValidationService
                .isValid(any()))
                .thenReturn(true);

        Mockito.when(mockUserService
                .findUserByUsername(any()))
                .thenReturn(new UserServiceModel());
        Mockito.when(mockProductService
                .findProductById(any()))
                .thenReturn(new ProductServiceModel());

        orderService.createOrder(new OrderServiceModelNew());

        Mockito.verify(mockOrderRepository)
                .save(any());
    }

    @Test(expected = Exception.class)
    public void createOrder_whenUserAndProductAreInvalid_throwException() throws Exception {
        Mockito.when(mockUserValidationService
                .isValid(any()))
                .thenReturn(false);
        Mockito.when(mockProductValidationService
                .isValid(any()))
                .thenReturn(false);

        orderService.createOrder(new OrderServiceModelNew());
    }

    @Test(expected = Exception.class)
    public void createOrder_whenUserIsValidButProductNotValid_throwException() throws Exception {
        Mockito.when(mockUserValidationService
                .isValid(any()))
                .thenReturn(true);
        Mockito.when(mockProductValidationService
                .isValid(any()))
                .thenReturn(false);

        orderService.createOrder(new OrderServiceModelNew());
    }

    @Test(expected = Exception.class)
    public void createOrder_whenUserNotValidButProductIsValid_throwException() throws Exception {
        Mockito.when(mockUserValidationService
                .isValid(any()))
                .thenReturn(false);
        Mockito.when(mockProductValidationService
                .isValid(any()))
                .thenReturn(true);

        orderService.createOrder(new OrderServiceModelNew());
    }
}

