package com.meiyou.hbase.manager.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ShellUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShellUtils.class);

	private static final int SHELL_TIME_OUT = 60000;

	/**
	 * 连接
	 * @param host
	 * @param userName
	 * @param password
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public static Session connect(String host, String userName, String password, int port) throws Exception {
		LOGGER.info("connect to host:" + host + ", username:" + userName + ", password:" + password + ", port:" + port);

		JSch jsch = new JSch();
		// 获取一个session
		Session session = jsch.getSession(userName, host, port);
		session.setPassword(password);

		Properties config = new Properties();
		// 跳过key检测
		config.put("StrictHostKeyChecking", "no");

		session.setConfig(config);
		// 设置超时时间
		session.setTimeout(SHELL_TIME_OUT);
		// 建立链接
		session.connect();

		return session;
	}

	/**
	 * 在远程服务器上执行命令
	 * @param session
	 * @param cmd
	 * @return
	 */
	public static String runCmd(Session session, String cmd, LogCallBack logCallBack) {
		ChannelExec channelExec = null;
		try {
			// 打开通道
			channelExec = (ChannelExec) session.openChannel("exec");

			channelExec.setCommand(cmd);
			channelExec.setInputStream(null);
			channelExec.setErrStream(System.err);
			// 连接
			channelExec.connect();
		} catch (JSchException e) {
			channelExec.disconnect();
			session.disconnect();
			throw new RuntimeException(e.getMessage());
		}

		InputStream in = null;
		BufferedReader reader = null;
		String buf = null;
		try {
			in = channelExec.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
			while ((buf = reader.readLine()) != null) {
				if(null != logCallBack) {
					logCallBack.call(buf);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			// 关闭资源
			channelExec.disconnect();
			session.disconnect();
			try {
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return "SUCCEED";
	}
	
	public interface LogCallBack{
		void call(String message);
	}

}
