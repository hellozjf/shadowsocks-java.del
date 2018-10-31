package com.hellozjf.shadowsocks.ssserver.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
public class IdleCloseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 如果空闲时间超时了，那么就要关闭通道
            InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
            log.debug("{}:{} -> {}:{} 因为空闲超时关闭",
                    remoteAddress.getHostString(), remoteAddress.getPort(),
                    localAddress.getHostString(), localAddress.getPort());
            ctx.close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
