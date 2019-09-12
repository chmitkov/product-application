package org.ch.productshop.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Product name exists!")
public class ProductNameAlreadyExistsException extends RuntimeException {

    private int statusCode;

    public ProductNameAlreadyExistsException() {
        this.setStatusCode(404);
    }

    public ProductNameAlreadyExistsException(String message) {
        super(message);
        this.setStatusCode(404);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
