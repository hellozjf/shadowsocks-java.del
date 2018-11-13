package com.hellozjf.shadowsocks.ssserver.service.impl;

import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import com.hellozjf.shadowsocks.ssserver.constant.SSCommon;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.service.IFlowStatisticsDetailService;
import com.hellozjf.shadowsocks.ssserver.vo.ClientIpInfoVO;
import com.hellozjf.shadowsocks.ssserver.vo.ClientHostBrowseContentVO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author hellozjf
 */
@Service
@Slf4j
public class FlowStatisticsDetailServiceImpl implements IFlowStatisticsDetailService {

    @Autowired
    private FlowStatisticsDetailRepository flowStatisticsDetailRepository;

    @Override
    public List<FlowStatisticsDetail> findByGmtCreateGtLtGroupByServerPortAndDirection(Long gmtCreateStart, Long gmtCreateEnd) {
        synchronized (flowStatisticsDetailRepository) {
            return flowStatisticsDetailRepository.findByGmtCreateGtLtGroupByServerPortAndDirection(gmtCreateStart, gmtCreateEnd);
        }
    }

    @Override
    public Long findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(Integer serverPort, List<Integer> directions, Long gmtCreateStart, Long gmtCreateEnd) {
        synchronized (flowStatisticsDetailRepository) {
            return flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(serverPort, directions, gmtCreateStart, gmtCreateEnd);
        }
    }

    @Override
    public Long findSumFlowSizeByDirectionsAndGmtCreate(List<Integer> directions, Long gmtCreateStart, Long gmtCreateEnd) {
        synchronized (flowStatisticsDetailRepository) {
            return flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(directions, gmtCreateStart, gmtCreateEnd);
        }
    }

    @Override
    public FlowStatisticsDetail findTopByServerPortOrderByGmtCreateAsc(Integer serverPort) {
        synchronized (flowStatisticsDetailRepository) {
            return flowStatisticsDetailRepository.findTopByServerPortOrderByGmtCreateAsc(serverPort);
        }
    }

    @Override
    public List<ClientIpInfoVO> getAllClientIpInfoList() {
        synchronized (flowStatisticsDetailRepository) {
            List<String> allIpList;
            allIpList = flowStatisticsDetailRepository.findAllIpList();
            log.debug("allIpList = {}", allIpList);
            return getClientIpInfoListByIpList(allIpList);
        }
    }

    @Override
    public List<ClientIpInfoVO> getAllClientIpInfoListByServerPort(Integer serverPort) {
        synchronized (flowStatisticsDetailRepository) {
            List<String> allIpList;
            allIpList = flowStatisticsDetailRepository.findAllIpListByServerPort(serverPort);
            log.debug("allIpList = {}", allIpList);
            return getClientIpInfoListByIpList(allIpList);
        }
    }

    @Override
    public List<ClientHostBrowseContentVO> getClientHostBrowseContentList(String clientHost) {
        synchronized (flowStatisticsDetailRepository) {
            // 首先通过clientHost查出来它访问了哪些remoteAddress
            List<ClientHostBrowseContentVO> remoteAddressList = flowStatisticsDetailRepository.findAllClientHostBrowseContentByClientHost(clientHost);
            remoteAddressList.sort(new Comparator<ClientHostBrowseContentVO>() {
                @Override
                public int compare(ClientHostBrowseContentVO o1, ClientHostBrowseContentVO o2) {
                    return o1.getBrowseCount() >= o2.getBrowseCount() ? -1 : 1;
                }
            });
            for (ClientHostBrowseContentVO remoteAddress : remoteAddressList) {
                remoteAddress.setClientHost(clientHost);
            }
            return remoteAddressList;
        }
    }

    /**
     * 将数据保存到FlowStatisticsDetail表中
     *
     * @param ctx
     * @param byteBuf
     * @param direction
     * @throws Exception
     */
    @Override
    public void record(ChannelHandlerContext ctx,
                       ByteBuf byteBuf,
                       Integer direction) throws Exception {
        record(ctx.channel(), byteBuf, direction);
    }

    /**
     * 将数据保存到FlowStatisticsDetail表中
     *
     * @param channel
     * @param byteBuf
     * @param direction
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void record(Channel channel,
                       ByteBuf byteBuf,
                       Integer direction) throws Exception {

        synchronized (flowStatisticsDetailRepository) {
            FlowStatisticsDetail flowStatisticsDetail = new FlowStatisticsDetail();
            InetSocketAddress localAddress = channel.attr(SSCommon.SERVER).get();
            InetSocketAddress clientAddress = channel.attr(SSCommon.CLIENT).get();
            InetSocketAddress remoteAddress = channel.attr(SSCommon.REMOTE_DES).get();

            flowStatisticsDetail.setDirection(direction);
            flowStatisticsDetail.setFlowSize(Long.valueOf(byteBuf.readableBytes()));
            // TODO 先不要记录数据，否则我的vultr.hellozjf.com存储空间不够用了，以后再想办法存储数据
//        byte[] content = new byte[byteBuf.readableBytes()];
//        byteBuf.getBytes(byteBuf.readerIndex(), content);
//        flowStatisticsDetail.setContent(content);
            flowStatisticsDetail.setServerAddress(localAddress.getHostString());
            flowStatisticsDetail.setServerPort(localAddress.getPort());
            flowStatisticsDetail.setClientHost(clientAddress.getHostString());
            flowStatisticsDetail.setClientPort(clientAddress.getPort());
            flowStatisticsDetail.setRemoteAddress(remoteAddress.getHostString());
            flowStatisticsDetail.setRemotePort(remoteAddress.getPort());
            flowStatisticsDetailRepository.save(flowStatisticsDetail);

            InOutSiteEnum inOutSiteEnum = InOutSiteEnum.getByDirection(direction);
            if (inOutSiteEnum == null) {
                log.error("unknown direction {}", direction);
            } else {
                log.debug("{} size {}", inOutSiteEnum.getDetail(), byteBuf.readableBytes());
            }
        }
    }

    /**
     * 清除FlowStatisticsDetail表中的所有数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearAll() {
        synchronized (flowStatisticsDetailRepository) {
            flowStatisticsDetailRepository.deleteAll();
        }
    }

    /**
     * 清除FlowStatisticsDetail表与serverPort有关的所有数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearAll(Integer serverPort) {
        synchronized (flowStatisticsDetailRepository) {
            flowStatisticsDetailRepository.deleteAllByServerPort(serverPort);
        }
    }

    /**
     * 通过IP列表，查询http://clientHost-api.com/json/，获取IpInfo列表
     *
     * @param allIpList
     * @return
     */
    private List<ClientIpInfoVO> getClientIpInfoListByIpList(List<String> allIpList) {
        List<ClientIpInfoVO> allClientIpInfoList = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        for (String ip : allIpList) {
            ClientIpInfoVO clientIpInfo = restTemplate.getForObject("http://ip-api.com/json/" + ip, ClientIpInfoVO.class);
            allClientIpInfoList.add(clientIpInfo);
        }
        return allClientIpInfoList;
    }
}
