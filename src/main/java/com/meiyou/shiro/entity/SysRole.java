package com.meiyou.shiro.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sys_role")
public class SysRole implements Serializable {
	private static final long serialVersionUID = 64989471150593814L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Integer roleId; // 角色id

	@Column(name = "role_name")
	private String roleName; // 角色名,用于显示

	@Column(name = "role_desc")
	private String roleDesc; // 角色描述
	
	@Column(name = "sys_permission_ids")
	private String sysPermissionIds;

	@Column(name = "created")
	private Date created; // 创建时间
	
	public SysRole() {
	}

	public SysRole(Integer roleId, String roleName, String roleDesc, String sysPermissionIds, Date created) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleDesc = roleDesc;
		this.sysPermissionIds = sysPermissionIds;
		this.created = created;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getSysPermissionIds() {
		return sysPermissionIds;
	}

	public void setSysPermissionIds(String sysPermissionIds) {
		this.sysPermissionIds = sysPermissionIds;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "SysRole [roleId=" + roleId + ", roleName=" + roleName + ", roleDesc=" + roleDesc + ", sysPermissionIds="
				+ sysPermissionIds + ", created=" + created + "]";
	}

}
