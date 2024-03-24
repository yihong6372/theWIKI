package net.geekh.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import net.geekh.wiki.domain.LoginUser;
import net.geekh.wiki.domain.User;
import net.geekh.wiki.mapper.MenuMapper;
import net.geekh.wiki.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MyUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    //UserDetails是Security官方提供的接口
    public UserDetails loadUserByUsername(String xxusername) throws UsernameNotFoundException {

        //查询用户信息。我们写的userMapper接口里面是空的，所以调用的是mybatis-plus提供的方法
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //eq方法表示等值匹配，第一个参数是数据库的用户名，第二个参数是我们传进来的用户名，这两个参数进行比较是否相等
        queryWrapper.eq(User::getUserName,xxusername);
        User user = userMapper.selectOne(queryWrapper);
        //如果用户传进来的用户名，但是数据库没有这个用户名，就会导致我们是查不到的情况，那么就进行下面的判断。避免程序安全问题
        if(Objects.isNull(user)){//判断user对象是否为空。当在数据库没有查到数据时，user就会为空，也就会进入这个判断
            throw new RuntimeException("用户名或者密码错误");
        }


        //获取权限信息
        List<String> perms = menuMapper.selectPermsByUserId(user.getId());

        //把查询到的user结果，封装成UserDetails类型，然后返回。
        //但是由于UserDetails是个接口，所以我们先需要在domino目录新建LoginUser类，作为UserDetails的实现类，再写下面那行
        return new LoginUser(user,perms);
    }
}