package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import com.hellozjf.shadowsocks.ssserver.encryption.CryptUtil;
import com.hellozjf.shadowsocks.ssserver.encryption.ICrypt;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SSCipherEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
//        log.debug("encode msg size:{}", msg.readableBytes());
        ICrypt _crypt = ctx.channel().attr(SSCommon.CIPHER).get();
        byte[] encryptedData = CryptUtil.encrypt(_crypt, msg);
//        log.debug("encode after encryptedData size:{}", encryptedData.length);
        out.add(msg.retain().clear().writeBytes(encryptedData));
    }
}
