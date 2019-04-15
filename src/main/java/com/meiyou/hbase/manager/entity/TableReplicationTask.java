package com.meiyou.hbase.manager.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table_replication_task")
public class TableReplicationTask {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "source_cluster")
	private String sourceCluster;

	@Column(name = "source_table")
	private String sourceTable;

	@Column(name = "target_cluster")
	private String targetCluster;

	@Column(name = "target_table")
	private String targetTable;

	@Column(name = "peer_id")
	private String peerId;

	@Column(name = "create_time")
	private Date createTime;

	public TableReplicationTask() {
	}

	public TableReplicationTask(Integer id, String sourceCluster, String sourceTable, String targetCluster,
			String targetTable, String peerId, Date createTime) {
		this.id = id;
		this.sourceCluster = sourceCluster;
		this.sourceTable = sourceTable;
		this.targetCluster = targetCluster;
		this.targetTable = targetTable;
		this.peerId = peerId;
		this.createTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSourceCluster() {
		return sourceCluster;
	}

	public void setSourceCluster(String sourceCluster) {
		this.sourceCluster = sourceCluster;
	}

	public String getSourceTable() {
		return sourceTable;
	}

	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}

	public String getTargetCluster() {
		return targetCluster;
	}

	public void setTargetCluster(String targetCluster) {
		this.targetCluster = targetCluster;
	}

	public String getTargetTable() {
		return targetTable;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "TableReplicationTask [id=" + id + ", sourceCluster=" + sourceCluster + ", sourceTable=" + sourceTable
				+ ", targetCluster=" + targetCluster + ", targetTable=" + targetTable + ", peerId=" + peerId
				+ ", createTime=" + createTime + "]";
	}

}
