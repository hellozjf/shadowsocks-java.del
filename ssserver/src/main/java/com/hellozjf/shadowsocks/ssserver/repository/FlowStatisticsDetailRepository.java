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

    FlowStatisticsDetail findTopByServerPortOrderByGmtCreateAsc(Integer serverPort);

    @Query(
            "select new FlowStatisticsDetail(serverPort, direction, sum(flowSize)) " +
                    "from FlowStatisticsDetail " +
                    "where ?1 <= gmtCreate and gmtCreate < ?2 " +
                    "group by serverPort, direction"
    )
    List<FlowStatisticsDetail> findByGmtCreateGtLtGroupByServerPortAndDirection(Long gmtCreateStart, Long gmtCreateEnd);

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
                    "where serverPort = ?1 " +
                    "   and direction = ?2 " +
                    "   and ?3 <= gmtCreate and gmtCreate < ?4"
    )
    Long findSumFlowSizeByGmtCreateAndDirectionAndServerPort(Integer serverPort, Integer direction, Long gmtCreateStart, Long gmtCreateEnd);

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
                    "where serverPort = ?1 " +
                    "   and direction in ?2 " +
                    "   and ?3 <= gmtCreate and gmtCreate < ?4"
    )
    Long findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(Integer serverPort, List<Integer> directions, Long gmtCreateStart, Long gmtCreateEnd);

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
                    "where direction in ?1 " +
                    "   and ?2 <= gmtCreate and gmtCreate < ?3"
    )
    Long findSumFlowSizeByDirectionsAndGmtCreate(List<Integer> directions, Long gmtCreateStart, Long gmtCreateEnd);
}
