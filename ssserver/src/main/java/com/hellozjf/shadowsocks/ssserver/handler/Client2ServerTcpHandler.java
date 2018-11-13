package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.service.IFlowStatisticsDetailService;
import com.hellozjf.shadowsocks.ssserver.util.BeanUtils;
import com.hellozjf.shadowsocks.ssserver.util.ChannelUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Client2ServerTcpHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private Channel clientServerChannel;
    private Channel serverRemoteChannel;
    private Bootstrap serverRemoteBootstrap;
    /**
     * clientBufList是必须的，因为连接必须是非阻塞的，在连上之前需要把数据存在clientByteBufList里面
     */
    private List<ByteBuf> clientByteBufList;

    public Client2ServerTcpHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext clientServerCtx, ByteBuf msg) throws Exception {
        if (this.clientServerChannel == null) {
            this.clientServerChannel = clientServerCtx.channel();
        }
        proxy(clientServerCtx, msg);
    }

    private void proxy(ChannelHandlerContext clientServerCtx, ByteBuf msg) throws Exception {

        // 如果是第一次代理，那么要创建Server -> Remote的通道
        if (serverRemoteChannel == null && serverRemoteBootstrap == null) {
            serverRemoteBootstrap = new Bootstrap();

            InetSocketAddress remoteInetSocketAddress = clientServerCtx.channel().attr(SSCommon.REMOTE_DES).get();

            serverRemoteBootstrap.group(clientServerCtx.channel().eventLoop())
                    .channel(NioSocketChannel.class)
                    // 连接到Remote 60s超时
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60 * 1000)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // 读缓冲区为32k
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(
                            new ChannelInitializer<Channel>() {
                                @Override
                                protected void initChannel(Channel ch) throws Exception {
                                    ch.pipeline()
                                            .addLast("timeout", new IdleStateHandler(0, 0, SSCommon.TCP_PROXY_IDEL_TIME, TimeUnit.SECONDS))
                                            .addLast("idleClose", new IdleCloseHandler())
                                            .addLast("inFlowStatistics", new InFlowStatisticsHandler(InOutSiteEnum.REMOTE_TO_SERVER.getDirection()))
                                            .addLast("reomte2server", new Remote2ServerHandler(clientServerCtx))
                                            .addLast("outFlowStatistics", new OutFlowStatisticsHandler(InOutSiteEnum.SERVER_TO_REMOTE.getDirection()));
                                }
                            }
                    );
            // 连接的时候不能阻塞，否则访问A网站阻塞了，导致B网站也不可访问了
            serverRemoteBootstrap.connect(remoteInetSocketAddress)
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (future.isSuccess()) {
                                serverRemoteChannel = future.channel();
                                flushAllClientBufList();
                            } else {
                                ChannelUtils.closeChannels(clientServerChannel, serverRemoteChannel);
                            }
                        }
                    });
        }

        if (serverRemoteChannel == null) {
            // 说明还没有连接上，那么先把数据保存在clientBufList中
            addToClientBufList(msg);
        } else {
            // 通过判断clientBufList决定是添加到clientBufList还是直接发送给Remote
            addToClientBufListOrSendToRemote(msg);
        }


    }

    private synchronized void flushAllClientBufList() {
        if (clientByteBufList != null) {
            ListIterator<ByteBuf> bufsIterator = clientByteBufList.listIterator();
            while (bufsIterator.hasNext()) {
                serverRemoteChannel.writeAndFlush(bufsIterator.next());
            }
            clientByteBufList = null;
        }
    }

    private synchronized void addToClientBufList(ByteBuf byteBuf) {
        if (clientByteBufList == null) {
            clientByteBufList = new ArrayList<>();
        }
        clientByteBufList.add(byteBuf.retain());
    }

    private synchronized void addToClientBufListOrSendToRemote(ByteBuf byteBuf) {
        if (clientByteBufList == null) {
            // 说明flushAllClientBufList已经调用过了，或者连接得特别快，总之clientByteBufList已经不需要了
            // Server将数据发送给Remote，对于这个channel而言这个是出口流量，可以在出口那里捕获它
            serverRemoteChannel.writeAndFlush(byteBuf.retain());
        } else {
            // 说明flushAllClientBufList还没有调用，为了保证流的顺序，只能将数据放在链表尾部
            clientByteBufList.add(byteBuf.retain());
        }
    }

    private synchronized void clearClientBufList() {
        if (clientByteBufList != null) {
            clientByteBufList.forEach(ReferenceCountUtil::release);
            clientByteBufList = null;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelUtils.closeChannels(clientServerChannel, serverRemoteChannel);
        clearClientBufList();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        log.warn("{}:{} -> {}:{} cause {}",
                remoteAddress.getHostString(), remoteAddress.getPort(),
                localAddress.getHostString(), localAddress.getPort(),
                cause.getMessage());
        ChannelUtils.closeChannels(clientServerChannel, serverRemoteChannel);
        clearClientBufList();
    }
}
