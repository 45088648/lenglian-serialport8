package com.beetech.serialport.code;

public abstract class BaseResponse extends CommonBase {
	
	public BaseResponse() {
		super();
	}
	
	public BaseResponse(byte[] buf) {
		this();
		this.buf = buf;
	}
	
	public abstract void unpack();
}
