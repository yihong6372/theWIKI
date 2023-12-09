package net.geekh.wiki.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpRequest;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/9 08:46
 */
@Component
public class LogFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("-------------- LogFilter开始 -------------------");
        log.info("请求地址：{} {}", request.getRequestURI(), request.getMethod());
        log.info("远程地址: {}", request.getRemoteAddr());

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest,servletResponse);
        log.info("------------LogFilter 结速 耗时：{} ms----------------",System.currentTimeMillis() - startTime);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
