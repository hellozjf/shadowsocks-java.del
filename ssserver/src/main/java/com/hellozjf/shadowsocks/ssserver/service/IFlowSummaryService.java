package com.hellozjf.shadowsocks.ssserver.service;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Jingfeng Zhou
 */
public interface IFlowSummaryService {
    FlowSummary findByUserInfoId(String userInfoId);
    FlowSummary findByUserInfoId(String timeZone, Integer dayOfWeek, String userInfoId);
    FlowSummary findAll();
    FlowSummary findAll(String timeZone, Integer dayOfWeek);
    List<FlowSummary> findByRecordMinuteTime(Long recordMinuteTime);
    Map<Long, FlowSummary> getTimeUserInfoMap(String userInfoId, List<Long> recordMinuteTime);
    void initFlowSummary();
    boolean isFlowSummaryInited();
    void calcOneMinuteFlowAndSave(Long timeMs, UserInfo needUserInfo);
    FlowSummary findRecentUserInfoByUserInfoId(String userInfoId);
    FlowSummary findAllRecent();
}
