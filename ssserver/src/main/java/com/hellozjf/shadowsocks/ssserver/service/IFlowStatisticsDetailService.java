package com.hellozjf.shadowsocks.ssserver.service;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.vo.ClientIpInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author hellozjf
 */
public interface IFlowStatisticsDetailService {
    List<FlowStatisticsDetail> findByGmtCreateGtLtGroupByServerPortAndDirection(Long gmtCreateStart, Long gmtCreateEnd);
    Long findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(Integer serverPort, List<Integer> directions, Long gmtCreateStart, Long gmtCreateEnd);
    Long findSumFlowSizeByDirectionsAndGmtCreate(List<Integer> directions, Long gmtCreateStart, Long gmtCreateEnd);
    FlowStatisticsDetail findTopByServerPortOrderByGmtCreateAsc(Integer serverPort);
    List<ClientIpInfo> getAllClientIpInfoList();
    List<ClientIpInfo> getAllClientIpInfoListByServerPort(Integer serverPort);
    void record(ChannelHandlerContext ctx, ByteBuf byteBuf, Integer direction) throws Exception;
    void record(Channel channel, ByteBuf byteBuf, Integer direction) throws Exception;
    void clearAll();
    void clearAll(Integer serverPort);
}
