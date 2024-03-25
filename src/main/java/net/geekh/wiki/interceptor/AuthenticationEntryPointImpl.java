package net.geekh.wiki.interceptor;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import net.geekh.wiki.service.ErrorLogService;
import net.geekh.wiki.util.WebUtils;
import net.geekh.wiki.vo.CommonResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author YIHONG
 * @Description
 * @Date 2024/3/15 09:27
 */
@Component
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Autowired
    private ErrorLogService errorLogService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info((String) request.getAttribute("err"));
        CommonResponseVo vo = new CommonResponseVo<>(HttpStatus.UNAUTHORIZED.value(), "认证异常" + authException.getMessage());
        String jsonString = JSON.toJSONString(vo);
        log.info("AuthenticationEntryPoint--->{}",authException.toString());
        errorLogService.saveLog(authException, vo.getMsg());
        WebUtils.renderString(response, jsonString);
    }
}
