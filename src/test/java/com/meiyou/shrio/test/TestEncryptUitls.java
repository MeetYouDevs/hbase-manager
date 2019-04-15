package com.meiyou.shrio.test;

import com.meiyou.shiro.utils.EncryptUitls;

public class TestEncryptUitls {
	public static void main(String[] args) {
		String password = "admin";// 密码明文
		String salt = "1";// 以userId作为盐值
		String encryptedPassword = EncryptUitls.encrypt(password, salt);
		System.out.println("密码： " + encryptedPassword);
	}
}
