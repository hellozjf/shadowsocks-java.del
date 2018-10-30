package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import com.hellozjf.shadowsocks.ssserver.encryption.CryptUtil;
import com.hellozjf.shadowsocks.ssserver.encryption.ICrypt;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.List;

public class SSCipherDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static InternalLogger logger =  InternalLoggerFactory.getInstance(SSCipherDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> list) throws Exception {
        ICrypt _crypt = ctx.channel().attr(SSCommon.CIPHER).get();
        byte[] data = CryptUtil.decrypt(_crypt, msg);
        // 如果读取解密不出来数据，那就说明加密方式+密码错误了
        if (data == null) {
            // 如果是tcp通道，解密不出来数据，那就把通道关了
            if (!ctx.channel().attr(SSCommon.IS_UDP).get()) {
                ctx.close();
            }
            return;
        }
        logger.debug( (ctx.channel().attr(SSCommon.IS_UDP).get()? "(UDP)":"(TCP)")+ " decode after:"+ data.length);
        // 将解密的数据交给下一个处理器去处理
        list.add(msg.retain().clear().writeBytes(data));
    }
}
