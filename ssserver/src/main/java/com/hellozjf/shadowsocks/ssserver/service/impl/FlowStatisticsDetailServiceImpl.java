package com.hellozjf.shadowsocks.ssserver.service.impl;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.service.IFlowStatisticsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hellozjf
 */
@Service
public class FlowStatisticsDetailServiceImpl implements IFlowStatisticsDetailService {

    @Autowired
    private FlowStatisticsDetailRepository flowStatisticsDetailRepository;

    @Override
    public List<FlowStatisticsDetail> findByGmtCreateGtLtGroupByServerPortAndDirection(Long gmtCreateStart, Long gmtCreateEnd) {
        return flowStatisticsDetailRepository.findByGmtCreateGtLtGroupByServerPortAndDirection(gmtCreateStart, gmtCreateEnd);
    }
}
