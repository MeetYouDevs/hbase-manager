package com.meiyou.shiro.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.meiyou.hbase.manager.utils.JsonUtils;
import com.meiyou.shiro.entity.SysUser;
import com.meiyou.shiro.service.SysUserService;
import com.meiyou.shiro.utils.EncryptUitls;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

	@Autowired
	private SysUserService sysUserService;

	@RequestMapping("")
	public ModelAndView list(ModelAndView mv) {
		LOGGER.info("Query sys user list");
		return getSysUserList(mv);
	}

	@RequestMapping("/add")
	public ModelAndView addSysUser(ModelAndView mv, Integer userId, String userName, String nickName, String password,
			String sysRoleIds) {
		LOGGER.info("Add or update sys user: userId={}, userName={}, nickName={}, password={}, sysRoleIds={}.", userId,
				userName, nickName, password, sysRoleIds);
		String salt = String.valueOf(userId);
		SysUser sysUser = new SysUser(userId, userName, nickName, EncryptUitls.encrypt(password, salt), salt, sysRoleIds,
				new Date());
		sysUserService.addSysUser(sysUser);
		return getSysUserList(mv);
	}

	@RequestMapping("/delete")
	public ModelAndView deleteSysUser(ModelAndView mv, Integer userId) {
		LOGGER.info("Delete sys user: userId={}.", userId);
		sysUserService.deleteSysUser(userId);
		return getSysUserList(mv);
	}

	private ModelAndView getSysUserList(ModelAndView mv) {
		List<SysUser> sysUserList = sysUserService.listSysUser();
		mv.addObject("sysUserList", JsonUtils.toJSON(sysUserList));
		mv.setViewName("sys_user");
		return mv;
	}

}
