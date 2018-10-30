package com.hellozjf.shadowsocks.ssserver.handler;

import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnums;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class SSInSiteFlowStatisticsHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private FlowStatisticsDetailRepository flowStatisticsDetailRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = ByteBufUtils.getByteBufFromMsg(msg);
        if (byteBuf != null) {
            // 统计入站流量到数据库中
            FlowStatisticsDetail flowStatisticsDetail = new FlowStatisticsDetail();
            InetSocketAddress address = (InetSocketAddress) ctx.channel().localAddress();
            flowStatisticsDetail.setPort(address.getPort());
            flowStatisticsDetail.setDirection(InOutSiteEnums.IN_SITE.getDirection());
            flowStatisticsDetail.setFlow(byteBuf.readableBytes());
            flowStatisticsDetail.setGmtCreate(new Date());
            flowStatisticsDetail.setGmtModified(new Date());
            flowStatisticsDetailRepository.save(flowStatisticsDetail);
            log.debug("flowStatisticsDetail={}", flowStatisticsDetail);
        }

        ctx.fireChannelRead(msg);
    }
}
