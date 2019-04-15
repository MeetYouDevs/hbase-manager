package com.meiyou.shiro.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sys_user")
public class SysUser implements Serializable {
	private static final long serialVersionUID = 189021790932769144L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId; // 用户id

	@Column(name = "user_name")
	private String userName; // 登录名，不可改

	@Column(name = "nick_name")
	private String nickName; // 用户昵称，可改

	@Column(name = "password")
	private String password; // 已加密的登录密码

	@Column(name = "salt")
	private String salt; // 加密盐值

	@Column(name = "sys_role_ids")
	private String sysRoleIds; // 加密盐值

	@Column(name = "created")
	private Date created; // 创建时间

	@Transient
	private Set<String> roles = new HashSet<String>(); // 用户角色值

	@Transient
	private Set<String> perms = new HashSet<String>(); // 用户权限值

	public SysUser() {
	}

	public SysUser(Integer userId, String userName, String nickName, String password, String salt, String sysRoleIds,
			Date created) {
		this.userId = userId;
		this.userName = userName;
		this.nickName = nickName;
		this.password = password;
		this.salt = salt;
		this.sysRoleIds = sysRoleIds;
		this.created = created;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSysRoleIds() {
		return sysRoleIds;
	}

	public void setSysRoleIds(String sysRoleIds) {
		this.sysRoleIds = sysRoleIds;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public Set<String> getPerms() {
		return perms;
	}

	public void setPerms(Set<String> perms) {
		this.perms = perms;
	}

	@Override
	public String toString() {
		return "SysUser [userId=" + userId + ", userName=" + userName + ", nickName=" + nickName + ", password="
				+ password + ", salt=" + salt + ", sysRoleIds=" + sysRoleIds + ", created=" + created + "]";
	}

}
