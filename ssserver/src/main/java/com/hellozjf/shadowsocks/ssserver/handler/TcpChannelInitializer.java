package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import com.hellozjf.shadowsocks.ssserver.handler.obfs.ObfsFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
public class TcpChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    private String method;
    private String password;
    private String obfs;
    private String obfsparam;

    public TcpChannelInitializer(String method, String password, String obfs, String obfsparam) {
        this.method = method;
        this.password = password;
        this.obfs = obfs;
        this.obfsparam = obfsparam;
    }

    @Override
    protected void initChannel(NioSocketChannel ctx) {
        ctx.pipeline()
                //timeout
                .addLast("timeout", new IdleStateHandler(0, 0, SSCommon.TCP_PROXY_IDEL_TIME, TimeUnit.SECONDS))
                .addLast("idleClose", new IdleCloseHandler());
        // obfs pugin
        List<ChannelHandler> obfsHandlers = ObfsFactory.getObfsHandler(obfs);
        if (obfsHandlers != null) {
            for (ChannelHandler obfsHandler : obfsHandlers) {
                ctx.pipeline().addLast(obfsHandler);
            }
        }
        //ss
        ctx.pipeline()
                // ss-in
                .addLast("ssCheckerReceive", new SSCheckerReceive(method, password))
                .addLast("ssCipherDecoder", new SSCipherDecoder())
                .addLast("ssProtocolDecoder", new SSProtocolDecoder())
                // 流量统计入口，因为ss-in里面会解析出client/server/remote的IP和端口，所以只能在这里统计入口流量
                .addLast("inFlowStatistics", new InFlowStatisticsHandler(InOutSiteEnum.CLIENT_TO_SERVER.getDirection()))
                // ss-proxy，在这里Client->Server的数据会转发给Server->Remote
                .addLast("client2server", new Client2ServerHandler())
                // 流量统计出口
                .addLast("outFlowStatistics", new OutFlowStatisticsHandler(InOutSiteEnum.SERVER_TO_CLIENT.getDirection()))
                // ss-out
                .addLast("ssCheckerSend", new SSCheckerSend())
                .addLast("ssCipherEncoder", new SSCipherEncoder())
                .addLast("ssProtocolEncoder", new SSProtocolEncoder());
    }
}

