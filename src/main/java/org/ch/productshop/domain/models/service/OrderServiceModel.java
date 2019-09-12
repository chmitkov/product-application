package org.ch.productshop.domain.models.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderServiceModel extends BaseServiceModel{

    private String username;
    private String productId;
    private BigDecimal price;
    private LocalDate date;

    public OrderServiceModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
