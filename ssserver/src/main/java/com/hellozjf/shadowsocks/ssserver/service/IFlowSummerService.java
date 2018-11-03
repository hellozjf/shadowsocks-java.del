package com.hellozjf.shadowsocks.ssserver.service;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;

/**
 * @author Jingfeng Zhou
 */
public interface IFlowSummerService {
    FlowSummary findByUserInfoId(String userInfoId);
    FlowSummary findByUserInfoId(String timeZone, Integer dayOfWeek, String userInfoId);
    FlowSummary findAll();
    FlowSummary findAll(String timeZone, Integer dayOfWeek);
}
