package net.geekh.wiki.controller;

import net.geekh.wiki.domain.LoginUser;
import net.geekh.wiki.domain.User;
import net.geekh.wiki.form.RegisterForm;
import net.geekh.wiki.service.UserService;
import net.geekh.wiki.vo.CommonResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

        if (!userId.equals(user.getId())) {
            throw new RuntimeException("请求参数错误");
        }
        user.setUserName(null);
        user.setEmail(null);
        user.setStatus(null);
        userService.updateById(user);
        return new CommonResponseVo<>(200, "修改信息成功");
    }

//    @PutMapping("/userPwd")
//    public ResponseVo resetPassword(String oldPassword, String newPassword) {
//        LoginUserContextBean loginUser = SecurityContextUtil.getLoginUser();
//        String userName = loginUser.getUsername();
//        String password = loginUser.getPassword();
//        if (!SecurityContextUtil.matchesPassword(oldPassword, password)) {
//            return Result.fail("修改密码失败，旧密码错误");
//        }
//        if (SecurityContextUtil.matchesPassword(newPassword, password)) {
//            return Result.fail("新密码不能与旧密码相同");
//        }
//        if (!userService.resetUserPwd(userName, newPassword)) {
//            return Result.fail("修改密码异常，请联系管理员");
//        }
//        // 更新缓存用户密码
//        tokenService.setLoginUser(loginUser);
//        return Result.success();
//    }

    //    @PreAuthorize("hasAuthority('system:test:list')")
    @GetMapping("/logout")
    public CommonResponseVo logout() {
        return userService.logout();
    }
}
