package org.ch.productshop.domain.models.service;

import org.ch.productshop.domain.entities.Product;

import java.math.BigDecimal;

public class PromoProductServiceModel extends BaseServiceModel {

    private Product product;
    private BigDecimal discountedPrice;
    private int discount;

    public PromoProductServiceModel() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}
