package org.ch.productshop.web.contollers;

import org.ch.productshop.domain.models.service.OrderServiceModelNew;
import org.ch.productshop.domain.models.service.ProductServiceModel;
import org.ch.productshop.domain.models.service.UserServiceModel;
import org.ch.productshop.domain.models.view.ProductViewModel;
import org.ch.productshop.domain.models.view.ShoppingCartItem;
import org.ch.productshop.error.ProductNotFoundException;
import org.ch.productshop.service.OrderService;
import org.ch.productshop.service.ProductService;
import org.ch.productshop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.ch.productshop.web.GlobalConstants.SHOPPING_CART_NAME;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public CartController(ProductService productService, UserService userService, OrderService orderService, ModelMapper modelMapper) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add-product")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addToCartConfirm(String id, int quantity, HttpSession httpSession) throws ProductNotFoundException {

        this.initCart(httpSession);

        ProductViewModel productViewModel = this.modelMapper
                .map(this.productService.findProductById(id), ProductViewModel.class);

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setProductViewModel(productViewModel);
        shoppingCartItem.setQuantity(quantity);

        this.addItemToCart(shoppingCartItem, httpSession);


        return redirect("/home");
    }


    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView cartDetails(ModelAndView modelAndView, HttpSession httpSession) {
        this.initCart(httpSession);
        modelAndView.addObject("total", this.calcTotal(httpSession));

        return view("shopping-cart/cart-details", modelAndView);
    }


    @DeleteMapping("/remove-product")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView removeFromCartConfirm(String id, HttpSession httpSession) {
        this.removeItemFromCart(id, httpSession);

        return redirect("/cart/details");
    }

    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView checkoutConfirm(HttpSession httpSession, Principal principal) throws Exception, ProductNotFoundException {
        List<ShoppingCartItem> list = this.retrieveList(httpSession);

        OrderServiceModelNew orderServiceModelNew = this.prepareOrder(list, principal.getName());
        this.orderService.createOrder(orderServiceModelNew);

        httpSession.setAttribute(SHOPPING_CART_NAME, null);
        return redirect("/home");
    }

    //Private Methods

    private void initCart(HttpSession httpSession) {
        if (httpSession.getAttribute(SHOPPING_CART_NAME) == null) {
            httpSession.setAttribute(SHOPPING_CART_NAME, new LinkedList<>());
        }
    }

    private void addItemToCart(ShoppingCartItem item, HttpSession httpSession) {
        for (ShoppingCartItem shoppingCartItem : (List<ShoppingCartItem>) httpSession.getAttribute(SHOPPING_CART_NAME)) {
            if (shoppingCartItem.getProductViewModel().getId()
                    .equals(item.getProductViewModel().getId())) {

                shoppingCartItem.setQuantity(shoppingCartItem.getQuantity() + item.getQuantity());
                return;
            }
        }

        ((List<ShoppingCartItem>) httpSession.getAttribute(SHOPPING_CART_NAME))
                .add(item);

    }

    private BigDecimal calcTotal(HttpSession httpSession) {
        List<ShoppingCartItem> list = (List<ShoppingCartItem>) httpSession.getAttribute("shopping-cart");

        return list
                .stream()
                .map(i -> BigDecimal.valueOf(i.getQuantity() * 1.0).multiply(i.getProductViewModel().getPrice()))
                .reduce((a, b) -> a.add(b))
                .orElse(BigDecimal.ZERO);

    }

    private void removeItemFromCart(String id, HttpSession httpSession) {
        var list = ((List<ShoppingCartItem>) httpSession.getAttribute(SHOPPING_CART_NAME))
                .stream()
                .filter(s -> !s.getProductViewModel().getId().equals(id))
                .collect(Collectors.toList());

        httpSession.setAttribute(SHOPPING_CART_NAME, list);
    }

    private List<ShoppingCartItem> retrieveList(HttpSession httpSession) {
        return (List<ShoppingCartItem>) httpSession.getAttribute(SHOPPING_CART_NAME);
    }

    private OrderServiceModelNew prepareOrder(List<ShoppingCartItem> cartItems, String customer) throws ProductNotFoundException {
        OrderServiceModelNew orderServiceModelNew = new OrderServiceModelNew();

        UserServiceModel user = this.userService.findUserByUsername(customer);


        BigDecimal totalPrice = BigDecimal.ZERO;

//        cartItems
//                .forEach(i -> {
//                    ProductServiceModel productServiceModel = this.productService
//                            .findProductById(i.getProductViewModel().getId());
//                    for (int j = 0; j < i.getQuantity(); j++) {
//                        orderServiceModelNew.getProducts()
//                                .add(productServiceModel);
//                        totalPrice[0] = totalPrice[0].add(productServiceModel.getPrice());
//                    }
//                });
        for (ShoppingCartItem item : cartItems) {
            ProductServiceModel productServiceModel = this.productService
                    .findProductById(item.getProductViewModel().getId());
            for (int j = 0; j < item.getQuantity(); j++) {
                orderServiceModelNew.getProducts()
                        .add(productServiceModel);
                totalPrice = totalPrice.add(productServiceModel.getPrice());
            }
        }
        orderServiceModelNew.setUser(user);
        orderServiceModelNew.setTotalPrice(totalPrice);

        return orderServiceModelNew;
    }
}

