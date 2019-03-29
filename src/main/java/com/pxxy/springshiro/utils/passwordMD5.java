package com.pxxy.springshiro.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author YZJ
 * @date 2019/3/28 - 21:57
 */

/**
 * 将密码使用用户名进行盐值加密
 */
public class passwordMD5 {
    public static String  encryptPasswordBySaltUseName(String username,String password){
        String algorithmName="MD5";
        Object source=password;
        Object salt= ByteSource.Util.bytes(username);
        int hashIterations=1024;
        return new SimpleHash(algorithmName, source, salt, hashIterations).toString();
    }
//    public static String encryptPassword(String password)
//    {
//        String salt = new SecureRandomNumberGenerator().nextBytes().toHex(); //生成盐值
//        String ciphertext = new Md5Hash(password,"yan",1024).toString(); //生成的密文
//
//        String[] strings = new String[]{salt, ciphertext};
//
//        return ciphertext;
//    }

//    public static void main(String[] args) {
//        String s = encryptPasswordBySaltUseName("yan","123456");
//        System.out.println(s);
//    }
}
