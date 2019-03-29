package com.pxxy.springshiro.config;

import com.pxxy.springshiro.authorityConfig.filterChainConfig;
import com.pxxy.springshiro.realms.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;

/**
 * @author YZJ
 * @date 2019/3/27 - 19:52
 */
@Configuration
public class ShiroConfig {
    /**
     * 创建DefaultWebSecurityManager
     * @return
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager=new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm());
        /**
         * 设置cookie的保存时长，默认是一年，以下修改为10秒
         */
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.getCookie().setMaxAge(10);
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager);
        return defaultWebSecurityManager;
    }

    /**
     * 创建ShiroFilterFactoryBean
     * @param defaultWebSecurityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap());
        shiroFilterFactoryBean.setLoginUrl("/user/loginPage");
        shiroFilterFactoryBean.setUnauthorizedUrl("/user/unauthorizedPage");
        return shiroFilterFactoryBean;
    }
    //使用静态工厂方式构造一个Map
    @Bean
    public LinkedHashMap<String,String> filterChainDefinitionMap(){
        return filterChainConfig.filterChain();
    }
    @Bean
    public UserRealm userRealm(){
        UserRealm userRealm=new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }
    /**
     * 使用HashedCredentialsMatcher作为凭证匹配器，其中使用MD5加密，会将UsernamePasswordToken的密码进行MD5配置的如下加密进行加密
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher=new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }
    /*<!--
    	4.配置LifecycleBeanPostProcessor.可以自动的来调用配置spring IOC容器中shiro bean的生命周期方法
     -->
    <bean id="lifecycleBeanPostProcessor" class="org.apache.shiro.spring.LifecycleBeanPostProcessor"/>

    <!-- Enable Shiro Annotations for Spring-configured beans.  Only run after
         the lifecycleBeanProcessor has run: -->
    <!-- 5.启用IOC容器中使用shiro的注解，但必须在配置了LifecycleBeanPostProcessor之后才可以使用 -->
    <bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"
          depends-on="lifecycleBeanPostProcessor"/>
    <bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">
        <property name="securityManager" ref="securityManager"/>
    </bean>*/
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        return new DefaultAdvisorAutoProxyCreator();
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }
}
