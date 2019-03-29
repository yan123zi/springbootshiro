package com.pxxy.springshiro.service.Impl;

import com.pxxy.springshiro.dao.UserDao;
import com.pxxy.springshiro.entitiy.User;
import com.pxxy.springshiro.service.UserService;
import com.pxxy.springshiro.utils.passwordMD5;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author YZJ
 * @date 2019/3/28 - 13:25
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public boolean regist(User user) {
        User userByUserName = userDao.findUserByUserName(user.getUsername());
        if(userByUserName!=null){
            return false;
        }else {
            String password = passwordMD5.encryptPasswordBySaltUseName(user.getUsername(), user.getPassword());
            user.setPassword(password);
            userDao.addUser(user);
            return true;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findUserByUserName(username);
    }

    @Override
    public String sendCode() {
        SimpleMailMessage message=new SimpleMailMessage();
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
        message.setFrom("3468827556@qq.com");//发件人
        message.setTo("673343330@qq.com");//收件人
        message.setSubject("User Regist Code");//主题
        message.setText(code);//正文
        mailSender.send(message);
        return code;
    }
}
