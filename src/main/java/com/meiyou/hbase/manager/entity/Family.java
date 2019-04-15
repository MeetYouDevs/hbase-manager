package com.meiyou.hbase.manager.entity;

public class Family {
	private String familyName;

	private Integer timeToLive;

	private Integer blockSize;
	
	private Integer compressionType;

	private Integer blockCacheEnabled;

	private Integer replicationScope;
	
	private Integer minVersion;

	private Integer maxVersion;

	private Integer bloomFilterType;

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public Integer getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(Integer timeToLive) {
		this.timeToLive = timeToLive;
	}

	public Integer getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(Integer blockSize) {
		this.blockSize = blockSize;
	}

	public Integer getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(Integer compressionType) {
		this.compressionType = compressionType;
	}

	public Integer getBlockCacheEnabled() {
		return blockCacheEnabled;
	}

	public void setBlockCacheEnabled(Integer blockCacheEnabled) {
		this.blockCacheEnabled = blockCacheEnabled;
	}

	public Integer getReplicationScope() {
		return replicationScope;
	}

	public void setReplicationScope(Integer replicationScope) {
		this.replicationScope = replicationScope;
	}
	
	public Integer getMinVersion() {
		return minVersion;
	}

	public void setMinVersion(Integer minVersion) {
		this.minVersion = minVersion;
	}

	public Integer getMaxVersion() {
		return maxVersion;
	}

	public void setMaxVersion(Integer maxVersion) {
		this.maxVersion = maxVersion;
	}

	public Integer getBloomFilterType() {
		return bloomFilterType;
	}

	public void setBloomFilterType(Integer bloomFilterType) {
		this.bloomFilterType = bloomFilterType;
	}

	@Override
	public String toString() {
		return "Family [familyName=" + familyName + ", timeToLive=" + timeToLive + ", blockSize=" + blockSize
				+ ", compressionType=" + compressionType + ", blockCacheEnabled=" + blockCacheEnabled
				+ ", replicationScope=" + replicationScope + ", minVersion=" + minVersion + ", maxVersion=" + maxVersion
				+ ", bloomFilterType=" + bloomFilterType + "]";
	}
	
}
