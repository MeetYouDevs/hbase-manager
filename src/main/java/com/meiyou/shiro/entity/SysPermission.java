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
@Table(name = "sys_permission")
public class SysPermission implements Serializable {
	private static final long serialVersionUID = -141768048335689380L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 权限id
	
	@Column(name = "permission_name")
	private String permissionName; // 权限名称
	
	@Column(name = "res_name")
	private String resName; // 资源名称
	
	@Column(name = "res_type")
	private Integer resType; // 资源类型：1-菜单；2-按钮
	
	@Column(name = "url")
	private String url; // url
	
	@Column(name = "shrio_value")
	private String shrioValue; // shrio权限值
	
	@Column(name = "created")
	private Date created; // 创建时间

	public SysPermission() {
	}
	
	public SysPermission(Integer id, String permissionName, String resName, Integer resType, String url, String shrioValue,
			Date created) {
		this.id = id;
		this.permissionName = permissionName;
		this.resName = resName;
		this.resType = resType;
		this.url = url;
		this.shrioValue = shrioValue;
		this.created = created;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public Integer getResType() {
		return resType;
	}

	public void setResType(Integer resType) {
		this.resType = resType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getShrioValue() {
		return shrioValue;
	}

	public void setShrioValue(String shrioValue) {
		this.shrioValue = shrioValue;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "SysPermission [id=" + id + ", permissionName=" + permissionName + ", resName=" + resName + ", resType="
				+ resType + ", url=" + url + ", shrioValue=" + shrioValue + ", created=" + created + "]";
	}

}
