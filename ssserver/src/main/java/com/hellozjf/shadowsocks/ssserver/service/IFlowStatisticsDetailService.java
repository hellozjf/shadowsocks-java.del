package com.hellozjf.shadowsocks.ssserver.service;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.vo.ClientIpInfo;

import java.util.List;

/**
 * @author hellozjf
 */
public interface IFlowStatisticsDetailService {
    List<FlowStatisticsDetail> findByGmtCreateGtLtGroupByServerPortAndDirection(Long gmtCreateStart, Long gmtCreateEnd);
    List<ClientIpInfo> getAllClientIpInfoList();
    List<ClientIpInfo> getAllClientIpInfoListByServerPort(Integer serverPort);
}
