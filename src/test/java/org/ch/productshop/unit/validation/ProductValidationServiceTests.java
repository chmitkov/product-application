package org.ch.productshop.unit.validation;

import org.ch.productshop.domain.entities.Product;
import org.ch.productshop.validations.ProductValidationService;
import org.ch.productshop.validations.implementations.ProductValidationServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class ProductValidationServiceTests   {

    private ProductValidationService productValidationService;

    @Before
    public void setUpTest(){
        productValidationService = new ProductValidationServiceImpl();
    }

    @Test
    public void isValid_whenNull_returnFalse(){
        boolean result = productValidationService.isValid(null);
        Assert.assertFalse(result);
    }

    @Test
    public void isValid_whenValid_returnTrue(){
        boolean result = productValidationService
                .isValid(new Product(){{
                    setCategories(new ArrayList<>());
                }});
        Assert.assertTrue(result);
    }
}
