package com.meiyou.shiro.core;

import java.util.List;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.meiyou.shiro.entity.SysPermission;
import com.meiyou.shiro.service.SysPermissionService;
import com.meiyou.shiro.utils.EncryptUitls;

@Configuration
public class ShiroConfig {
	@Autowired
	private SysPermissionService sysPermissionService;

	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		// 散列算法
		hashedCredentialsMatcher.setHashAlgorithmName(EncryptUitls.HASH_ALGORITHM_NAME);
		// 散列次数
		hashedCredentialsMatcher.setHashIterations(EncryptUitls.HASH_ITERATIONS);
		return hashedCredentialsMatcher;
	}

	@Bean
	public Realm realm() {
		CustomRealm customRealm = new CustomRealm();
		customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return customRealm;
	}

	@Bean
	public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
		// 设置为ture，用于解决一个奇怪的bug。在引入spring aop的情况下，@RequiresRole注解，会导致该方法无法映射请求，导致返回404
		creator.setUsePrefix(true);
		return creator;
	}

	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(realm());
		return securityManager;
	}

	/**
	 * 过滤器： anon org.apache.shiro.web.filter.authc.AnonymousFilter authc
	 * org.apache.shiro.web.filter.authc.FormAuthenticationFilter authcBasic
	 * org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter logout
	 * org.apache.shiro.web.filter.authc.LogoutFilter noSessionCreation
	 * org.apache.shiro.web.filter.session.NoSessionCreationFilter perms
	 * org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter port
	 * org.apache.shiro.web.filter.authz.PortFilter rest
	 * org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter roles
	 * org.apache.shiro.web.filter.authz.RolesAuthorizationFilter ssl
	 * org.apache.shiro.web.filter.authz.SslFilter user
	 * org.apache.shiro.web.filter.authc.UserFilter
	 */
	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();

		// 注意此处使用的是LinkedHashMap，是有顺序的，shiro会按从上到下的顺序匹配验证，匹配了就不再继续验证
		chain.addPathDefinition("/fonts/**", "anon");// fonts
		chain.addPathDefinition("/image/**", "anon");// image
		chain.addPathDefinition("/css/**", "anon");// css
		chain.addPathDefinition("/js/**", "anon");// js

		// 可以匿名访问的url
		chain.addPathDefinition("/login", "anon");
		chain.addPathDefinition("/unauthorized", "anon");
		chain.addPathDefinition("/logout", "anon");

		List<SysPermission> sysPermissionList = sysPermissionService.listSysPermission();
		for (SysPermission sysPermission : sysPermissionList) {
			// 设置authc,roles[roleName]无效
			// chain.addPathDefinition(sysPermission.getUrl(),
			// "authc,"+sysPermission.getShrioValue());
			chain.addPathDefinition(sysPermission.getUrl(), "perms[" + sysPermission.getPermissionName() + "]");
		}

		// 其它路径均需要登录
		chain.addPathDefinition("/**", "authc");

		return chain;
	}

}
