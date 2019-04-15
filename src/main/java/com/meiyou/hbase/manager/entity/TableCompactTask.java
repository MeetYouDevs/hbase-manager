package com.meiyou.hbase.manager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table_compact_task")
public class TableCompactTask {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "cluster_name")
	private String clusterName;

	@Column(name = "table_name")
	private String tableName;

	@Column(name = "cron")
	private String cron;

	@Column(name = "interval_position")
	private Integer intervalPosition;

	@Column(name = "`interval`")
	private Integer interval;

	@Column(name = "offset_position")
	private Integer offsetPosition;

	@Column(name = "`offset`")
	private Integer offset;

	public TableCompactTask() {
	}

	public TableCompactTask(Integer id, String clusterName, String tableName, String cron, Integer intervalPosition,
			Integer interval, Integer offsetPosition, Integer offset) {
		this.id = id;
		this.clusterName = clusterName;
		this.tableName = tableName;
		this.cron = cron;
		this.intervalPosition = intervalPosition;
		this.interval = interval;
		this.offsetPosition = offsetPosition;
		this.offset = offset;
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public Integer getIntervalPosition() {
		return intervalPosition;
	}

	public void setIntervalPosition(Integer intervalPosition) {
		this.intervalPosition = intervalPosition;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Integer getOffsetPosition() {
		return offsetPosition;
	}

	public void setOffsetPosition(Integer offsetPosition) {
		this.offsetPosition = offsetPosition;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "TableCompactTask [clusterName=" + clusterName + ", tableName=" + tableName + ", cron=" + cron
				+ ", intervalPosition=" + intervalPosition + ", interval=" + interval + ", offsetPosition="
				+ offsetPosition + ", offset=" + offset + "]";
	}

}
