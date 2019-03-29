package com.pxxy.springshiro.dao;

import com.pxxy.springshiro.entitiy.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author YZJ
 * @date 2019/3/28 - 13:25
 */
@Mapper
public interface UserDao {
    @Insert("insert into user(username,password,email) values(#{username},#{password},#{email})")
    public void addUser(User user);
    @Select("select * from user where username=#{username}")
    public User findUserByUserName(String username);
}
