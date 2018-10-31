package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.util.ByteBufUtils;
import com.hellozjf.shadowsocks.ssserver.util.FlowStatisticsDetailRepositoryUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
public class InFlowStatisticsHandler extends ChannelInboundHandlerAdapter {

    private FlowStatisticsDetailRepository flowStatisticsDetailRepository;
    private Integer inType;

    public InFlowStatisticsHandler(Integer inType) {
        this.inType = inType;
        // 手动注入FlowStatisticsDetailRepository
        flowStatisticsDetailRepository = FlowStatisticsDetailRepositoryUtils.getFlowStatisticsDetailRepository();
    }

    @Override
    public void channelRead(ChannelHandlerContext serverRemoteCtx, Object msg) throws Exception {

        ByteBuf byteBuf = ByteBufUtils.getByteBufFromMsg(msg);
        if (byteBuf != null) {
            FlowStatisticsDetailRepositoryUtils.record(serverRemoteCtx,
                    byteBuf,
                    flowStatisticsDetailRepository,
                    inType);
        }

        serverRemoteCtx.fireChannelRead(msg);
    }

    /**
     * 捕获入站到转发前的异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        log.warn("{}:{} -> {}:{} cause {}",
                remoteAddress.getHostString(), remoteAddress.getPort(),
                localAddress.getHostString(), localAddress.getPort(),
                cause.getMessage());
    }
}
