package com.pxxy.springshiro.controller;

import com.pxxy.springshiro.entitiy.User;
import com.pxxy.springshiro.service.ShiroService;
import com.pxxy.springshiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author YZJ
 * @date 2019/3/27 - 19:52
 */
@Controller
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private ShiroService shiroService;
    @GetMapping("/loginPage")
    public String loginPage(){
        return "login";
    }
    @GetMapping("/registPage")
    public String registPage(){
        return "regist";
    }
    @GetMapping("/indexPage")
    public String indexPage(){
        return "index";
    }
    @GetMapping("/login")
    public String login(User user, Model model){
        Subject currentuser = SecurityUtils.getSubject();
        if(!currentuser.isAuthenticated()){
            //将用户名和密码封装到一个token中
            UsernamePasswordToken token=new UsernamePasswordToken(user.getUsername(),user.getPassword());
            //rememberme
            token.setRememberMe(true);
            try {
                System.out.println(currentuser.isRemembered());
                currentuser.login(token);
            } catch (IncorrectCredentialsException e) {
                String msg = "登录密码错误. Password for account " + token.getPrincipal() + " was incorrect.";
                model.addAttribute("message", msg);
                return "login";
            } catch (ExcessiveAttemptsException e) {
                String msg = "登录失败次数过多";
                model.addAttribute("message", msg);
                return "login";
            } catch (LockedAccountException e) {
                String  msg = "帐号已被锁定. The account for username " + token.getPrincipal() + " was locked.";
                model.addAttribute("message", msg);
                return "login";
            } catch (DisabledAccountException e) {
                String msg = "帐号已被禁用. The account for username " + token.getPrincipal() + " was disabled.";
                model.addAttribute("message", msg);
                return "login";
            } catch (ExpiredCredentialsException e) {
                String msg = "帐号已过期. the account for username " + token.getPrincipal() + "  was expired.";
                model.addAttribute("message", msg);
                return "login";
            } catch (UnknownAccountException e) {
                String msg = "帐号不存在. There is no user with username of " + token.getPrincipal();
                model.addAttribute("message", msg);
                return "login";
            } catch (UnauthorizedException e) {
                String  msg = "您没有得到相应的授权！" + e.getMessage();
                model.addAttribute("message", msg);
                return "login";
            }

        }
        System.out.println("sdfsdf");
        return "redirect:indexPage";
    }
    @PostMapping("/regist")
    public String regist(User user,String code,HttpSession session,Model model){
        String  regCode = (String) session.getAttribute("regCode");
        if(regCode.equals(code)){
            if (userService.regist(user)){
                return "redirect:indexPage";
            }else{
                model.addAttribute("msg","该用户名已经存在无法注册!");
                return "regist";
            }
        }
        model.addAttribute("msg","验证码输入错误!无法注册!");
        return "regist";
    }
    @GetMapping("/sendCode")
    public @ResponseBody String sendCode(HttpSession session){
        String code = userService.sendCode();
        session.setAttribute("regCode",code);
        return "success";
    }
    @GetMapping("/userPage")
    public String userPage(){
        return "user";
    }
    @GetMapping("/adminPage")
    public String adminPage(){
        return "admin";
    }
    @GetMapping("/unauthorizedPage")
    public String unauthorizedPage(){
        return "unauthorizedPage";
    }
    @GetMapping("/shiroAnnotation")
    public String shiroAnno(HttpSession session){
        session.setAttribute("key","123456");
        shiroService.outPutDate();
        return "index";
    }
}
