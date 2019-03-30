package com.example.common.exception;

/**
 * 自定义异常
 *
 * @Author: cxx
 * @Date: 2019/1/1 16:30
 */
public class BizException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public BizException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public BizException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public BizException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public BizException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
