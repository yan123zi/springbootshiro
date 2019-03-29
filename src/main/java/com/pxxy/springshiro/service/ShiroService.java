package com.pxxy.springshiro.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author YZJ
 * @date 2019/3/29 - 17:22
 */
@Service
public class ShiroService {
    @RequiresRoles({"admin"})
    public void outPutDate(){
        String  key = (String) SecurityUtils.getSubject().getSession().getAttribute("key");
        System.out.println(key);
        System.out.println(new Date());
    }
}
