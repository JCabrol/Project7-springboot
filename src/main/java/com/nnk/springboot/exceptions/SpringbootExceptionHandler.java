package com.nnk.springboot.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class SpringbootExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    MessageSource messageSource;

    private ModelAndView buildErrorPage(SpringbootError springbootError) {
        String viewName = "error";
        Map<String, Object> model = new HashMap<>();
        model.put("status", springbootError.getStatus());
        model.put("message", springbootError.getMessage());
        return new ModelAndView(viewName, model);
    }

    /**
     * {@link BindException}Handling
     *
     * @param bindException {@link BindException}
     * @param httpHeaders   {@link HttpHeaders}
     * @param httpStatus    {@link HttpStatus}
     * @param webRequest    {@link WebRequest}
     * @return Response to client
     */
    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException bindException,
            HttpHeaders httpHeaders,
            HttpStatus httpStatus,
            WebRequest webRequest
    ) {
        //Error list stored in the response body
        List<SpringbootError> springbootErrorList = new ArrayList<>();

        List<ObjectError> objectErrorList = bindException.getAllErrors();

        for (ObjectError objectError : objectErrorList) {

            //Get message from error code
            String message = messageSource.getMessage(objectError, webRequest.getLocale());

            //Create an error object for the response body and store it in the list
            SpringbootError springbootError = new SpringbootError(BAD_REQUEST);
            springbootError.setMessage(message);
            springbootErrorList.add(springbootError);
        }
        return new ResponseEntity<>(springbootErrorList, httpHeaders, httpStatus);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    protected ModelAndView handleNotFoundObject(
            ObjectNotFoundException ex) {
        SpringbootError springbootError = new SpringbootError(NOT_FOUND);
        springbootError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(springbootError);
    }

}
