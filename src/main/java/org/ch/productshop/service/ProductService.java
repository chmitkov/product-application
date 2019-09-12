package org.ch.productshop.service;

import org.ch.productshop.domain.models.service.ProductServiceModel;
import org.ch.productshop.error.ProductNotFoundException;

import java.util.List;

public interface ProductService {

    ProductServiceModel addProduct(ProductServiceModel productServiceModel) throws Exception;

    List<ProductServiceModel> findAllProducts();

    ProductServiceModel findProductById(String id) throws ProductNotFoundException;

    ProductServiceModel editProduct(String id, ProductServiceModel productServiceModel) throws ProductNotFoundException;

    void deleteProduct(String id);

    List<ProductServiceModel> findAllProductsByCategory(String category);
}
