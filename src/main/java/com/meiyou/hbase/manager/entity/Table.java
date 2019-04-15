package com.meiyou.hbase.manager.entity;

import java.util.List;

public class Table {
	private String tableName = null;

	private String namespace = null;

	private List<Family> families = null;

	private Integer status = null;

	private String desc = null;
	
	private Integer numRegions = 1;
	
	private Long spaceSize = null;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public List<Family> getFamilies() {
		return families;
	}

	public void setFamilies(List<Family> families) {
		this.families = families;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getNumRegions() {
		return numRegions;
	}

	public void setNumRegions(Integer numRegions) {
		this.numRegions = numRegions;
	}

	public Long getSpaceSize() {
		return spaceSize;
	}

	public void setSpaceSize(Long spaceSize) {
		this.spaceSize = spaceSize;
	}

	@Override
	public String toString() {
		return "Table [tableName=" + tableName + ", namespace=" + namespace + ", families=" + families + ", status="
				+ status + ", desc=" + desc + ", numRegions=" + numRegions + ", spaceSize=" + spaceSize + "]";
	}
	
}
