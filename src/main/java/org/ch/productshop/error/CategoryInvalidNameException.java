package org.ch.productshop.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Invalid category name!")
public class CategoryInvalidNameException extends RuntimeException {

    private int statusCode;

    public CategoryInvalidNameException() {
        this.setStatusCode(406);
    }

    public CategoryInvalidNameException(String message) {
        super(message);
        this.setStatusCode(406);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
