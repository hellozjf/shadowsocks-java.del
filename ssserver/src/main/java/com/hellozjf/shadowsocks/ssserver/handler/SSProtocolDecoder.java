package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;


/**
 * https://www.shadowsocks.org/en/spec/Protocol.html
 * [1-byte type][variable-length host][2-byte serverPort]
 * The following serverAddress types are defined:
 * <p>
 * 0x01: host is a 4-byte IPv4 serverAddress.
 * 0x03: host is a variable length string, starting with a 1-byte length, followed by up to 255-byte domain name.
 * 0x04: host is a 16-byte IPv6 serverAddress.
 * The serverPort number is a 2-byte big-endian unsigned integer.
 **/
@Slf4j
public class SSProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

        // [1-byte type][variable-length host][2-byte serverPort]
        if (msg.readableBytes() < 1 + 1 + 2) {
            return;
        }

        Boolean isUdp = ctx.channel().attr(SSCommon.IS_UDP).get();
        Boolean isFirstTcpPack = ctx.channel().attr(SSCommon.IS_FIRST_TCP_PACK).get();

//        log.debug("dataBuff readableBytes:" + msg.readableBytes());

        if (isUdp || (isFirstTcpPack != null && isFirstTcpPack)) {
            // 从数据包头部解析出协议类型（ipv4，domain，ipv6），地址和端口
            SSAddrRequest addrRequest = SSAddrRequest.getAddrRequest(msg);
            if (addrRequest == null) {
                log.error("fail to get serverAddress request from {},pls check client's cipher setting", ctx.channel().attr(SSCommon.CLIENT).get().getHostString());
                if (!ctx.channel().attr(SSCommon.IS_UDP).get()) {
                    ctx.close();
                }
                return;
            }
//            log.debug(ctx.channel().id().toString() + " addressType = " + addrRequest.addressType() + ",host = " + addrRequest.host() + ",serverPort = " + addrRequest.port() + ",dataBuff = "
//                    + msg.readableBytes());
            ctx.channel().attr(SSCommon.REMOTE_DES).set(new InetSocketAddress(addrRequest.host(), addrRequest.port()));
            ctx.channel().attr(SSCommon.IS_FIRST_TCP_PACK).set(false);
        }

        // 将数据包交给下一个handler
        out.add(msg.retain());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        InetSocketAddress clientSender = ctx.channel().attr(SSCommon.CLIENT).get();
        log.error("client {},error :{}", clientSender.toString(), cause.getMessage());
//        super.exceptionCaught(ctx, cause);
    }
}
