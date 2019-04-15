package com.meiyou.shiro.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class EncryptUitls {
	/**
	 * 加密算法
	 */
	public static final String HASH_ALGORITHM_NAME = "MD5";

	/**
	 * 加密次数
	 */
	public static final Integer HASH_ITERATIONS = 2;

	public static String encrypt(String password, String salt) {
		return new SimpleHash(HASH_ALGORITHM_NAME, password, ByteSource.Util.bytes(salt), HASH_ITERATIONS).toHex();
	}

}
