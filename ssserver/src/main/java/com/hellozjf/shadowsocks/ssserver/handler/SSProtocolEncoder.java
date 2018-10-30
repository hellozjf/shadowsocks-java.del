package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.socks.SocksAddressType;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.util.List;

public class SSProtocolEncoder extends MessageToMessageEncoder<ByteBuf> {
    private static InternalLogger logger =  InternalLoggerFactory.getInstance(SSProtocolEncoder.class);


    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        logger.debug("encode " + msg.readableBytes());
        //组装ss协议
        //udp [target address][payload]
        //tcp only [payload]
        boolean isUdp = ctx.channel().attr(SSCommon.IS_UDP).get();
        if (isUdp) {
            ByteBuf addrBuff = Unpooled.buffer(128);
            SSAddrRequest ssAddr;
            InetSocketAddress remoteSrc = ctx.channel().attr(SSCommon.REMOTE_SRC).get();
            logger.debug("remote addr:{}", remoteSrc);
            if (remoteSrc.getAddress() instanceof Inet6Address) {
                ssAddr = new SSAddrRequest(SocksAddressType.IPv6, remoteSrc.getHostString(), remoteSrc.getPort());
            } else if (remoteSrc.getAddress() instanceof Inet4Address) {
                ssAddr = new SSAddrRequest(SocksAddressType.IPv4, remoteSrc.getHostString(), remoteSrc.getPort());
            } else {
                ssAddr = new SSAddrRequest(SocksAddressType.DOMAIN, remoteSrc.getHostString(), remoteSrc.getPort());
            }
            ssAddr.encodeAsByteBuf(addrBuff);
            out.add(Unpooled.wrappedBuffer(addrBuff, msg.retain()));
        } else {
            out.add(msg.retain());
        }
    }
}
