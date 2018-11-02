package com.hellozjf.shadowsocks.ssserver.service;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;

/**
 * @author Jingfeng Zhou
 */
public interface IFlowSummerService {
    FlowSummary findByUserInfoId(Long userInfoId);
    FlowSummary findAll();
}
