package com.guo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Administrator on 2020/1/12.
 */
@ControllerAdvice
public class ControlExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ControlExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("Request URL : {} , EXCEPTION : {}", request.getRequestURL(), e);

        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
                throw e ;
        }

        ModelAndView model = new ModelAndView();
        model.addObject("url", request.getRequestURL());
        model.addObject("exception", e);
        model.setViewName("error/error");
        return model;
    }

}
