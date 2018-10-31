package com.hellozjf.shadowsocks.ssserver.service.impl;

import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.repository.FlowSummaryRepository;
import com.hellozjf.shadowsocks.ssserver.service.IFlowSummerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author Jingfeng Zhou
 */
@Service
public class FlowSummaryServiceImpl implements IFlowSummerService {

    @Autowired
    private FlowStatisticsDetailRepository flowStatisticsDetailRepository;

    @Autowired
    private FlowSummaryRepository flowSummaryRepository;

    @Override
    public FlowSummary findByUserInfoId(Long userInfoId) {
        FlowSummary flowSummary = flowSummaryRepository.findByUserInfoId(userInfoId);
        if (flowSummary == null) {
            // 说明没有该用户的统计信息，那么需要往数据库中新增一条记录
            flowSummary = new FlowSummary();
            flowSummary.setUserInfoId(userInfoId);
            flowSummaryRepository.save(flowSummary);
        }
        // 填充和当前时间有关的几个流量信息
        Calendar currentCalendar = Calendar.getInstance();
        Calendar h24Calendar = (Calendar) currentCalendar.clone();
        h24Calendar.set(Calendar.DAY_OF_YEAR, -1);
        Calendar d7Calendar = (Calendar) currentCalendar.clone();
        d7Calendar.set(Calendar.WEEK_OF_YEAR, -1);
        Calendar m1Calendar = (Calendar) currentCalendar.clone();
        m1Calendar.set(Calendar.MONTH, -1);
        Calendar m3Calendar = (Calendar) currentCalendar.clone();
        m3Calendar.set(Calendar.MONTH, -3);
        Calendar y1Calendar = (Calendar) currentCalendar.clone();
        y1Calendar.set(Calendar.YEAR, -1);
        // 构造方向数组
        List<Integer> inList = Arrays.asList(InOutSiteEnum.CLIENT_TO_SERVER.getDirection(), InOutSiteEnum.REMOTE_TO_SERVER.getDirection());
        List<Integer> outList = Arrays.asList(InOutSiteEnum.SERVER_TO_CLIENT.getDirection(), InOutSiteEnum.SERVER_TO_REMOTE.getDirection());
        // 查出日/周/月/季度/年的出/入流量
        Long h24InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirections(h24Calendar.getTime(), currentCalendar.getTime(), inList);
        Long h24OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirections(h24Calendar.getTime(), currentCalendar.getTime(), outList);
        Long d7InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirections(d7Calendar.getTime(), currentCalendar.getTime(), inList);
        Long d7OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirections(d7Calendar.getTime(), currentCalendar.getTime(), outList);
        Long m1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirections(m1Calendar.getTime(), currentCalendar.getTime(), inList);
        Long m1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirections(m1Calendar.getTime(), currentCalendar.getTime(), outList);
        Long m3InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirections(m3Calendar.getTime(), currentCalendar.getTime(), inList);
        Long m3OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirections(m3Calendar.getTime(), currentCalendar.getTime(), outList);
        Long y1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirections(y1Calendar.getTime(), currentCalendar.getTime(), inList);
        Long y1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirections(y1Calendar.getTime(), currentCalendar.getTime(), outList);
        // 填充和当前时间有关的几个流量信息
        flowSummary.setH24InFlowSummary(h24InFlowSummary);
        flowSummary.setH24OutFlowSummary(h24OutFlowSummary);
        flowSummary.setD7InFlowSummary(d7InFlowSummary);
        flowSummary.setD7OutFlowSummary(d7OutFlowSummary);
        flowSummary.setM1InFlowSummary(m1InFlowSummary);
        flowSummary.setM1OutFlowSummary(m1OutFlowSummary);
        flowSummary.setM3InFlowSummary(m3InFlowSummary);
        flowSummary.setM3OutFlowSummary(m3OutFlowSummary);
        flowSummary.setY1InFlowSummary(y1InFlowSummary);
        flowSummary.setY1OutFlowSummary(y1OutFlowSummary);
        return flowSummary;
    }
}
