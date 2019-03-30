# springboot+shiro注解+配置版

## 1,pom文件

- pom文件的依赖如下

  ```java
  <dependencies>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-thymeleaf</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-mail</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
          <dependency>
              <groupId>org.mybatis.spring.boot</groupId>
              <artifactId>mybatis-spring-boot-starter</artifactId>
              <version>2.0.0</version>
          </dependency>
  
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-devtools</artifactId>
              <scope>runtime</scope>
          </dependency>
          <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
              <version>5.1.38</version>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-test</artifactId>
              <scope>test</scope>
          </dependency>
          <!-- https://mvnrepository.com/artifact/org.apache.shiro/shiro-all -->
          <dependency>
              <groupId>org.apache.shiro</groupId>
              <artifactId>shiro-spring</artifactId>
              <version>1.4.0</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
          <dependency>
              <groupId>com.alibaba</groupId>
              <artifactId>druid</artifactId>
              <version>1.1.10</version>
          </dependency>
          <dependency>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <version>1.18.6</version>
          </dependency>
          <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>4.12</version>
          </dependency>
          <dependency>
              <groupId>com.github.theborakompanioni</groupId>
              <artifactId>thymeleaf-extras-shiro</artifactId>
              <version>2.0.0</version>
          </dependency>
  
  </dependencies>
  ```

## 2,yml文件配置

```java
spring:
  datasource:
    username: root
    password: root
    url: jdbc:mysql://localhost:3306/clouddb01?useSSL=true
    driver-class-name: com.mysql.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
    initialSize: 5
    minIdle: 5
    maxActive: 20
    maxWait: 60000
    timeBetweenEvictionRunsMillis: 60000
    minEvictableIdleTimeMillis: 300000
    validationQuery: SELECT 1 FROM DUAL
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    poolPreparedStatements: true
    #   配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
    filters: stat,wall
    maxPoolPreparedStatementPerConnectionSize: 20
    useGlobalDataSourceStat: true
    connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
    initialization-mode: always
  thymeleaf:
    cache: false
  mail:
    username: 3468827556@qq.com
    password: uhhspjfhrumldbbe
    host: smtp.qq.com
mybatis:
  configuration:
    map-underscore-to-camel-case: true
type-aliases-package: com.pxxy.springshiro.entity
```



## 3,功能实现

- 登录注册（密码都采用用户名的MD5盐值加密）

  1. 使用rememberMe，使用HashedCredentialsMatcher的凭证匹配器

  ```java
  /**
       * 使用HashedCredentialsMatcher作为凭证匹配器，其中使用MD5加密，会将UsernamePasswordToken的密码进行MD5配置的如下加密进行加密,加密次数为1024次，token中的password采用该方式加密
       * @return
       */
      @Bean
  public HashedCredentialsMatcher hashedCredentialsMatcher(){
      HashedCredentialsMatcher hashedCredentialsMatcher=new      HashedCredentialsMatcher();
      hashedCredentialsMatcher.setHashAlgorithmName("MD5");
      hashedCredentialsMatcher.setHashIterations(1024);
      return hashedCredentialsMatcher;
  }
  ```

    2.注册进数据库的密码为经过下面的MD5盐值加密后的值

  ```java
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
  ```

  

- 授权采用静态工厂注入一个Map的方式，将所有的拦截内容单独配置（具体配置看shiroConfig.java）

```java
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
```

- html页面上使用shiro标签,页面上添加如下的xmlns

  `<html lang="en" xmlns:th="http://www.thymeleaf.org" xmlns:shiro="http://www.pollix.at/thymeleaf/shiro">`

  1. 可以使用如下的一些shiro标签，全部的请百度

     ```java
     <shiro:principal/>:显示出当前登录用户的用户名
     <shiro:hasRole name="user">:判断当前用户是否拥有哪个角色
     ```

   2.使用该shiro标签还必须配置thymeleaf模板引xin，配置如下

  ```java
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
  ```

  

- 邮箱发送验证码注册(邮箱的配置在上面的yml配置中)

- 使用druid管理数据库连接池和sql处理配置在上面的yml中，配置类如下

  ```java
  @Configuration
  public class druidConfig {
      @ConfigurationProperties(prefix = "spring.datasource")
      @Bean
      public DataSource druid(){
          return  new DruidDataSource();
      }
  
      //配置Druid的监控
      //1、配置一个管理后台的Servlet
      @Bean
      public ServletRegistrationBean statViewServlet(){
          ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
          Map<String,String> initParams = new HashMap<>();
  
          initParams.put("loginUsername","admin");
          initParams.put("loginPassword","123456");
          initParams.put("allow","127.0.0.1");//默认就是允许所有访问
          initParams.put("deny","192.168.15.21");
  
          bean.setInitParameters(initParams);
          return bean;
      }
  
  
      //2、配置一个web监控的filter
      @Bean
      public FilterRegistrationBean webStatFilter(){
          FilterRegistrationBean bean = new FilterRegistrationBean();
          bean.setFilter(new WebStatFilter());
  
          Map<String,String> initParams = new HashMap<>();
          initParams.put("exclusions","*.js,*.css,/druid/*");
  
          bean.setInitParameters(initParams);
  
          bean.setUrlPatterns(Arrays.asList("/*"));
  
          return  bean;
      }
  }
  ```

  

## 4,shiroConfig的内容如下

```java
@Configuration
public class ShiroConfig {
    /**
     * 创建DefaultWebSecurityManager
     * @return
     */
    //默认的shiro的web应用使用的安全管理器
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
    //shiro的过滤器
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
```


