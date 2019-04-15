package com.meiyou.hbase.manager.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

	public static String httpGet(String url) {
		// 创建httpClient
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		String result = null;
		try {
			httpclient = HttpClients.createDefault();
			// 创建httpGet
			HttpGet httpGet = new HttpGet(url);
			response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (ClientProtocolException e) {
			LOGGER.error(e.toString(), e);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.toString(), e);
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			// 关闭连接, 释放资源
			try {
				if (httpclient != null) {
					httpclient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.toString(), e);
			}
		}
		return result;
	}
}
