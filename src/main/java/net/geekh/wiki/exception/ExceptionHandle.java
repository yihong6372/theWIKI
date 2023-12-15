package net.geekh.wiki.exception;

import net.geekh.wiki.vo.CommonResponseVo;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/15 21:45
 */
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public CommonResponseVo MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new CommonResponseVo(-1, e.getFieldError().getDefaultMessage());
    }
}
