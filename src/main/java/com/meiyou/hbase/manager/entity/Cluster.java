package com.meiyou.hbase.manager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "cluster")
public class Cluster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "cluster_name")
	private String clusterName;

	@Column(name = "zookeeper_address")
	private String zookeeperAddress;

	@Column(name = "znode_parent")
	private String znodeParent;

	@Column(name = "version")
	private String version;

	@Transient
	private Integer tableCount;

	@Column(name = "hdfs_root_path")
	private String hdfsRootPath;

	@Column(name = "web_console_url")
	private String webConsoleUrl;
	
	public Cluster() {
	}

	public Cluster(Integer id, String clusterName, String zookeeperAddress, String znodeParent, String version,
			Integer tableCount, String hdfsRootPath, String webConsoleUrl) {
		this.id = id;
		this.clusterName = clusterName;
		this.zookeeperAddress = zookeeperAddress;
		this.znodeParent = znodeParent;
		this.version = version;
		this.tableCount = tableCount;
		this.hdfsRootPath = hdfsRootPath;
		this.webConsoleUrl = webConsoleUrl;
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

	public String getZookeeperAddress() {
		return zookeeperAddress;
	}

	public void setZookeeperAddress(String zookeeperAddress) {
		this.zookeeperAddress = zookeeperAddress;
	}

	public String getZnodeParent() {
		return znodeParent;
	}

	public void setZnodeParent(String znodeParent) {
		this.znodeParent = znodeParent;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getTableCount() {
		return tableCount;
	}

	public void setTableCount(Integer tableCount) {
		this.tableCount = tableCount;
	}

	public String getHdfsRootPath() {
		return hdfsRootPath;
	}

	public void setHdfsRootPath(String hdfsRootPath) {
		this.hdfsRootPath = hdfsRootPath;
	}

	public String getWebConsoleUrl() {
		return webConsoleUrl;
	}

	public void setWebConsoleUrl(String webConsoleUrl) {
		this.webConsoleUrl = webConsoleUrl;
	}

	@Override
	public String toString() {
		return "Cluster [id=" + id + ", clusterName=" + clusterName + ", zookeeperAddress=" + zookeeperAddress
				+ ", znodeParent=" + znodeParent + ", version=" + version + ", tableCount=" + tableCount
				+ ", hdfsRootPath=" + hdfsRootPath + ", webConsoleUrl=" + webConsoleUrl + "]";
	}

}
