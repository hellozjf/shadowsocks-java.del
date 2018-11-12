package com.hellozjf.shadowsocks.ssserver.service;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.vo.ClientIpInfoVO;

import java.util.List;

/**
 * @author hellozjf
 */
public interface IFlowStatisticsDetailService {
    List<FlowStatisticsDetail> findByGmtCreateGtLtGroupByServerPortAndDirection(Long gmtCreateStart, Long gmtCreateEnd);
    List<ClientIpInfoVO> getAllClientIpInfoList();
    List<ClientIpInfoVO> getAllClientIpInfoListByServerPort(Integer serverPort);
}
