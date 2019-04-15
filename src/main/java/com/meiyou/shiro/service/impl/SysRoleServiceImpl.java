package com.meiyou.shiro.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiyou.shiro.dao.SysRoleRepository;
import com.meiyou.shiro.entity.SysRole;
import com.meiyou.shiro.service.SysRoleService;

@Service
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired
	private SysRoleRepository sysRoleRepository;
	
	@Override
	public List<SysRole> listSysRole() {
		List<SysRole> sysRoleList = new ArrayList<SysRole>();
		sysRoleRepository.findAll().forEach(sysRoleList::add);
		return sysRoleList;
	}
	
	@Override
	public SysRole getSysRoleById(Integer roleId) {
		return sysRoleRepository.findById(roleId).get();
	}

	@Override
	public SysRole addSysRole(SysRole sysRole) {
		return sysRoleRepository.save(sysRole);
	}

	@Override
	public void deleteSysRole(Integer roleId) {
		sysRoleRepository.deleteById(roleId);
	}

}
