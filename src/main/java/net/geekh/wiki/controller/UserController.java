package net.geekh.wiki.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import net.geekh.wiki.domain.LoginUser;
import net.geekh.wiki.domain.User;
import net.geekh.wiki.form.RePasswordForm;
import net.geekh.wiki.form.RegisterForm;
import net.geekh.wiki.service.UserService;
import net.geekh.wiki.vo.CommonResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2024/3/24 16:35
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/list")
    public CommonResponseVo list() {
        List<User> list = userService.list();
        return new CommonResponseVo<>(200, list);
    }

    @PostMapping("/login")
    public CommonResponseVo login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/register")
    public CommonResponseVo register(@RequestBody RegisterForm registerForm) {
        userService.register(registerForm);
        return new CommonResponseVo<>(200, "注册成功");
    }

    @GetMapping("/userInfo")
    public CommonResponseVo getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getXxuser().getId();
        User user = userService.getById(userId);
        return new CommonResponseVo(200, "查询成功", user);
    }

    @PutMapping("/userInfo")
    public CommonResponseVo updateUserInfo(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getXxuser().getId();

//        if (!userId.equals(user.getId())) {
//            throw new RuntimeException("请求参数错误");
//        }
        user.setId(userId);
        user.setUserName(null);
//        user.setEmail(null);
        user.setStatus(null);
        if (!userService.updateById(user)) {
            throw new RuntimeException("保存失败");
        }
            return new CommonResponseVo<>(200, "修改信息成功");
    }

    @PutMapping("/userPassword")
    public CommonResponseVo resetPassword(@RequestBody RePasswordForm passwordForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!bCryptPasswordEncoder.matches(passwordForm.getOldPassword(), password)) {
            return new CommonResponseVo<>(200,"修改密码失败，旧密码错误");
        }
        if (bCryptPasswordEncoder.matches(passwordForm.getNewPassword(), password)) {
            return new CommonResponseVo<>(200,"新密码不能与旧密码相同");
        }

        // 更新缓存用户密码
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(true, "user_name", userName);
        updateWrapper.set("password", bCryptPasswordEncoder.encode(passwordForm.getNewPassword()));
        userService.update(updateWrapper);
//        tokenService.setLoginUser(loginUser);
        return new CommonResponseVo<>(200,"操作成功");
    }

    //    @PreAuthorize("hasAuthority('system:test:list')")
    @GetMapping("/logout")
    public CommonResponseVo logout() {
        return userService.logout();
    }
}
