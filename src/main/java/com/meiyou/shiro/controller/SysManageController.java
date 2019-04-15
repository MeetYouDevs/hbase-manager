package com.meiyou.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.meiyou.shiro.vo.Message;

@RestController
public class SysManageController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SysManageController.class);

	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		return mv;
	}

	@PostMapping("/login")
	public Message login(String userName, String password) {
		LOGGER.info("User: " + userName + " access.");
		Subject user = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		try {
			// shiro会根据我们在CustomRealm里认证方法设置的来验证
			user.login(token);
		} catch (UnknownAccountException e) {
			// 账号不存在和下面密码错误一般都合并为一个账号或密码错误，这样可以增加暴力破解难度
			return new Message(500, "账号不存在！", null);
		} catch (DisabledAccountException e) {
			return new Message(500, "账号未启用！", null);
		} catch (IncorrectCredentialsException e) {
			return new Message(500, "密码错误！", null);
		} catch (Throwable e) {
			return new Message(500, "未知错误！", null);
		}
		return new Message(200, "成功", null);
	}

	@GetMapping("/unauthorized")
	public ModelAndView unauthorized() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unauthorized");
		return mv;
	}

	@GetMapping("/logout")
	public ModelAndView logout() {
		SecurityUtils.getSubject().logout();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		return mv;
	}

}
