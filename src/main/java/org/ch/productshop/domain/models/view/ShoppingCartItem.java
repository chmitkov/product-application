package org.ch.productshop.domain.models.view;

import java.io.Serializable;

public class ShoppingCartItem implements Serializable {

    private ProductViewModel productViewModel;
    private int quantity;

    public ShoppingCartItem() {
    }

    public ProductViewModel getProductViewModel() {
        return productViewModel;
    }

    public void setProductViewModel(ProductViewModel productViewModel) {
        this.productViewModel = productViewModel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
