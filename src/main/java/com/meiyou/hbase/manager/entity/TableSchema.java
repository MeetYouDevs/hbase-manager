package com.meiyou.hbase.manager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table_schema")
public class TableSchema {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "cluster_name")
	private String clusterName;

	@Column(name = "table_name")
	private String tableName;

	@Column(name = "column_name")
	private String columnName;

	@Column(name = "data_type")
	private String dataType;

	@Column(name = "name_type")
	private Integer nameType;

	public TableSchema() {
	}

	public TableSchema(Integer id, String clusterName, String tableName, String columnName, String dataType,
			Integer nameType) {
		this.id = id;
		this.clusterName = clusterName;
		this.tableName = tableName;
		this.columnName = columnName;
		this.dataType = dataType;
		this.nameType = nameType;
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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getNameType() {
		return nameType;
	}

	public void setNameType(Integer nameType) {
		this.nameType = nameType;
	}

	@Override
	public String toString() {
		return "TableSchema [id=" + id + ", clusterName=" + clusterName + ", tableName=" + tableName + ", columnName="
				+ columnName + ", dataType=" + dataType + ", nameType=" + nameType + "]";
	}

}
