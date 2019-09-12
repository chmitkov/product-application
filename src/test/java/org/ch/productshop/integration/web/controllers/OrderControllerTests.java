package org.ch.productshop.integration.web.controllers;

import org.ch.productshop.domain.entities.Order;
import org.ch.productshop.domain.models.view.OrderViewModel;
import org.ch.productshop.repository.OrderRepository;
import org.ch.productshop.web.contollers.OrderController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure = false)
public class OrderControllerTests {

    private static final String USER_USERNAME = "TestUsername";

    @Autowired
    OrderController orderController;

    @MockBean
    OrderRepository orderRepository;

    @Mock
    Principal principal;

    private ArrayList<Order> orders;

    @Before
    public void setupTest() {
        this.orders = new ArrayList<>();

        Mockito.when(orderRepository
                .findAllByUserUsernameOrderByFinishedOnDesc(any()))
                .thenReturn(orders);

    }

    @Test
    @WithMockUser
    public void allOrdersByCustomer_whenCustomerHasNoOrders_empty() {
        orders.clear();

        Mockito.when(principal.getName())
                .thenReturn(USER_USERNAME);

        ModelAndView modelAndView = new ModelAndView();

        ModelAndView result = orderController
                .allOrdersByCustomer(principal, modelAndView);

        List<OrderViewModel> orderViewModels =
                (List<OrderViewModel>) result.getModel().get("orders");

        Assert.assertTrue(orderViewModels.isEmpty());
    }

    @Test
    @WithMockUser
    public void allOrdersByCustomer_whenAllOrdersForCustomer_orders() {
        orders.add(new Order());

        Mockito.when(principal.getName())
                .thenReturn(USER_USERNAME);

        ModelAndView modelAndView = new ModelAndView();

        ModelAndView result = orderController
                .allOrdersByCustomer(principal, modelAndView);

        List<OrderViewModel> orderViewModels =
                (List<OrderViewModel>) result.getModel().get("orders");

        Assert.assertEquals(1, orderViewModels.size());
    }

    @Test
    @WithMockUser
    public void allOrdersByCustomer_whenNotAllOrdersForCustomer_orders() {
        orders.add(new Order());

        Mockito.when(principal.getName())
                .thenReturn(USER_USERNAME);

        ModelAndView modelAndView = new ModelAndView();

        ModelAndView result = orderController
                .allOrdersByCustomer(principal, modelAndView);

        List<OrderViewModel> orderViewModels =
                (List<OrderViewModel>) result.getModel().get("orders");

        Assert.assertEquals(1, orderViewModels.size());
    }
}
