package com.meiyou.hbase.manager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "run_cluster")
public class RunCluster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "cluster_name")
	private String clusterName;

	@Column(name = "ip")
	private String ip;

	@Column(name = "port")
	private String port;

	@Column(name = "ssh_user")
	private String sshUser;

	@Column(name = "ssh_password")
	private String sshPassword;

	public RunCluster() {
	}

	public RunCluster(Integer id, String clusterName, String ip, String port, String sshUser, String sshPassword) {
		this.id = id;
		this.clusterName = clusterName;
		this.ip = ip;
		this.port = port;
		this.sshUser = sshUser;
		this.sshPassword = sshPassword;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSshUser() {
		return sshUser;
	}

	public void setSshUser(String sshUser) {
		this.sshUser = sshUser;
	}

	public String getSshPassword() {
		return sshPassword;
	}

	public void setSshPassword(String sshPassword) {
		this.sshPassword = sshPassword;
	}

	@Override
	public String toString() {
		return "RunCluster [id=" + id + ", clusterName=" + clusterName + ", ip=" + ip + ", port=" + port + ", sshUser="
				+ sshUser + ", sshPassword=" + sshPassword + "]";
	}

}
