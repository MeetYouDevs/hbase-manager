package com.meiyou.hbase.manager.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HBaseFacadeFactory {
	private static final Map<String, HBaseFacade> HBASE_FACADE_CACHE = new ConcurrentHashMap<String, HBaseFacade>();

	private static final Lock LOCK = new ReentrantLock();

	public static HBaseFacade createAdministrator(String clusterName, String zkServers, String znode) {
		HBaseFacade administrator = HBASE_FACADE_CACHE.get(clusterName);
		if (null == administrator) {
			LOCK.lock();
			try {
				administrator = HBASE_FACADE_CACHE.get(clusterName);
				if (null == administrator) {
					administrator = new HBaseFacade(zkServers, znode);
					HBASE_FACADE_CACHE.put(clusterName, administrator);
				}
			} catch (Exception e) {
				throw new RuntimeException("Can not connect to hbase server. check your zookeeper hosts");
			} finally {
				LOCK.unlock();
			}
		}
		return administrator;
	}

}
