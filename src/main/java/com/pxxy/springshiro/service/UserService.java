package com.pxxy.springshiro.service;

import com.pxxy.springshiro.entitiy.User;

/**
 * @author YZJ
 * @date 2019/3/28 - 13:24
 */
public interface UserService {
    public boolean regist(User user);
    public User getUserByUsername(String username);
    public String  sendCode();
}
