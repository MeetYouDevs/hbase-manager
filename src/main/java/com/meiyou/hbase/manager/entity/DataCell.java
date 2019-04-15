package com.meiyou.hbase.manager.entity;

public class DataCell {
	private String rowKey;

	private String column;

	private Long timestamp;

	private String value;

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Cell [rowKey=" + rowKey + ", column=" + column + ", timestamp=" + timestamp + ", value=" + value + "]";
	}

}
