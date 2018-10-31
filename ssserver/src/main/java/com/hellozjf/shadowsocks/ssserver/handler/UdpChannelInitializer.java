package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
public class UdpChannelInitializer extends ChannelInitializer<NioDatagramChannel> {

    private String method;
    private String password;
    private String obfs;
    private String obfsparam;

    public UdpChannelInitializer(String method, String password, String obfs, String obfsparam) {
        this.method = method;
        this.password = password;
        this.obfs = obfs;
        this.obfsparam = obfsparam;
    }

    @Override
    protected void initChannel(NioDatagramChannel ctx) throws Exception {
        ctx.pipeline()
                // in
                .addLast("ssCheckerReceive", new SSCheckerReceive(method, password, true))
                .addLast("ssCipherDecoder", new SSCipherDecoder())
                .addLast("ssProtocolDecoder", new SSProtocolDecoder())
                // 流量统计入口
                .addLast("inSiteFlowStatistics", new InFlowStatisticsHandler(InOutSiteEnum.CLIENT_TO_SERVER.getDirection()))
                //proxy
                .addLast("ssUdpProxy", new Client2ServerUdpHandler())
                // 流量统计出口
                .addLast("outSiteFlowStatistics", new OutFlowStatisticsHandler(InOutSiteEnum.SERVER_TO_CLIENT.getDirection()))
                // out
                .addLast("ssCheckerSend", new SSCheckerSend())
                .addLast("ssCipherEncoder", new SSCipherEncoder())
                .addLast("ssProtocolEncoder", new SSProtocolEncoder());
    }
}
