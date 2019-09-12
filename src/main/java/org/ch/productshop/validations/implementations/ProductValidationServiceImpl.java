package org.ch.productshop.validations.implementations;

import org.ch.productshop.domain.entities.Category;
import org.ch.productshop.domain.entities.Product;
import org.ch.productshop.validations.ProductValidationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductValidationServiceImpl implements ProductValidationService {
    @Override
    public boolean isValid(Product product) {
        return product != null
                && areCategoriesValid(product.getCategories());
    }

    private boolean areCategoriesValid(List<Category> categories) {
        return categories != null && !categories.isEmpty();
    }
}
