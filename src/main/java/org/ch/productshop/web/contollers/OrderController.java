package org.ch.productshop.web.contollers;

import org.ch.productshop.domain.models.binding.OrderBindingModel;
import org.ch.productshop.domain.models.service.OrderServiceModel;
import org.ch.productshop.domain.models.view.OrderViewModel;
import org.ch.productshop.domain.models.view.ProductDetailsViewModel;
import org.ch.productshop.error.OrderNotFoundException;
import org.ch.productshop.error.ProductNotFoundException;
import org.ch.productshop.service.OrderService;
import org.ch.productshop.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.ch.productshop.web.GlobalConstants.VIEW_MODEL_OBJECT_NAME;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {

    private final OrderService orderService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ProductService productService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/product/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView orderProduct(@PathVariable String id, ModelAndView modelAndView,
                                     Principal principal) throws ProductNotFoundException {

        ProductDetailsViewModel productDetailsViewModel = this.modelMapper
                .map(this.productService.findProductById(id), ProductDetailsViewModel.class);

        modelAndView.addObject(VIEW_MODEL_OBJECT_NAME, productDetailsViewModel);
        //modelAndView.addObject("customerName", principal.getName());
        //Make this with th:text="'Customer: ' + ${#authentication.getName()}" in html input element

        return super.view("order/order-details", modelAndView);
    }

//    @PostMapping("/add")
//    @PreAuthorize("isAuthenticated()")
//    public ModelAndView addOrderConfirm(@ModelAttribute(name = "model") OrderBindingModel model) throws Exception {
//
//        OrderServiceModel orderServiceModel = this.modelMapper
//                .map(model, OrderServiceModel.class);
//        orderServiceModel.setUsername(model.getCustomerName());
//        orderServiceModel.setProductId(model.getProductId());
//        orderServiceModel.setDate(LocalDate.parse(model.getDate()));
//
//        this.orderService
//                .createOrder(orderServiceModel);
//
//        return super.redirect("/orders/my/" + model.getCustomerName());
//    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView allOrdersByCustomer(Principal principal, ModelAndView modelAndView) {
        List<OrderViewModel> orders = this.orderService
                .findAllOrdersByUserUsername(principal.getName())
                .stream()
                .map(o -> this.modelMapper.map(o, OrderViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("orders", orders);

        return view("order/all-orders", modelAndView);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allOrders(ModelAndView modelAndView) {

        List<OrderViewModel> orders = this.orderService
                .findAll()
                .stream()
                .map(o -> this.modelMapper.map(o, OrderViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("orders", orders);

        return view("order/all-orders", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allOrderDetails(@PathVariable String id, ModelAndView modelAndView) throws OrderNotFoundException {
        OrderViewModel orderViewModel = this.modelMapper
                .map(this.orderService.findById(id), OrderViewModel.class);

        modelAndView.addObject("order", orderViewModel);

        return view("order/order-all-products", modelAndView);
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class,
                new StringTrimmerEditor(true));
    }
}
