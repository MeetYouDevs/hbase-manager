package com.meiyou.hbase.manager.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table_stat_result")
public class TableStatResult {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "cluster_name")
	private String clusterName;

	@Column(name = "table_name")
	private String tableName;

	@Column(name = "region_server")
	private String regionServer;

	@Column(name = "column_family")
	private String columnFamily;

	@Column(name = "table_region_server_url")
	private String tableRegionServerUrl;

	@Column(name = "store_file_path")
	private String storeFilePath;

	@Column(name = "store_file_size")
	private Integer storeFileSize;

	@Column(name = "max_row_size")
	private Integer maxRowSize;

	@Column(name = "biggest_row_key")
	private String biggestRowKey;

	@Column(name = "create_time")
	private Date createTime;

	public TableStatResult() {
	}

	public TableStatResult(Integer id, String clusterName, String tableName, String regionServer, String columnFamily,
			String tableRegionServerUrl, String storeFilePath, Integer storeFileSize, Integer maxRowSize,
			String biggestRowKey, Date createTime) {
		this.id = id;
		this.clusterName = clusterName;
		this.tableName = tableName;
		this.regionServer = regionServer;
		this.columnFamily = columnFamily;
		this.tableRegionServerUrl = tableRegionServerUrl;
		this.storeFilePath = storeFilePath;
		this.storeFileSize = storeFileSize;
		this.maxRowSize = maxRowSize;
		this.biggestRowKey = biggestRowKey;
		this.createTime = createTime;
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

	public String getRegionServer() {
		return regionServer;
	}

	public void setRegionServer(String regionServer) {
		this.regionServer = regionServer;
	}

	public String getColumnFamily() {
		return columnFamily;
	}

	public void setColumnFamily(String columnFamily) {
		this.columnFamily = columnFamily;
	}

	public String getTableRegionServerUrl() {
		return tableRegionServerUrl;
	}

	public void setTableRegionServerUrl(String tableRegionServerUrl) {
		this.tableRegionServerUrl = tableRegionServerUrl;
	}

	public String getStoreFilePath() {
		return storeFilePath;
	}

	public void setStoreFilePath(String storeFilePath) {
		this.storeFilePath = storeFilePath;
	}

	public Integer getStoreFileSize() {
		return storeFileSize;
	}

	public void setStoreFileSize(Integer storeFileSize) {
		this.storeFileSize = storeFileSize;
	}

	public Integer getMaxRowSize() {
		return maxRowSize;
	}

	public void setMaxRowSize(Integer maxRowSize) {
		this.maxRowSize = maxRowSize;
	}

	public String getBiggestRowKey() {
		return biggestRowKey;
	}

	public void setBiggestRowKey(String biggestRowKey) {
		this.biggestRowKey = biggestRowKey;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "TableStatResult [id=" + id + ", clusterName=" + clusterName + ", tableName=" + tableName
				+ ", regionServer=" + regionServer + ", columnFamily=" + columnFamily + ", tableRegionServerUrl="
				+ tableRegionServerUrl + ", storeFilePath=" + storeFilePath + ", storeFileSize=" + storeFileSize
				+ ", maxRowSize=" + maxRowSize + ", biggestRowKey=" + biggestRowKey + ", createTime=" + createTime
				+ "]";
	}

}
