package com.pxxy.springshiro.realms;

import com.pxxy.springshiro.entitiy.User;
import com.pxxy.springshiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author YZJ
 * @date 2019/3/28 - 15:21
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    //授权的方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1,从PrincipalCollection中来获取登录用户的信息
        Object principal = principalCollection.getPrimaryPrincipal();
        //2,利用登录的用户的信息来确定当前用户的角色或权限（可能需要查询数据库）
        Set<String> roles=new HashSet<>();
        roles.add("user");
        if("admin".equals(principal)){
            roles.add("admin");
        }
        //3,创建SimpleAuthenticationInfo，并设置其roles属性：
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo(roles);
        //4,返回SimpleAuthenticationInfo对象，
        return info;
    }
    //认证的方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //UsernamePasswordToken封装了页面提交的用户名密码
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        String username=token.getUsername();
        User user=userService.getUserByUsername(username);
        if(user!=null) {
            //1)principal:认证的实体信息，可以是username，也可以是数据库表中对应的用户实体对象
            Object principal = username;
            //2,credentials:密码（数据库查询的）
            Object credentials = user.getPassword();
            //3,realName:当前realm对象的name，调用父类的getname方法即可
            String realName = getName();
            //使用用户的用户名进行加盐，盐值加密
            ByteSource credentialsSalt = ByteSource.Util.bytes(username);
            SimpleAuthenticationInfo info = null;//new SimpleAuthenticationInfo(principal, credentials, realName);
            info=new SimpleAuthenticationInfo(principal, credentials, credentialsSalt,realName);
            return info;
        }else {
            throw new UnknownAccountException("用户不存在！");
        }

    }

    /**
     * Shiro底层使用的加密方法
     * @param args
     */
    public static void main(String[] args) {
        String algorithmName="MD5";
        Object source="123456";
        Object salt=ByteSource.Util.bytes("yan");
        int hashIterations=1024;
        SimpleHash simpleHash = new SimpleHash(algorithmName, source, salt, hashIterations);
        System.out.println(simpleHash);
    }
}
