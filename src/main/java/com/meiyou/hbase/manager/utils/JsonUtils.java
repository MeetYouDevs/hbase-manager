package com.meiyou.hbase.manager.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class JsonUtils {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static String toJSON(Object obj) {
		try {
			return MAPPER.writeValueAsString(obj);
		} catch (Exception e) {
		}
		return null;
	}

	public static <T> T getJsonVal(String json, TypeReference<T> type) {
		try {
			return MAPPER.readValue(json, type);
		} catch (Exception e) {
		}
		return null;
	}

	public static <T> T getJsonVal(String json, Class<T> clazz) {
		try {
			return MAPPER.readValue(json, clazz);
		} catch (Exception e) {
		}
		return null;
	}
}
