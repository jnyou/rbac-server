package com.blithe.cms.realm;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfig
 * @Description: shiro配置
 * @Author: 夏小颜
 * @Date: 13:29
 * @Version: 1.0
 **/
@Configuration
@Slf4j
public class ShiroConfig {
    /**
     * 加密方式
      */
    private String hashAlgorithmName="md5";
    /**
     * 散列次数
     */
    private int hashIterations=2;

    /**
     * 声明凭证匹配器
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(hashAlgorithmName);
        credentialsMatcher.setHashIterations(hashIterations);
        return credentialsMatcher;
    }

    /**
     * 声明userRealm
     */
    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        // 注入凭证匹配器
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

    /**
     * 配置SecurityManager
     *
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 注入userRealm
        securityManager.setRealm(userRealm());
        return securityManager;
    }

    /**
     * 配置shiro的过滤器
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        // 设置安全管理器
        shiroFilter.setSecurityManager(securityManager());

        //配置拦截链 使用LinkedHashMap,因为LinkedHashMap是有序的，shiro会根据添加的顺序进行拦截
        // Map<K,V> K指的是拦截的url V值的是该url是否拦截
        Map<String,String> filterChain = new LinkedHashMap<String,String>(16);
        // 不会被拦截得内容
        filterChain.put("/css/**", "anon");
        filterChain.put("/images/**", "anon");
        filterChain.put("/fonts/**", "anon");
        filterChain.put("/js/**", "anon");
        filterChain.put("/layui/**", "anon");
        filterChain.put("/plugins/**", "anon");
        filterChain.put("/druid/**", "anon");
        filterChain.put("/page/**", "anon");
        filterChain.put("/json/**", "anon");
        //配置退出过滤器logout，由shiro实现
        filterChain.put("/logout","logout");
        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问,先配置anon再配置authc。
        filterChain.put("/login","anon");
        filterChain.put("/login/login","anon");

        // 未授权界面;
        shiroFilter.setUnauthorizedUrl("/403");
        //设置默认登录的URL.
        shiroFilter.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilter.setSuccessUrl("/index");

        filterChain.put("/**", "authc");

        shiroFilter.setFilterChainDefinitionMap(filterChain);


//        Map<String, Filter> filters = new HashMap<>();
//        filters.put("authc", formAuthenticationFilter());  // 前后端分离需要打开
        //配置过滤器
//        shiroFilter.setFilters(filters);
        return shiroFilter;
    }

    /**
     * 注册shiro的委托过滤器，相当于之前在web.xml里面配置的
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<DelegatingFilterProxy>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /* 加入注解的使用，不加入这个注解不生效--开始 */

    /**
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    /* 加入注解的使用，不加入这个注解不生效--结束 */

    /**
     * 这里是为了能在html页面引用shiro标签，上面两个函数必须添加，不然会报错
     *
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }



    /**
     * 此处不应将自定义Filter注册为 @Bean 否则SpringBoot将加载此Filter导致ShiroFilter优先级失效等一系列问题
     * 该过滤器下的页面必须验证后才能访问,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter -->
     *
     * @return
     */
//    public LoginFormAuthenticationFilter formAuthenticationFilter() {
//        return new LoginFormAuthenticationFilter();
//    }
}