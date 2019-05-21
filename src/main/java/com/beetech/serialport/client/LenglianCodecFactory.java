package com.beetech.serialport.client;

import java.nio.charset.Charset;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineDecoder;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

public class LenglianCodecFactory implements ProtocolCodecFactory {
	private final ProtocolEncoder encoder;
	private final ProtocolDecoder decoder;

	public LenglianCodecFactory(){
		encoder = new TextLineEncoder(Charset.forName("GBK"), new LineDelimiter("\0"));
		decoder = new TextLineDecoder(Charset.forName("GBK"), new LineDelimiter("\0"));
	}
	
	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}
}