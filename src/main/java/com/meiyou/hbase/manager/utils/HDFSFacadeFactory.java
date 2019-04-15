package com.meiyou.hbase.manager.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HDFSFacadeFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(HDFSFacadeFactory.class);

	private static final Map<String, HDFSFacade> HDFS_FACADE_CACHE = new ConcurrentHashMap<String, HDFSFacade>();

	private static final Lock LOCK = new ReentrantLock();

	public static HDFSFacade createHDFSFacade(String hdfsAddress) {
		HDFSFacade hdfsFacade = HDFS_FACADE_CACHE.get(hdfsAddress);
		if (null == hdfsFacade) {
			LOCK.lock();
			try {
				hdfsFacade = HDFS_FACADE_CACHE.get(hdfsAddress);
				if (null == hdfsFacade) {
					hdfsFacade = new HDFSFacade(hdfsAddress);
					HDFS_FACADE_CACHE.put(hdfsAddress, hdfsFacade);
				}
			} catch (Exception e) {
				LOGGER.error(e.toString(), e);
				throw new RuntimeException("Can not connect to hdfs server");
			} finally {
				LOCK.unlock();
			}
		}
		return hdfsFacade;
	}

}
