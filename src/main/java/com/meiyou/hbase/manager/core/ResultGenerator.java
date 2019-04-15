package com.meiyou.hbase.manager.core;

public class ResultGenerator {
	private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

	public static Result<String> genSuccessResult() {
		return new Result<String>().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE);
	}

	public static <T> Result<T> genSuccessResult(T data) {
		return new Result<T>().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE).setData(data);
	}

	public static Result<String> genFailResult(String message) {
		return new Result<String>().setCode(ResultCode.FAIL).setMessage(message);
	}
}
