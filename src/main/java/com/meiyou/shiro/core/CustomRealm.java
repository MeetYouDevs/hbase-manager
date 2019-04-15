package com.meiyou.shiro.core;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.meiyou.shiro.entity.SysUser;
import com.meiyou.shiro.service.SysUserService;

public class CustomRealm extends AuthorizingRealm {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomRealm.class);

	@Autowired
	private SysUserService sysUserService;

	// 定义如何获取用户的角色和权限的逻辑，给shiro做权限判断
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) {
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
		}

		// SysUser user = (SysUser) getAvailablePrincipal(principals);
		SysUser user = (SysUser) principals.getPrimaryPrincipal();
		if (null == user) {
			LOGGER.error("授权失败，用户信息为空!");
			return null;
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		LOGGER.info("获取角色信息：" + user.getRoles());
		LOGGER.info("获取权限信息：" + user.getPerms());
		info.setRoles(user.getRoles());
		info.setStringPermissions(user.getPerms());
		return info;
	}

	// 定义如何获取用户信息的业务逻辑，给shiro做登录
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		String userName = token.getUsername();
		SysUser sysUser = sysUserService.findUserByName(userName);
		if (sysUser == null) {
			throw new UnknownAccountException("No account found for [" + userName + "]");
		}

		// 查询用户的角色和权限存到SimpleAuthenticationInfo中，这样在其它地方SecurityUtils.getSubject().getPrincipal()就能拿出用户的角色和权限
		Set<String> roles = sysUserService.getRolesByUserId(sysUser.getUserId());
		Set<String> perms = sysUserService.getPermsByUserId(sysUser.getUserId());
		sysUser.getRoles().addAll(roles);
		sysUser.getPerms().addAll(perms);

		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(),
				ByteSource.Util.bytes(sysUser.getSalt()), getName());
		return simpleAuthenticationInfo;
	}

}
