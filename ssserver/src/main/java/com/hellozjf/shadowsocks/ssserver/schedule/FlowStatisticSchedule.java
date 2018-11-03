package com.hellozjf.shadowsocks.ssserver.schedule;

import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.repository.FlowSummaryRepository;
import com.hellozjf.shadowsocks.ssserver.service.IUserInfoService;
import com.hellozjf.shadowsocks.ssserver.util.CalendarUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author hellozjf
 */
@Component
@Slf4j
public class FlowStatisticSchedule {

    @Autowired
    private FlowStatisticsDetailRepository flowStatisticsDetailRepository;

    @Autowired
    private FlowSummaryRepository flowSummaryRepository;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private CalendarUtils calendarUtils;

    /**
     * 每小时初的时候计算每个用户日、周、月、季度、年的进出流量
     */
//    @Scheduled(cron = "0 0 * * * ?")
    public void updateFlowSummary() {

        // 获取各个时间，以便后续进行计算
        Calendar hourStartTime = calendarUtils.getHourStartTime();
        Calendar todayStartTime = calendarUtils.getTodayStartTime();
        Calendar weekStartTime = calendarUtils.getWeekStartTime();
        Calendar monthStartTime = calendarUtils.getMonthStartTime();
        Calendar quarterStartTime = calendarUtils.getQuarterStartTime();
        Calendar yearStartTime = calendarUtils.getYearStartTime();

        // 获取所有用户
        List<UserInfo> userInfoList = userInfoService.findAll();

        // 构造方向数组
        List<Integer> inList = Arrays.asList(InOutSiteEnum.CLIENT_TO_SERVER.getDirection(), InOutSiteEnum.REMOTE_TO_SERVER.getDirection());
        List<Integer> outList = Arrays.asList(InOutSiteEnum.SERVER_TO_CLIENT.getDirection(), InOutSiteEnum.SERVER_TO_REMOTE.getDirection());

        // 为每个用户计算他们的日、周、月、季度、年的进出流量
        for (UserInfo userInfo : userInfoList) {
            Long todayInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(todayStartTime.getTime(), hourStartTime.getTime(), inList, userInfo.getPort());
            Long todayOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(todayStartTime.getTime(), hourStartTime.getTime(), outList, userInfo.getPort());
            Long weekInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(weekStartTime.getTime(), hourStartTime.getTime(), inList, userInfo.getPort());
            Long weekOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(weekStartTime.getTime(), hourStartTime.getTime(), outList, userInfo.getPort());
            Long monthInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(monthStartTime.getTime(), hourStartTime.getTime(), inList, userInfo.getPort());
            Long monthOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(monthStartTime.getTime(), hourStartTime.getTime(), outList, userInfo.getPort());
            Long quarterInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(quarterStartTime.getTime(), hourStartTime.getTime(), inList, userInfo.getPort());
            Long quarterOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(quarterStartTime.getTime(), hourStartTime.getTime(), outList, userInfo.getPort());
            Long yearInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(yearStartTime.getTime(), hourStartTime.getTime(), inList, userInfo.getPort());
            Long yearOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(yearStartTime.getTime(), hourStartTime.getTime(), outList, userInfo.getPort());

            FlowSummary flowSummary = flowSummaryRepository.findByUserInfoId(userInfo.getId());
            if (flowSummary == null) {
                flowSummary = new FlowSummary();
                flowSummary.setUserInfoId(userInfo.getId());
            }
            flowSummary.setThisDayInFlowSummary(todayInFlowSummary);
            flowSummary.setThisDayOutFlowSummary(todayOutFlowSummary);
            flowSummary.setThisWeekInFlowSummary(weekInFlowSummary);
            flowSummary.setThisWeekOutFlowSummary(weekOutFlowSummary);
            flowSummary.setThisMonthInFlowSummary(monthInFlowSummary);
            flowSummary.setThisMonthOutFlowSummary(monthOutFlowSummary);
            flowSummary.setThisQuarterInFlowSummary(quarterInFlowSummary);
            flowSummary.setThisQuarterOutFlowSummary(quarterOutFlowSummary);
            flowSummary.setThisYearInFlowSummary(yearInFlowSummary);
            flowSummary.setThisYearOutFlowSummary(yearOutFlowSummary);
            flowSummaryRepository.save(flowSummary);
        }
    }
}
