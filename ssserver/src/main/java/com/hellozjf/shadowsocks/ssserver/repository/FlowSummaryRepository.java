package com.hellozjf.shadowsocks.ssserver.repository;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jingfeng Zhou
 */
public interface FlowSummaryRepository extends JpaRepository<FlowSummary, String> {
    FlowSummary findByUserInfoId(String userInfoId);
}
