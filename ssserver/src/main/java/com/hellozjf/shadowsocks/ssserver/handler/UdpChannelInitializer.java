package com.hellozjf.shadowsocks.ssserver.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class UdpChannelInitializer extends ChannelInitializer<NioDatagramChannel> {

    @Autowired
    private SSInSiteFlowStatisticsHandler ssInSiteFlowStatisticsHandler;

    @Autowired
    private SSOutSiteFlowStatisticsHandler ssOutSiteFlowStatisticsHandler;

    private String method;
    private String password;
    private String obfs;
    private String obfsparam;

    public UdpChannelInitializer init(String method, String password, String obfs, String obfsparam) {
        this.method = method;
        this.password = password;
        this.obfs = obfs;
        this.obfsparam = obfsparam;
        return this;
    }


    @Override
    protected void initChannel(NioDatagramChannel ctx) throws Exception {
        ctx.pipeline()
                // 流量统计
                .addLast("inSiteFlowStatistics", ssInSiteFlowStatisticsHandler)
                .addLast("outSiteFlowStatistics", ssOutSiteFlowStatisticsHandler)
                // in
                .addLast("ssCheckerReceive", new SSCheckerReceive(method, password, true))
                .addLast("ssCipherDecoder", new SSCipherDecoder())
                .addLast("ssProtocolDecoder", new SSProtocolDecoder())
                //proxy
                .addLast("ssUdpProxy", new SSUdpProxyHandler())
                // out
                .addLast("ssCheckerSend", new SSCheckerSend())
                .addLast("ssCipherEncoder", new SSCipherEncoder())
                .addLast("ssProtocolEncoder", new SSProtocolEncoder())
        ;
    }
}
