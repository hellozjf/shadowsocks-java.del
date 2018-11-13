package com.hellozjf.shadowsocks.ssserver.service;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.vo.ClientIpInfoVO;
import com.hellozjf.shadowsocks.ssserver.vo.ClientHostBrowseContentVO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author hellozjf
 */
public interface IFlowStatisticsDetailService {
    List<FlowStatisticsDetail> findByGmtCreateGtLtGroupByServerPortAndDirection(Long gmtCreateStart, Long gmtCreateEnd);
    Long findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(Integer serverPort, List<Integer> directions, Long gmtCreateStart, Long gmtCreateEnd);
    Long findSumFlowSizeByDirectionsAndGmtCreate(List<Integer> directions, Long gmtCreateStart, Long gmtCreateEnd);
    FlowStatisticsDetail findTopByServerPortOrderByGmtCreateAsc(Integer serverPort);
    void record(ChannelHandlerContext ctx, ByteBuf byteBuf, Integer direction) throws Exception;
    void record(Channel channel, ByteBuf byteBuf, Integer direction) throws Exception;
    void clearAll();
    void clearAll(Integer serverPort);
    List<ClientIpInfoVO> getAllClientIpInfoList();
    List<ClientIpInfoVO> getAllClientIpInfoListByServerPort(Integer serverPort);
    List<ClientHostBrowseContentVO> getClientHostBrowseContentList(String ip);
}
