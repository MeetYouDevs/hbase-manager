package com.meiyou.hbase.manager.test;

import org.codehaus.jackson.type.TypeReference;

import com.meiyou.hbase.manager.entity.Table;
import com.meiyou.hbase.manager.utils.JsonUtils;


public class TestJson {
	public static final TypeReference<Table> TABLE_TYPE = new TypeReference<Table>()
	{
	};
	
	public static void main(String[] args) {
		String jsonStr = "{\"tableName\":\"1\",\"namespace\":\"1\",\"families\":[{\"familyName\":\"1\",\"timeToLive\":\"1\",\"compressionType\":\"0\",\"blockCacheEnabled\":\"1\",\"replicationScope\":\"1\",\"bloomFilterType\":\"0\"},{\"familyName\":\"2\",\"timeToLive\":\"2\",\"compressionType\":\"0\",\"blockCacheEnabled\":\"1\",\"replicationScope\":\"1\",\"bloomFilterType\":\"0\"}]}";
		Table table = JsonUtils.getJsonVal(jsonStr, TABLE_TYPE);
		System.out.println(table.getFamilies());
	}
}
