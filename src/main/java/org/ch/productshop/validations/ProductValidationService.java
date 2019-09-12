package org.ch.productshop.validations;

import org.ch.productshop.domain.entities.Product;
import org.ch.productshop.domain.entities.User;

public interface ProductValidationService {
    boolean isValid(Product product);
}
