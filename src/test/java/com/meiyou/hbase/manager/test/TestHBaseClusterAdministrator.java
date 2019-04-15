package com.meiyou.hbase.manager.test;

import java.util.Map;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.meiyou.hbase.manager.entity.DataCell;
import com.meiyou.hbase.manager.utils.HBaseFacade;
import com.meiyou.hbase.manager.utils.HBaseFacade.HTableSnapshotOpEnum;
import com.meiyou.hbase.manager.utils.HBaseFacadeFactory;

public class TestHBaseClusterAdministrator {
	public static void main(String[] args) throws Exception {
		HBaseFacade hbase3Cluster = HBaseFacadeFactory
				.createAdministrator("", "", "/hbase");
		hbase3Cluster.operateTableSnapshot("test_snapshot", "test", HTableSnapshotOpEnum.CREATE_SNAPSHOT);
		
		HBaseFacade hbaseOffline = HBaseFacadeFactory
				.createAdministrator("", "", "/hbase");
		hbaseOffline.operateTableSnapshot("test_snapshot", "test", HTableSnapshotOpEnum.CREATE_SNAPSHOT);
		
		Put put = new Put(Bytes.toBytes("row1"));
		put.addColumn(Bytes.toBytes("f"), Bytes.toBytes("c1"), Bytes.toBytes("hello, hbase"));
		hbaseOffline.put("test", put);
		
		Map<String, Map<String, DataCell>> data = hbaseOffline.scanByRowKeyPrefix("test", "r", null, null, 10);
		for(Map.Entry<String, Map<String, DataCell>> entry : data.entrySet()) {
			System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
		}
		
		Map<String, DataCell> result = hbaseOffline.get("test", "row1", null, null);
		for(Map.Entry<String, DataCell> entry : result.entrySet()) {
			System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
		}
	}
}
