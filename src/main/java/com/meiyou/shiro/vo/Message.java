package com.meiyou.shiro.vo;

public class Message {
	private Integer code;

	private String message;

	private String desc;

	public Message(Integer code, String message, String desc) {
		this.code = code;
		this.message = message;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "Message [code=" + code + ", message=" + message + ", desc=" + desc + "]";
	}

}
