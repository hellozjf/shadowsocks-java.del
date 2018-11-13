package com.hellozjf.shadowsocks.ssserver.service;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

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
    FlowSummary findRecentUserInfoByUserInfoId(String userInfoId);
    FlowSummary findAllRecent();
    void updateFlowSummary();
    void clearAll();
    void clearAll(String userInfoId);
}
