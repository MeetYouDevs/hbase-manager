package com.meiyou.shiro.service;

import java.util.List;
import java.util.Set;

import com.meiyou.shiro.entity.SysUser;

public interface SysUserService {
	public Set<String> getRolesByUserId(Integer userId);
	
	public Set<String> getPermsByUserId(Integer userId);
	
	public SysUser findUserByName(String userName);
	
	public List<SysUser> listSysUser();
	
	public SysUser addSysUser(SysUser sysUser);
	
	public void deleteSysUser(Integer userId);
}
