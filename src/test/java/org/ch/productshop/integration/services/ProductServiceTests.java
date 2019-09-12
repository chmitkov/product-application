package org.ch.productshop.integration.services;

import org.ch.productshop.domain.entities.Category;
import org.ch.productshop.domain.models.service.CategoryServiceModel;
import org.ch.productshop.domain.models.service.ProductServiceModel;
import org.ch.productshop.repository.ProductRepository;
import org.ch.productshop.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository mockProductRepository;

    @Test
    public void createProduct_whenValid_productCreated() throws Exception {
        productService.addProduct(new ProductServiceModel(){{
            setCategories(new ArrayList<>(){{
                add(new CategoryServiceModel());
            }});
        }});

        Mockito.verify(mockProductRepository)
                .save(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProduct_whenNull_throw() throws Exception {
        productService.addProduct(null);
        Mockito.verify(mockProductRepository)
                .save(any());
    }
}
