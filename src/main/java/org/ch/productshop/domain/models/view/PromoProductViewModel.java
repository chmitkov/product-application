package org.ch.productshop.domain.models.view;

import java.math.BigDecimal;

public class PromoProductViewModel {

    private ProductViewModel product;
    private BigDecimal discountedPrice;
    private int discount;

    public PromoProductViewModel() {
    }

    public ProductViewModel getProduct() {
        return product;
    }

    public void setProduct(ProductViewModel product) {
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
