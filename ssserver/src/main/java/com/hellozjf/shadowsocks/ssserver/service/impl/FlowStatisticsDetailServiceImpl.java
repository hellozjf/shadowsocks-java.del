package com.hellozjf.shadowsocks.ssserver.service.impl;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.service.IFlowStatisticsDetailService;
import com.hellozjf.shadowsocks.ssserver.vo.ClientIpInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
        return flowStatisticsDetailRepository.findByGmtCreateGtLtGroupByServerPortAndDirection(gmtCreateStart, gmtCreateEnd);
    }

    @Override
    public List<ClientIpInfo> getAllClientIpInfoList() {
        List<String> allIpList = flowStatisticsDetailRepository.findAllIpList();
        log.debug("allIpList = {}", allIpList);
        return getClientIpInfoListByIpList(allIpList);
    }

    @Override
    public List<ClientIpInfo> getAllClientIpInfoListByServerPort(Integer serverPort) {
        List<String> allIpList = flowStatisticsDetailRepository.findAllIpListByServerPort(serverPort);
        log.debug("allIpList = {}", allIpList);
        return getClientIpInfoListByIpList(allIpList);
    }

    /**
     * 通过IP列表，查询http://ip-api.com/json/，获取IpInfo列表
     * @param allIpList
     * @return
     */
    private List<ClientIpInfo> getClientIpInfoListByIpList(List<String> allIpList) {
        List<ClientIpInfo> allClientIpInfoList = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        for (String ip : allIpList) {
            ClientIpInfo clientIpInfo = restTemplate.getForObject("http://ip-api.com/json/" + ip, ClientIpInfo.class);
            allClientIpInfoList.add(clientIpInfo);
        }
        return allClientIpInfoList;
    }
}
