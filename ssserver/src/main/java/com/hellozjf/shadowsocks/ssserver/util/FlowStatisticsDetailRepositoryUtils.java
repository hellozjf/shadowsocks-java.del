package com.hellozjf.shadowsocks.ssserver.util;

import com.hellozjf.shadowsocks.ssserver.SpringContextUtil;
import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.exception.ShadowsocksException;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
public class FlowStatisticsDetailRepositoryUtils {
    public static void record(ChannelHandlerContext ctx,
                              ByteBuf byteBuf,
                              FlowStatisticsDetailRepository flowStatisticsDetailRepository,
                              Integer direction) throws Exception {
        record(ctx.channel(), byteBuf, flowStatisticsDetailRepository, direction);
    }

    public static void record(Channel channel,
                              ByteBuf byteBuf,
                              FlowStatisticsDetailRepository flowStatisticsDetailRepository,
                              Integer direction) throws Exception {
        FlowStatisticsDetail flowStatisticsDetail = new FlowStatisticsDetail();
        InetSocketAddress localAddress = channel.attr(SSCommon.SERVER).get();
        InetSocketAddress clientAddress = channel.attr(SSCommon.CLIENT).get();
        InetSocketAddress remoteAddress = channel.attr(SSCommon.REMOTE_DES).get();

        flowStatisticsDetail.setDirection(direction);
        flowStatisticsDetail.setFlowSize(byteBuf.readableBytes());
        byte[] content = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(byteBuf.readerIndex(), content);
        flowStatisticsDetail.setContent(content);
        flowStatisticsDetail.setServerAddress(localAddress.getHostString());
        flowStatisticsDetail.setServerPort(localAddress.getPort());
        flowStatisticsDetail.setClientHost(clientAddress.getHostString());
        flowStatisticsDetail.setClientPort(clientAddress.getPort());
        flowStatisticsDetail.setRemoteAddress(remoteAddress.getHostString());
        flowStatisticsDetail.setRemotePort(remoteAddress.getPort());
        flowStatisticsDetail.setGmtCreate(new Date());
        flowStatisticsDetail.setGmtModified(new Date());
        flowStatisticsDetailRepository.save(flowStatisticsDetail);
        log.debug("flowStatisticsDetail={}", flowStatisticsDetail);
    }

    public static FlowStatisticsDetailRepository getFlowStatisticsDetailRepository() {
        // 从SpringContext中获取FlowStatisticsDetailRepository
        Object bean = SpringContextUtil.getBean("flowStatisticsDetailRepository");
        if (bean instanceof FlowStatisticsDetailRepository) {
            return (FlowStatisticsDetailRepository) bean;
        } else {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_GET_BEAN);
        }
    }
}
