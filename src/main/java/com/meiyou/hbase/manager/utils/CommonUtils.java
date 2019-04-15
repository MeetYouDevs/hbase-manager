package com.meiyou.hbase.manager.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.util.Pair;

public class CommonUtils {

	/**
	 * 返回工程部署的根目录的绝对路径
	 * 
	 * @return
	 */
	public static String getProjectPath() {
		java.net.URL url = CommonUtils.class.getProtectionDomain().getCodeSource().getLocation();
		String filePath = null;
		try {
			filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (filePath.endsWith(".jar"))
			filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		java.io.File file = new java.io.File(filePath);
		filePath = file.getAbsolutePath();
		return filePath;
	}
	public static Pair<String,String> parseZkAddr(String zkAddr){
		Pair<String,String> hostAndPort = null;
		String hostStr = null;
		String portStr = null;
		if(StringUtils.isNotEmpty(zkAddr)){
			zkAddr = zkAddr.replace(" ","");
			String[] zks = zkAddr.split(",");
			for(String zk:zks){
				String[] params= zk.split(":");
				if(hostStr==null){
					hostStr = params[0];
					portStr = params[1];
				}else{
					hostStr = hostStr +"," + params[0];
				}

			}
		}
		hostAndPort = new Pair<>(hostStr,portStr);
		return hostAndPort;
	}
}
