package com.pxxy.springshiro.authorityConfig;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author YZJ
 * @date 2019/3/29 - 20:57
 */
/**
 * 配置过滤器链
 */
public class filterChainConfig {
    public static LinkedHashMap<String,String> filterChain(){
        LinkedHashMap<String,String> filterChainDefinitionMap=new LinkedHashMap<String, String>();
        /**
         * 添加shiro内置过滤器
         * anon:无需认证（登录）就能访问
         * authc:必须认证才能访问
         * user:使用rememberMe功能可以直接访问
         * perms：该资源必须得到资源权限才能访问
         * role：该资源必须得到角色权限才能访问
         */
        filterChainDefinitionMap.put("/css/**","anon");
        filterChainDefinitionMap.put("/js/**","anon");
        filterChainDefinitionMap.put("/user/userPage","authc,roles[user]");
        filterChainDefinitionMap.put("/user/adminPage","authc,roles[admin]");
        filterChainDefinitionMap.put("/user/indexPage","user");
        filterChainDefinitionMap.put("/user/**","anon");
        filterChainDefinitionMap.put("/shiro/logout","logout");
        filterChainDefinitionMap.put("/**","authc");
        return filterChainDefinitionMap;
    }
}
