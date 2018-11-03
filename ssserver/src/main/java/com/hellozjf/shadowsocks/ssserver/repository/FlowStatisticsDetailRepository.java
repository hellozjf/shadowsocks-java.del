package com.hellozjf.shadowsocks.ssserver.repository;

import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author Jingfeng Zhou
 */
public interface FlowStatisticsDetailRepository extends JpaRepository<FlowStatisticsDetail, String> {

    /**
     * 通过开始时间，结束时间，方向来获取总流量
     * @param gmtCreateStart
     * @param gmtCreateEnd
     * @param direction
     * @return
     */
    @Query(
            "select sum(flowSize) " +
                    "from FlowStatisticsDetail " +
                    "where ?1 <= gmtCreate and gmtCreate < ?2 " +
                    "   and direction = ?3 " +
                    "   and serverPort = ?4"
    )
    Long findSumFlowSizeByGmtCreateAndDirectionAndServerPort(Long gmtCreateStart, Long gmtCreateEnd, Integer direction, Integer serverPort);

    /**
     * 通过开始时间，结束时间，多个方向来获取总流量
     * @param gmtCreateStart
     * @param gmtCreateEnd
     * @param directions
     * @return
     */
    @Query(
            "select sum(flowSize) " +
                    "from FlowStatisticsDetail " +
                    "where ?1 <= gmtCreate and gmtCreate < ?2 " +
                    "   and direction in ?3 " +
                    "   and serverPort = ?4"
    )
    Long findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(Long gmtCreateStart, Long gmtCreateEnd, List<Integer> directions, Integer serverPort);

    /**
     * 通过开始时间，结束时间，多个方向来获取总流量
     * @param gmtCreateStart
     * @param gmtCreateEnd
     * @param directions
     * @return
     */
    @Query(
            "select sum(flowSize) " +
                    "from FlowStatisticsDetail " +
                    "where ?1 <= gmtCreate and gmtCreate < ?2 " +
                    "   and direction in ?3"
    )
    Long findSumFlowSizeByGmtCreateAndDirections(Long gmtCreateStart, Long gmtCreateEnd, List<Integer> directions);
}
