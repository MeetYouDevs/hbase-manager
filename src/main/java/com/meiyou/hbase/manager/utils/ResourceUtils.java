package com.meiyou.hbase.manager.utils;

import java.io.Closeable;

import org.apache.commons.io.IOUtils;

public class ResourceUtils {
	/**
	 * 
	 * @Title: addToShutDownHook
	 * @Description: 添加需要关闭的资源钩子
	 * @return void
	 * @throws 方法异常
	 */
	public static void addToShutDownHook(final Closeable closeable) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				IOUtils.closeQuietly(closeable);
			}
		}));
	}
}
