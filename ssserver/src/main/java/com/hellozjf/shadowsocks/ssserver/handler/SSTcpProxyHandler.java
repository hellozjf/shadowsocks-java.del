package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.util.FlowStatisticsDetailRepositoryUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SSTcpProxyHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private Channel clientChannel;
    private Channel remoteChannel;
    private Bootstrap proxyClient;
    private List<ByteBuf> clientBuffs;

    private FlowStatisticsDetailRepository flowStatisticsDetailRepository;

    public SSTcpProxyHandler() {
        // 手动注入FlowStatisticsDetailRepository
        flowStatisticsDetailRepository = FlowStatisticsDetailRepositoryUtils.getFlowStatisticsDetailRepository();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext clientCtx, ByteBuf msg) throws Exception {
//        log.debug("channel id {},readableBytes:{}", clientCtx.channel().id().toString(), msg.readableBytes());
        if (this.clientChannel == null) {
            this.clientChannel = clientCtx.channel();
        }
//        if (msg.readableBytes() == 0) return;
        proxy(clientCtx, msg);
    }

    private void proxy(ChannelHandlerContext clientCtx, ByteBuf msg) {
//        log.debug("channel id {},pc is null {},{}", clientCtx.channel().id().toString(), (remoteChannel == null), msg.readableBytes());
        if (remoteChannel == null && proxyClient == null) {
            proxyClient = new Bootstrap();//

            InetSocketAddress clientRecipient = clientCtx.channel().attr(SSCommon.REMOTE_DES).get();

            proxyClient.group(clientCtx.channel().eventLoop()).channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60 * 1000)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)// 读缓冲区为32k
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(
                            new ChannelInitializer<Channel>() {
                                @Override
                                protected void initChannel(Channel ch) throws Exception {
                                    ch.pipeline()
                                            .addLast("timeout", new IdleStateHandler(0, 0, SSCommon.TCP_PROXY_IDEL_TIME, TimeUnit.SECONDS) {
                                                @Override
                                                protected IdleStateEvent newIdleStateEvent(IdleState state, boolean first) {
//                                                    log.debug("{} state:{}", clientRecipient.toString(), state.toString());
                                                    proxyChannelClose();
                                                    return super.newIdleStateEvent(state, first);
                                                }
                                            })
                                            .addLast("tcpProxy", new SimpleChannelInboundHandler<ByteBuf>() {
                                                @Override
                                                protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                                    try {
                                                        // 这里是Remote -> Server
                                                        FlowStatisticsDetailRepositoryUtils.record(ctx,
                                                                msg,
                                                                flowStatisticsDetailRepository,
                                                                InOutSiteEnum.REMOTE_TO_SERVER.getDirection());
                                                    } catch (Exception e) {
                                                        log.error("e={}", e);
                                                    }
                                                    clientCtx.channel().writeAndFlush(msg.retain());
                                                }

                                                @Override
                                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                                    try {
                                                        // 将clientCtx里面的变量同样也设置到ctx里面
                                                        ctx.channel().attr(SSCommon.SERVER).set(clientCtx.channel().attr(SSCommon.SERVER).get());
                                                        ctx.channel().attr(SSCommon.CLIENT).set(clientCtx.channel().attr(SSCommon.CLIENT).get());
                                                        ctx.channel().attr(SSCommon.REMOTE_DES).set(clientCtx.channel().attr(SSCommon.REMOTE_DES).get());
                                                    } catch (Exception e) {
                                                        log.error("e={}", e);
                                                    }
                                                }

                                                @Override
                                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                                    super.channelInactive(ctx);
                                                    proxyChannelClose();
                                                }

                                                @Override
                                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//                                                    super.exceptionCaught(ctx, cause);
                                                    proxyChannelClose();
                                                }
                                            });
                                }
                            }
                    );
            try {
                proxyClient
                        .connect(clientRecipient)
                        .addListener((ChannelFutureListener) future -> {
                            try {
                                if (future.isSuccess()) {
//                                    log.debug("channel id {}, {}<->{}<->{} connect  {}", clientCtx.channel().id().toString(), clientCtx.channel().remoteAddress().toString(), future.channel().localAddress().toString(), clientRecipient.toString(), future.isSuccess());
                                    remoteChannel = future.channel();
                                    if (clientBuffs != null) {
                                        ListIterator<ByteBuf> bufsIterator = clientBuffs.listIterator();
                                        while (bufsIterator.hasNext()) {
                                            // 这里是Server -> Remote
                                            ByteBuf byteBuf = bufsIterator.next();
                                            try {
                                                FlowStatisticsDetailRepositoryUtils.record(remoteChannel,
                                                        byteBuf,
                                                        flowStatisticsDetailRepository,
                                                        InOutSiteEnum.SERVER_TO_REMOTE.getDirection());
                                            } catch (Exception e) {
                                                log.error("e={}", e);
                                            }
                                            remoteChannel.writeAndFlush(byteBuf);
                                        }
                                        clientBuffs = null;
                                    }
                                } else {
//                                    log.error("channel id {}, {}<->{} connect {},cause {}", clientCtx.channel().id().toString(), clientCtx.channel().remoteAddress().toString(), clientRecipient.toString(), future.isSuccess(), future.cause());
                                    proxyChannelClose();
                                }
                            } catch (Exception e) {
                                proxyChannelClose();
                            }
                        });
            } catch (Exception e) {
                log.error("connect internet error", e);
                proxyChannelClose();
                return;
            }
        }


        if (remoteChannel == null) {
            if (clientBuffs == null) {
                clientBuffs = new ArrayList<>();
            }
            clientBuffs.add(msg.retain());
//            log.debug("channel id {},add to client buff list", clientCtx.channel().id().toString());
        } else {
            if (clientBuffs == null) {
                // 这里是Server -> Remote
                try {
                    FlowStatisticsDetailRepositoryUtils.record(remoteChannel,
                            msg,
                            flowStatisticsDetailRepository,
                            InOutSiteEnum.SERVER_TO_REMOTE.getDirection());
                } catch (Exception e) {
                    log.error("e={}", e);
                }
                remoteChannel.writeAndFlush(msg.retain());
            } else {
                clientBuffs.add(msg.retain());
            }
//            log.debug("channel id {},remote channel write {}", clientCtx.channel().id().toString(), msg.readableBytes());
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        proxyChannelClose();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("SSTcpProxyHandler exceptionCaught = {}", cause.getMessage());
        proxyChannelClose();
    }

    private void proxyChannelClose() {
//        log.info("proxyChannelClose");
        try {
            if (clientBuffs != null) {
                clientBuffs.forEach(ReferenceCountUtil::release);
                clientBuffs = null;
            }
            if (remoteChannel != null) {
                remoteChannel.close();
                remoteChannel = null;
            }
            if (clientChannel != null) {
                clientChannel.close();
                clientChannel = null;
            }
        } catch (Exception e) {
//            log.error("close channel error", e);
        }
    }
}
