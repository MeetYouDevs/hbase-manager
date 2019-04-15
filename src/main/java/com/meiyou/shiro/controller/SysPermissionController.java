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
import com.meiyou.shiro.entity.SysPermission;
import com.meiyou.shiro.service.SysPermissionService;

@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SysPermissionController.class);

	@Autowired
	private SysPermissionService sysPermissionService;

	@RequestMapping("")
	public ModelAndView list(ModelAndView mv) {
		LOGGER.info("Query sys permission list");
		return getSysPermissionList(mv);
	}

	@RequestMapping("/list")
	@ResponseBody
	public String list() {
		LOGGER.info("Query sys permission list");
		List<SysPermission> sysPermissionList = sysPermissionService.listSysPermission();
		return JsonUtils.toJSON(sysPermissionList);
	}

	@RequestMapping("/add")
	public ModelAndView addSysPermission(ModelAndView mv, Integer id, String permissionName, String resName,
			String url) {
		LOGGER.info("Add or update sys permission: id={}, permissionName={}, resName={}, url={}.", id, permissionName, resName, url);
		SysPermission sysPermission = new SysPermission(id, permissionName, resName, 1, url, "roles[admin]", new Date());
		sysPermissionService.addSysPermission(sysPermission);
		return getSysPermissionList(mv);
	}

	@RequestMapping("/delete")
	public ModelAndView deleteSysPermission(ModelAndView mv, Integer id) {
		LOGGER.info("Delete sys permission: id={}.", id);
		sysPermissionService.deleteSysPermission(id);
		return getSysPermissionList(mv);
	}

	private ModelAndView getSysPermissionList(ModelAndView mv) {
		List<SysPermission> sysPermissionList = sysPermissionService.listSysPermission();
		mv.addObject("sysPermissionList", JsonUtils.toJSON(sysPermissionList));
		mv.setViewName("sys_permission");
		return mv;
	}
}
