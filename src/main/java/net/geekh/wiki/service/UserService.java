package net.geekh.wiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.geekh.wiki.domain.User;
import net.geekh.wiki.form.RegisterForm;
import net.geekh.wiki.vo.CommonResponseVo;

/**
 * @Author YIHONG
 * @Description
 * @Date 2024/3/24 16:40
 */
public interface UserService extends IService<User> {

    User register(RegisterForm registerForm);

    CommonResponseVo login(User user);

    CommonResponseVo logout();
}
