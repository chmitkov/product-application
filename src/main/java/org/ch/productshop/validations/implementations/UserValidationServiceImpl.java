package org.ch.productshop.validations.implementations;

import org.ch.productshop.domain.entities.User;
import org.ch.productshop.validations.UserValidationService;
import org.springframework.stereotype.Component;

@Component
public class UserValidationServiceImpl implements UserValidationService {
    @Override
    public boolean isValid(User user) {
        return user != null;
    }
}
