package com.meiyou.hbase.manager.utils;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HDFSFacade {
	private static final Logger LOGGER = LoggerFactory.getLogger(HDFSFacade.class);

	private FileSystem fs = null;

	private String hdfsAddress = "";
	
	public HDFSFacade(String hdfsAddress) {
		this.hdfsAddress = hdfsAddress;
		init();
	}

	private void init() {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", hdfsAddress);
		conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
		}
		ResourceUtils.addToShutDownHook(fs);
	}
	
	public FileSystem getFs() {
		return fs;
	}
	
	public long getPathSpaceSize(String path) {
		try {
			return fs.getContentSummary(new Path(path)).getLength();
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
		return 0L;
	}

}
