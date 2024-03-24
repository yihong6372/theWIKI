package net.geekh.wiki.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.geekh.wiki.domain.LoginUser;
import net.geekh.wiki.util.JwtUtil;
import net.geekh.wiki.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static net.geekh.wiki.consts.UserConst.LOGIN_TOKEN;
import static net.geekh.wiki.consts.UserConst.USER_REDIS_KEY;


/**
 * @Author YIHONG
 * @Description
 * @Date 2024/3/13 14:16
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //在请教头获取token
        String token = request.getHeader(LOGIN_TOKEN);

        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
//            throw new InsufficientAuthenticationException("token 非法");
            request.setAttribute("err", "token 非法");
            filterChain.doFilter(request, response);
            return;
        }

        String redisKey = USER_REDIS_KEY + userId;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);

        //判断获取到的用户信息是否为空，因为redis里面可能并不存在这个用户信息，例如缓存过期了
        if (Objects.isNull(loginUser)) {
//            throw new InsufficientAuthenticationException("用户未登陆111");
            request.setAttribute("err", "用户未登陆");
            filterChain.doFilter(request, response);
            return;
        }

        //把最终的LoginUser用户信息，通过setAuthentication方法，存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
