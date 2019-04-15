package com.meiyou.shiro.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.meiyou.shiro.dao.SysPermissionRepository;
import com.meiyou.shiro.entity.SysPermission;
import com.meiyou.shiro.service.SysPermissionService;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {
	@Resource
	private SysPermissionRepository sysPermissionRepository;
	
	@Override
	public List<SysPermission> listSysPermission() {
		List<SysPermission> sysPermissionList = new ArrayList<SysPermission>();
		sysPermissionRepository.findAll().forEach(sysPermissionList::add);
		return sysPermissionList;
	}

	@Override
	public void addSysPermission(SysPermission sysPermission) {
		sysPermissionRepository.save(sysPermission);
	}

	@Override
	public void deleteSysPermission(Integer id) {
		sysPermissionRepository.deleteById(id);
	}

}
