package com.meiyou.shiro.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.meiyou.hbase.manager.utils.JsonUtils;
import com.meiyou.shiro.entity.SysRole;
import com.meiyou.shiro.service.SysRoleService;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SysRoleController.class);

	@Autowired
	private SysRoleService sysRoleService;

	@RequestMapping("")
	public ModelAndView list(ModelAndView mv) {
		LOGGER.info("Query sys role list");
		return getSysRoleList(mv);
	}

	@RequestMapping("/list")
	@ResponseBody
	public String list() {
		LOGGER.info("Query sys role list");
		List<SysRole> sysRoleList = sysRoleService.listSysRole();
		return JsonUtils.toJSON(sysRoleList);
	}

	@RequestMapping("/add")
	public ModelAndView addSysRole(ModelAndView mv, Integer roleId, String roleName, String roleDesc,
			String sysPermissionIds) {
		LOGGER.info("Add or update sys role: roleId={}, roleName={}, roleDesc={}, sysPermissionIds={}.", roleId,
				roleName, roleDesc, sysPermissionIds);
		SysRole sysRole = new SysRole(roleId, roleName, roleDesc, sysPermissionIds, new Date());
		sysRoleService.addSysRole(sysRole);
		return getSysRoleList(mv);
	}

	@RequestMapping("/delete")
	public ModelAndView deleteSysRole(ModelAndView mv, Integer roleId) {
		LOGGER.info("Delete sys role: roleId={}.", roleId);
		sysRoleService.deleteSysRole(roleId);
		return getSysRoleList(mv);
	}

	private ModelAndView getSysRoleList(ModelAndView mv) {
		List<SysRole> sysRoleList = sysRoleService.listSysRole();
		mv.addObject("sysRoleList", JsonUtils.toJSON(sysRoleList));
		mv.setViewName("sys_role");
		return mv;
	}
}
