package com.crud_restful.exception;

import com.crud_restful.util.RespBean;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionInterceptor {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public RespBean exceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e) {

       /* String failMsg = null;
        if (e instanceof MethodArgumentNotValidException) {
            // 拿到参数校验具体异常信息提示
            failMsg = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError().getDefaultMessage();
           return RespBean.fail(ServiceEnumType.INSERT_FAIL.getCode(),failMsg);
        }
        // 将消息返回给前端
        return null;*/

        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 然后提取错误提示信息进行返回

        return RespBean.fail(com.crud_restful.exception.ServiceEnumType.INSERT_FAIL.getCode(), objectError.getDefaultMessage());
    }
}
