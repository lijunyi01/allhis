package com.allhis.filter;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineDecoder;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

import java.nio.charset.Charset;

/**
 * Created by ljy on 15/10/22.
 * ok
 */
public class MyCodeFactory implements ProtocolCodecFactory {
    private final TextLineEncoder encoder;
    private final TextLineDecoder decoder;
    /*final static char endchar = 0x1a;*/
    final static char endchar = 0x0d;
    public MyCodeFactory() {
        this(Charset.forName("utf8"));
    }
    public MyCodeFactory(Charset charset) {
//        encoder = new TextLineEncoder(charset, LineDelimiter.UNIX);
//        decoder = new TextLineDecoder(charset, LineDelimiter.AUTO);
        //邮件协议要求LineDelimiter.CRLF
        encoder = new TextLineEncoder(charset, LineDelimiter.CRLF);
        decoder = new TextLineDecoder(charset, LineDelimiter.CRLF);
    }

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        return decoder;
    }
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        return encoder;
    }
    public int getEncoderMaxLineLength() {
        return encoder.getMaxLineLength();
    }
    public void setEncoderMaxLineLength(int maxLineLength) {
        encoder.setMaxLineLength(maxLineLength);
    }
    public int getDecoderMaxLineLength() {
        return decoder.getMaxLineLength();
    }
    public void setDecoderMaxLineLength(int maxLineLength) {
        decoder.setMaxLineLength(maxLineLength);
    }

}
