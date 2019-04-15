package com.meiyou.hbase.manager.core;

import com.alibaba.fastjson.JSON;

public class Result<T> {
	private int code;
	private String message;
	private T data;

	public Result<T> setCode(ResultCode resultCode) {
		this.code = resultCode.code();
		return this;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Result<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getData() {
		return data;
	}

	public Result<T> setData(T data) {
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
