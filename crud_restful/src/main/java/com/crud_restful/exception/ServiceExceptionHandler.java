package com.crud_restful.exception;

import com.crud_restful.util.RespBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice  //对controller的增强
public class ServiceExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public RespBean serviceHandler(ServiceException e) {
        RespBean respBean = RespBean.fail(e.getCode(), e.getMsg());
        return respBean;
    }
}
