package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.service.IFlowStatisticsDetailService;
import com.hellozjf.shadowsocks.ssserver.util.BeanUtils;
import com.hellozjf.shadowsocks.ssserver.util.ChannelUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
public class Remote2ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private ChannelHandlerContext clientServerCtx;

    public Remote2ServerHandler(ChannelHandlerContext client2ServerCtx) {
        this.clientServerCtx = client2ServerCtx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext serverRemoteCtx, ByteBuf msg) throws Exception {
        // 将remote -> server的数据再发送到server -> client通道
        clientServerCtx.channel().writeAndFlush(msg.retain());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext serverRemoteCtx) throws Exception {
        try {
            // 将clientServerCtx里面的变量同样也设置到serverRemoteCtx里面
            serverRemoteCtx.channel().attr(SSCommon.SERVER).set(clientServerCtx.channel().attr(SSCommon.SERVER).get());
            serverRemoteCtx.channel().attr(SSCommon.CLIENT).set(clientServerCtx.channel().attr(SSCommon.CLIENT).get());
            serverRemoteCtx.channel().attr(SSCommon.REMOTE_DES).set(clientServerCtx.channel().attr(SSCommon.REMOTE_DES).get());
        } catch (Exception e) {
            log.error("e={}", e);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext serverRemoteCtx) throws Exception {
        ChannelUtils.closeChannels(serverRemoteCtx, clientServerCtx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext serverRemoteCtx, Throwable cause) throws Exception {
        InetSocketAddress localAddress = (InetSocketAddress) serverRemoteCtx.channel().localAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) serverRemoteCtx.channel().remoteAddress();
        log.warn("{}:{} -> {}:{} cause {}",
                remoteAddress.getHostString(), remoteAddress.getPort(),
                localAddress.getHostString(), localAddress.getPort(),
                cause.getMessage());
        ChannelUtils.closeChannels(serverRemoteCtx, clientServerCtx);
    }
}
