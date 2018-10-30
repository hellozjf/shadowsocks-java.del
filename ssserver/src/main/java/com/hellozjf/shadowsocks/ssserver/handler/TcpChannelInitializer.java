package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.handler.obfs.ObfsFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
//        log.debug("channel initializer");
        ctx.pipeline()
                //timeout
                .addLast("timeout", new SSTimeoutHandler(ctx));
        // obfs pugin
        List<ChannelHandler> obfsHandlers = ObfsFactory.getObfsHandler(obfs);
        if (obfsHandlers != null) {
            for (ChannelHandler obfsHandler : obfsHandlers) {
                ctx.pipeline().addLast(obfsHandler);
            }
        }
        //ss
        ctx.pipeline()
                //ss-in
                .addLast("ssCheckerReceive", new SSCheckerReceive(method, password))
                .addLast("ssCipherDecoder", new SSCipherDecoder())
                .addLast("ssProtocolDecoder", new SSProtocolDecoder())
                // 流量统计出口
                .addLast("outSiteFlowStatistics", new SSOutSiteFlowStatisticsHandler())
                // 流量统计入口
                .addLast("inSiteFlowStatistics", new SSInSiteFlowStatisticsHandler())
                //ss-proxy
                .addLast("ssTcpProxy", new SSTcpProxyHandler())
                //ss-out
                .addLast("ssCheckerSend", new SSCheckerSend())
                .addLast("ssCipherEncoder", new SSCipherEncoder())
                .addLast("ssProtocolEncoder", new SSProtocolEncoder());
    }
}

