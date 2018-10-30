package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
public class SSTimeoutHandler extends IdleStateHandler {

    private NioSocketChannel nioSocketChannel;

    public SSTimeoutHandler() {
        super(0, 0, SSCommon.TCP_PROXY_IDEL_TIME, TimeUnit.SECONDS);
    }

    public SSTimeoutHandler(NioSocketChannel nioSocketChannel) {
        this();
        this.nioSocketChannel = nioSocketChannel;
    }

    @Override
    protected IdleStateEvent newIdleStateEvent(IdleState state, boolean first) {
        nioSocketChannel.close();
        return super.newIdleStateEvent(state, first);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("SSTimeoutHandler exceptionCaught = {}", cause.getMessage());
    }
}
