package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.SpringContextUtil;
import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.exception.ShadowsocksException;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.util.ByteBufUtils;
import com.hellozjf.shadowsocks.ssserver.util.FlowStatisticsDetailRepositoryUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
public class SSInSiteFlowStatisticsHandler extends ChannelInboundHandlerAdapter {

    private FlowStatisticsDetailRepository flowStatisticsDetailRepository;

    public SSInSiteFlowStatisticsHandler() {
        // 手动注入FlowStatisticsDetailRepository
        flowStatisticsDetailRepository = FlowStatisticsDetailRepositoryUtils.getFlowStatisticsDetailRepository();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        log.debug("channelRead");
        ByteBuf byteBuf = ByteBufUtils.getByteBufFromMsg(msg);
        if (byteBuf != null) {
            // 统计Client -> Server流量到数据库中
            FlowStatisticsDetailRepositoryUtils.record(ctx,
                    byteBuf,
                    flowStatisticsDetailRepository,
                    InOutSiteEnum.CLIENT_TO_SERVER.getDirection());
        }

        ctx.fireChannelRead(msg);
    }
}
