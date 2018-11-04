package com.hellozjf.shadowsocks.ssserver.repository;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jingfeng Zhou
 */
public interface FlowSummaryRepository extends JpaRepository<FlowSummary, String> {
    List<FlowSummary> findByRecordMinuteTime(Long recordMinuteTime);
    List<FlowSummary> findByUserInfoIdAndRecordMinuteTimeInOrderByRecordMinuteTimeDesc(String userInfoId, List<Long> recordMinuteTimeList);
    FlowSummary findTopByUserInfoIdOrderByRecordMinuteTimeDesc(String userInfoId);
}
