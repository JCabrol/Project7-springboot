package com.nnk.springboot.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class SpringbootExceptionHandler extends ResponseEntityExceptionHandler {


    private ModelAndView buildErrorPage(SpringbootError springbootError) {
        String viewName = "error";
        Map<String, Object> model = new HashMap<>();
        model.put("status", springbootError.getStatus());
        model.put("message", springbootError.getMessage());
        return new ModelAndView(viewName, model);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    protected ModelAndView handleNotFoundObject(
            ObjectNotFoundException ex) {
        SpringbootError springbootError = new SpringbootError(NOT_FOUND);
        springbootError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(springbootError);
    }

    @ExceptionHandler(ObjectAlreadyExistingException.class)
    protected ModelAndView handleObjectAlreadyExisting(
            ObjectNotFoundException ex) {
        SpringbootError springbootError = new SpringbootError(CONFLICT);
        springbootError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(springbootError);
    }

    @ExceptionHandler(PageNotAuthorizedException.class)
    protected ModelAndView handlePageNotAuthorized(
            PageNotAuthorizedException ex) {
        SpringbootError springbootError = new SpringbootError(FORBIDDEN);
        springbootError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(springbootError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ModelAndView handleUsernameNotFound(
            UsernameNotFoundException ex) {
        SpringbootError springbootError = new SpringbootError(NOT_FOUND);
        springbootError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(springbootError);
    }
}
