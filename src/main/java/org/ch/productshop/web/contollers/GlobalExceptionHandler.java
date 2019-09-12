package org.ch.productshop.web.contollers;

import org.ch.productshop.error.CategoryInvalidNameException;
import org.ch.productshop.error.ProductNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    //This handle all implementations of Throwable
//    @ExceptionHandler({Throwable.class})
//    public ModelAndView handleSQLException(DataIntegrityViolationException e) {
//        ModelAndView modelAndView = new ModelAndView("error");
//
//        modelAndView.addObject("message", e.getMostSpecificCause());
//        return modelAndView;
//    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ModelAndView handleNotFoundException(ProductNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());

        return modelAndView;
    }


    @ExceptionHandler({CategoryInvalidNameException.class})
    public ModelAndView handleCategoryInvalidNameException(CategoryInvalidNameException e) {
        ModelAndView modelAndView = new ModelAndView("error");

        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());

        return modelAndView;
    }


}
