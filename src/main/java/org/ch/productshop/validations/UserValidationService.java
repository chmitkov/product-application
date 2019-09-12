package org.ch.productshop.validations;

import org.ch.productshop.domain.entities.User;

public interface UserValidationService {
    boolean isValid(User user);
}
