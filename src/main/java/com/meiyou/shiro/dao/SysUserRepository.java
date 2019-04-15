package com.meiyou.shiro.dao;

import org.springframework.data.repository.CrudRepository;
import com.meiyou.shiro.entity.SysUser;

public interface SysUserRepository extends CrudRepository<SysUser, Integer>{
	SysUser findUserByUserName(String userName);
}
