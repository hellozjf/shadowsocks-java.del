package com.hellozjf.shadowsocks.ssserver.repository;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jingfeng Zhou
 */
public interface FlowStatisticsDetailRepository extends JpaRepository<FlowStatisticsDetail, Long> {
}
