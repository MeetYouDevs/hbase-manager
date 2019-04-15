package com.meiyou.shiro.service;

import java.util.List;
import com.meiyou.shiro.entity.SysPermission;

public interface SysPermissionService {
	public List<SysPermission> listSysPermission();

	public void addSysPermission(SysPermission sysPermission);

	public void deleteSysPermission(Integer id);
}
