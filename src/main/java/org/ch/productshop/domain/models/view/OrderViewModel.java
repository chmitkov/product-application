package org.ch.productshop.domain.models.view;

import org.ch.productshop.domain.entities.Product;
import org.ch.productshop.domain.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrderViewModel {

    private String id;
    private List<ProductDetailsViewModel> products;
    private UserAllViewModel user;
    private BigDecimal totalPrice;
    private LocalDateTime finishedOn;

    public OrderViewModel() {
    }

    public List<ProductDetailsViewModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetailsViewModel> products) {
        this.products = products;
    }

    public UserAllViewModel getUser() {
        return user;
    }

    public void setUser(UserAllViewModel user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getFinishedOn() {
        return finishedOn;
    }

    public void setFinishedOn(LocalDateTime finishedOn) {
        this.finishedOn = finishedOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
