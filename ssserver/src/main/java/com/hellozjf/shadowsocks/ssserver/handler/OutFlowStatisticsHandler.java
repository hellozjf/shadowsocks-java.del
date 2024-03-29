package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.util.ByteBufUtils;
import com.hellozjf.shadowsocks.ssserver.util.FlowStatisticsDetailRepositoryUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
public class OutFlowStatisticsHandler extends ChannelOutboundHandlerAdapter {

    private FlowStatisticsDetailRepository flowStatisticsDetailRepository;
    private Integer outType;

    public OutFlowStatisticsHandler(Integer outType) {
        this.outType = outType;
        // 手动注入FlowStatisticsDetailRepository
        flowStatisticsDetailRepository = FlowStatisticsDetailRepositoryUtils.getFlowStatisticsDetailRepository();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        ByteBuf byteBuf = ByteBufUtils.getByteBufFromMsg(msg);
        if (byteBuf != null) {
            // 统计Client -> Server流量到数据库中
            FlowStatisticsDetailRepositoryUtils.record(ctx,
                    byteBuf,
                    flowStatisticsDetailRepository,
                    outType);
        }

        ctx.write(msg, promise);
    }

    /**
     * 捕获出站的异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        log.warn("{}:{} -> {}:{} cause {}",
                localAddress.getHostString(), localAddress.getPort(),
                remoteAddress.getHostString(), remoteAddress.getPort(),
                cause.getMessage());
    }
}
