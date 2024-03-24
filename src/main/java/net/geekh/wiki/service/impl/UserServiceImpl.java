package net.geekh.wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.geekh.wiki.domain.LoginUser;
import net.geekh.wiki.domain.User;
import net.geekh.wiki.form.RegisterForm;
import net.geekh.wiki.mapper.UserMapper;
import net.geekh.wiki.service.UserService;
import net.geekh.wiki.util.JwtUtil;
import net.geekh.wiki.util.RedisCache;
import net.geekh.wiki.util.SecurityUtil;
import net.geekh.wiki.vo.CommonResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

import static net.geekh.wiki.consts.UserConst.LOGIN_TOKEN;
import static net.geekh.wiki.consts.UserConst.USER_REDIS_KEY;

/**
 * @Author YIHONG
 * @Description
 * @Date 2024/3/24 16:41
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;



    @Override
    public User register(RegisterForm registerForm) {
        if (!registerForm.getPassword().equals(registerForm.getPassword2())) {
            throw new RuntimeException("两次密码不正确");
        }

        //检测用户是否已经存在
        if (this.lambdaQuery().eq(User::getEmail, registerForm.getEmail()).count() > 0) {
            throw new RuntimeException("该邮已注册");
        }
        if (this.lambdaQuery().eq(User::getUserName, registerForm.getUserName()).count() > 0) {
            System.out.println("用户名已经存在");
            throw new RuntimeException("用户已经存在");
        }

        User user = new User();
        user.setPassword(SecurityUtil.encryptPassword(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        user.setUserName(registerForm.getUserName());

        boolean save = this.save(user);
        return user;
    }

    @Override
    public CommonResponseVo login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登陆失败");
        }

        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
        String userId = loginUser.getXxuser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        HashMap<String, String> map = new HashMap<>();
        map.put(LOGIN_TOKEN, jwt);
        redisCache.setCacheObject(USER_REDIS_KEY + userId, loginUser);
        return new CommonResponseVo(200, "登陆成功", map);
    }

    @Override
    public CommonResponseVo logout() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();

        redisCache.deleteObject(USER_REDIS_KEY + loginUser.getXxuser().getId());
        return new CommonResponseVo(200, "登出成功");
    }
}
