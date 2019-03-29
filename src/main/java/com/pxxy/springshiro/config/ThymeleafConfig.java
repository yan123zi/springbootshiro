package com.pxxy.springshiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.DefaultTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author YZJ
 * @date 2019/3/29 - 16:45
 */
@Configuration
public class ThymeleafConfig {
    @Bean
    public SpringTemplateEngine templateEngine(ShiroDialect shiroDialect) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(springResourceTemplateResolver());
        Set<IDialect> additionalDialects = new LinkedHashSet<IDialect>();
        additionalDialects.add(shiroDialect);
        templateEngine.setAdditionalDialects(additionalDialects);
        return templateEngine;
    }
    @Bean
    public SpringResourceTemplateResolver springResourceTemplateResolver(){
//        DefaultTemplateResolver templateResolver = new DefaultTemplateResolver();
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    /**
     * 配置使用shiro的方言
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
}
