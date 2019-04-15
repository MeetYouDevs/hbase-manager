package com.meiyou.shiro.service;

import java.util.List;

import com.meiyou.shiro.entity.SysRole;

public interface SysRoleService {
	public List<SysRole> listSysRole();
	
	public SysRole getSysRoleById(Integer roleId);
	
	public SysRole addSysRole(SysRole sysRole);
	
	public void deleteSysRole(Integer roleId);
}
