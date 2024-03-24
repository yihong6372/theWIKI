package net.geekh.wiki.exception;

import net.geekh.wiki.vo.CommonResponseVo;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

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
        return new CommonResponseVo(-1, "参数错误  " + Objects.requireNonNull(e.getBindingResult().getFieldError()).getField()+ " " +e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public CommonResponseVo runtimeException(RuntimeException e) {
        return new CommonResponseVo(-1, "错误！！  " + e.getMessage());
    }
}
