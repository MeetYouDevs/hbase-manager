package com.meiyou.shiro.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiyou.shiro.dao.SysUserRepository;
import com.meiyou.shiro.entity.SysPermission;
import com.meiyou.shiro.entity.SysRole;
import com.meiyou.shiro.entity.SysUser;
import com.meiyou.shiro.service.SysPermissionService;
import com.meiyou.shiro.service.SysRoleService;
import com.meiyou.shiro.service.SysUserService;

@Service
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserRepository sysUserRepository;

	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysPermissionService sysPermissionService;

	/**
	 * 根据用户id查询用户的所有角色
	 * 
	 * @param uid
	 * @return
	 */
	public Set<String> getRolesByUserId(Integer userId) {
		SysUser sysUser = sysUserRepository.findById(userId).get();
		String sysRoleIds = sysUser.getSysRoleIds();
		Set<String> result = new HashSet<String>();
		if (StringUtils.isNotBlank(sysRoleIds)) {
			// 获取角色列表
			List<SysRole> sysRoleList = sysRoleService.listSysRole();
			// Map: roleId->roleName
			Map<Integer, String> sysRoleIdNameMap = new HashMap<Integer, String>();
			for (SysRole sysRole : sysRoleList) {
				sysRoleIdNameMap.put(sysRole.getRoleId(), sysRole.getRoleName());
			}
			String[] sysRoleIdsArray = sysRoleIds.split(",");
			for (String sysRoleId : sysRoleIdsArray) {
				result.add(sysRoleIdNameMap.get(Integer.parseInt(sysRoleId)));
			}
		}
		return result;
	}

	/**
	 * 模拟根据用户id查询返回用户的所有权限
	 * 
	 * @param uid
	 * @return
	 */
	public Set<String> getPermsByUserId(Integer userId) {
		SysUser sysUser = sysUserRepository.findById(userId).get();
		String sysRoleIds = sysUser.getSysRoleIds();
		Set<String> result = new HashSet<String>();
		if (StringUtils.isNotBlank(sysRoleIds)) {
			// 获取角色列表
			List<SysPermission> sysPermissionList = sysPermissionService.listSysPermission();
			// Map: id->permissionName
			Map<Integer, String> sysPermissionIdNameMap = new HashMap<Integer, String>();
			for (SysPermission sysPermission : sysPermissionList) {
				sysPermissionIdNameMap.put(sysPermission.getId(), sysPermission.getPermissionName());
			}
			String[] sysRoleIdsArray = sysRoleIds.split(",");
			for (String sysRoleId : sysRoleIdsArray) {
				SysRole sysRole = sysRoleService.getSysRoleById(Integer.parseInt(sysRoleId));
				String sysPermissionIds = sysRole.getSysPermissionIds();
				if (StringUtils.isNotBlank(sysPermissionIds)) {
					String[] sysPermissionIdsArray = sysPermissionIds.split(",");
					for (String sysPermissionId : sysPermissionIdsArray) {
						result.add(sysPermissionIdNameMap.get(Integer.parseInt(sysPermissionId)));
					}
				}
			}
		}
		return result;
	}

	/**
	 * 根据用户民返回用户信息
	 * 
	 * @param uname
	 * @return
	 */
	public SysUser findUserByName(String userName) {
		return sysUserRepository.findUserByUserName(userName);
	}

	@Override
	public List<SysUser> listSysUser() {
		List<SysUser> sysUserList = new ArrayList<SysUser>();
		sysUserRepository.findAll().forEach(sysUserList::add);
		return sysUserList;
	}

	@Override
	public SysUser addSysUser(SysUser sysUser) {
		return sysUserRepository.save(sysUser);
	}

	@Override
	public void deleteSysUser(Integer userId) {
		sysUserRepository.deleteById(userId);
	}

}
