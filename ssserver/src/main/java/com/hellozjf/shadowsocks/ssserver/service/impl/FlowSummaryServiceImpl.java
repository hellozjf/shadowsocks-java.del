package com.hellozjf.shadowsocks.ssserver.service.impl;

import com.hellozjf.shadowsocks.ssserver.config.CustomConfig;
import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowStatisticsDetail;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.exception.ShadowsocksException;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.repository.FlowSummaryRepository;
import com.hellozjf.shadowsocks.ssserver.repository.UserInfoRepository;
import com.hellozjf.shadowsocks.ssserver.service.IFlowSummaryService;
import com.hellozjf.shadowsocks.ssserver.vo.AllOffsetDateTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Jingfeng Zhou
 */
@Service
public class FlowSummaryServiceImpl implements IFlowSummaryService {

    @Autowired
    private FlowStatisticsDetailRepository flowStatisticsDetailRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FlowSummaryRepository flowSummaryRepository;

    @Autowired
    private CustomConfig customConfig;

    private boolean bInited = false;

    @Override
    public FlowSummary findByUserInfoId(String userInfoId) {
        return findByUserInfoId(customConfig.getTimeZone(), customConfig.getDayOfWeek(), userInfoId);
    }

    @Override
    public FlowSummary findByUserInfoId(String timeZone, Integer dayOfWeek, String userInfoId) {
        FlowSummary flowSummary = new FlowSummary();
        UserInfo userInfo = userInfoRepository.findById(userInfoId).orElse(null);
        if (userInfo == null) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_FIND_THIS_ID_OBJECT);
        }
        // 首先获取需要统计的时间戳
        AllOffsetDateTimes allOffsetDateTimes = new AllOffsetDateTimes(timeZone, dayOfWeek);

        // 构造方向数组
        List<Integer> inList = Arrays.asList(InOutSiteEnum.CLIENT_TO_SERVER.getDirection(), InOutSiteEnum.REMOTE_TO_SERVER.getDirection());
        List<Integer> outList = Arrays.asList(InOutSiteEnum.SERVER_TO_CLIENT.getDirection(), InOutSiteEnum.SERVER_TO_REMOTE.getDirection());

        // 计算流量
        Long thisMinuteInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getMinuteStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisMinuteOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getMinuteStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHourInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getHourStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHourOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getHourStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHalfDayInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getHalfDayStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHalfDayOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getHalfDayStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisDayInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getDayStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisDayOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getDayStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisWeekInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getWeekStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisWeekOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getWeekStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisMonthInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getMonthStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisMonthOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getMonthStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisQuarterInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getQuarterStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisQuarterOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getQuarterStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHalfYearInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getHalfYearStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHalfYearOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getHalfYearStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisYearInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getYearStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisYearOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getYearStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long h1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getBeforeH1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long h1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getBeforeH1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long h12InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getBeforeH12StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long h12OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getBeforeH12StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long d1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getBeforeD1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long d1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getBeforeD1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long w1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getBeforeW1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long w1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getBeforeW1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getBeforeM1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getBeforeM1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m3InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getBeforeM3StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m3OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getBeforeM3StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m6InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getBeforeM6StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m6OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getBeforeM6StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long y1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, allOffsetDateTimes.getBeforeY1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long y1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, allOffsetDateTimes.getBeforeY1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long totalInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), inList, 0L, allOffsetDateTimes.getCurrentTimeMs());
        Long totalOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByServerPortAndDirectionsAndGmtCreate(userInfo.getPort(), outList, 0L, allOffsetDateTimes.getCurrentTimeMs());

        // 填充流量信息
        flowSummary.setThisMinuteInFlowSummary(null2Zero(thisMinuteInFlowSummary));
        flowSummary.setThisMinuteOutFlowSummary(null2Zero(thisMinuteOutFlowSummary));
        flowSummary.setThisHourInFlowSummary(null2Zero(thisHourInFlowSummary));
        flowSummary.setThisHourOutFlowSummary(null2Zero(thisHourOutFlowSummary));
        flowSummary.setThisHalfDayInFlowSummary(null2Zero(thisHalfDayInFlowSummary));
        flowSummary.setThisHalfDayOutFlowSummary(null2Zero(thisHalfDayOutFlowSummary));
        flowSummary.setThisDayInFlowSummary(null2Zero(thisDayInFlowSummary));
        flowSummary.setThisDayOutFlowSummary(null2Zero(thisDayOutFlowSummary));
        flowSummary.setThisWeekInFlowSummary(null2Zero(thisWeekInFlowSummary));
        flowSummary.setThisWeekOutFlowSummary(null2Zero(thisWeekOutFlowSummary));
        flowSummary.setThisMonthInFlowSummary(null2Zero(thisMonthInFlowSummary));
        flowSummary.setThisMonthOutFlowSummary(null2Zero(thisMonthOutFlowSummary));
        flowSummary.setThisQuarterInFlowSummary(null2Zero(thisQuarterInFlowSummary));
        flowSummary.setThisQuarterOutFlowSummary(null2Zero(thisQuarterOutFlowSummary));
        flowSummary.setThisHalfYearInFlowSummary(null2Zero(thisHalfYearInFlowSummary));
        flowSummary.setThisHalfYearOutFlowSummary(null2Zero(thisHalfYearOutFlowSummary));
        flowSummary.setThisYearInFlowSummary(null2Zero(thisYearInFlowSummary));
        flowSummary.setThisYearOutFlowSummary(null2Zero(thisYearOutFlowSummary));
        flowSummary.setH1InFlowSummary(null2Zero(h1InFlowSummary));
        flowSummary.setH1OutFlowSummary(null2Zero(h1OutFlowSummary));
        flowSummary.setH12InFlowSummary(null2Zero(h12InFlowSummary));
        flowSummary.setH12OutFlowSummary(null2Zero(h12OutFlowSummary));
        flowSummary.setD1InFlowSummary(null2Zero(d1InFlowSummary));
        flowSummary.setD1OutFlowSummary(null2Zero(d1OutFlowSummary));
        flowSummary.setW1InFlowSummary(null2Zero(w1InFlowSummary));
        flowSummary.setW1OutFlowSummary(null2Zero(w1OutFlowSummary));
        flowSummary.setM1InFlowSummary(null2Zero(m1InFlowSummary));
        flowSummary.setM1OutFlowSummary(null2Zero(m1OutFlowSummary));
        flowSummary.setM3InFlowSummary(null2Zero(m3InFlowSummary));
        flowSummary.setM3OutFlowSummary(null2Zero(m3OutFlowSummary));
        flowSummary.setM6InFlowSummary(null2Zero(m6InFlowSummary));
        flowSummary.setM6OutFlowSummary(null2Zero(m6OutFlowSummary));
        flowSummary.setY1InFlowSummary(null2Zero(y1InFlowSummary));
        flowSummary.setY1OutFlowSummary(null2Zero(y1OutFlowSummary));
        flowSummary.setTotalInFlowSummary(null2Zero(totalInFlowSummary));
        flowSummary.setTotalOutFlowSummary(null2Zero(totalOutFlowSummary));

        flowSummary.setUserInfoId(userInfoId);
        return flowSummary;
    }

    @Override
    public FlowSummary findAll() {
        return findAll(customConfig.getTimeZone(), customConfig.getDayOfWeek());
    }

    @Override
    public FlowSummary findAll(String timeZone, Integer dayOfWeek) {
        FlowSummary flowSummary = new FlowSummary();

        // 首先获取需要统计的时间戳
        AllOffsetDateTimes allOffsetDateTimes = new AllOffsetDateTimes(timeZone, dayOfWeek);

        // 构造方向数组
        List<Integer> inList = Arrays.asList(InOutSiteEnum.CLIENT_TO_SERVER.getDirection(), InOutSiteEnum.REMOTE_TO_SERVER.getDirection());
        List<Integer> outList = Arrays.asList(InOutSiteEnum.SERVER_TO_CLIENT.getDirection(), InOutSiteEnum.SERVER_TO_REMOTE.getDirection());

        // 计算流量
        Long thisMinuteInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getMinuteStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisMinuteOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getMinuteStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHourInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getHourStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHourOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getHourStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHalfDayInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getHalfDayStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHalfDayOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getHalfDayStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisDayInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getDayStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisDayOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getDayStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisWeekInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getWeekStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisWeekOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getWeekStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisMonthInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getMonthStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisMonthOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getMonthStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisQuarterInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getQuarterStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisQuarterOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getQuarterStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHalfYearInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getHalfYearStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisHalfYearOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getHalfYearStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisYearInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getYearStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long thisYearOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getYearStartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long h1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getBeforeH1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long h1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getBeforeH1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long h12InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getBeforeH12StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long h12OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getBeforeH12StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long d1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getBeforeD1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long d1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getBeforeD1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long w1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getBeforeW1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long w1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getBeforeW1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getBeforeM1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getBeforeM1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m3InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getBeforeM3StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m3OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getBeforeM3StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m6InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getBeforeM6StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long m6OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getBeforeM6StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long y1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, allOffsetDateTimes.getBeforeY1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long y1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, allOffsetDateTimes.getBeforeY1StartTimeMs(), allOffsetDateTimes.getCurrentTimeMs());
        Long totalInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(inList, 0L, allOffsetDateTimes.getCurrentTimeMs());
        Long totalOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByDirectionsAndGmtCreate(outList, 0L, allOffsetDateTimes.getCurrentTimeMs());

        // 填充流量信息
        flowSummary.setThisMinuteInFlowSummary(null2Zero(thisMinuteInFlowSummary));
        flowSummary.setThisMinuteOutFlowSummary(null2Zero(thisMinuteOutFlowSummary));
        flowSummary.setThisHourInFlowSummary(null2Zero(thisHourInFlowSummary));
        flowSummary.setThisHourOutFlowSummary(null2Zero(thisHourOutFlowSummary));
        flowSummary.setThisHalfDayInFlowSummary(null2Zero(thisHalfDayInFlowSummary));
        flowSummary.setThisHalfDayOutFlowSummary(null2Zero(thisHalfDayOutFlowSummary));
        flowSummary.setThisDayInFlowSummary(null2Zero(thisDayInFlowSummary));
        flowSummary.setThisDayOutFlowSummary(null2Zero(thisDayOutFlowSummary));
        flowSummary.setThisWeekInFlowSummary(null2Zero(thisWeekInFlowSummary));
        flowSummary.setThisWeekOutFlowSummary(null2Zero(thisWeekOutFlowSummary));
        flowSummary.setThisMonthInFlowSummary(null2Zero(thisMonthInFlowSummary));
        flowSummary.setThisMonthOutFlowSummary(null2Zero(thisMonthOutFlowSummary));
        flowSummary.setThisQuarterInFlowSummary(null2Zero(thisQuarterInFlowSummary));
        flowSummary.setThisQuarterOutFlowSummary(null2Zero(thisQuarterOutFlowSummary));
        flowSummary.setThisHalfYearInFlowSummary(null2Zero(thisHalfYearInFlowSummary));
        flowSummary.setThisHalfYearOutFlowSummary(null2Zero(thisHalfYearOutFlowSummary));
        flowSummary.setThisYearInFlowSummary(null2Zero(thisYearInFlowSummary));
        flowSummary.setThisYearOutFlowSummary(null2Zero(thisYearOutFlowSummary));
        flowSummary.setH1InFlowSummary(null2Zero(h1InFlowSummary));
        flowSummary.setH1OutFlowSummary(null2Zero(h1OutFlowSummary));
        flowSummary.setH12InFlowSummary(null2Zero(h12InFlowSummary));
        flowSummary.setH12OutFlowSummary(null2Zero(h12OutFlowSummary));
        flowSummary.setD1InFlowSummary(null2Zero(d1InFlowSummary));
        flowSummary.setD1OutFlowSummary(null2Zero(d1OutFlowSummary));
        flowSummary.setW1InFlowSummary(null2Zero(w1InFlowSummary));
        flowSummary.setW1OutFlowSummary(null2Zero(w1OutFlowSummary));
        flowSummary.setM1InFlowSummary(null2Zero(m1InFlowSummary));
        flowSummary.setM1OutFlowSummary(null2Zero(m1OutFlowSummary));
        flowSummary.setM3InFlowSummary(null2Zero(m3InFlowSummary));
        flowSummary.setM3OutFlowSummary(null2Zero(m3OutFlowSummary));
        flowSummary.setM6InFlowSummary(null2Zero(m6InFlowSummary));
        flowSummary.setM6OutFlowSummary(null2Zero(m6OutFlowSummary));
        flowSummary.setY1InFlowSummary(null2Zero(y1InFlowSummary));
        flowSummary.setY1OutFlowSummary(null2Zero(y1OutFlowSummary));
        flowSummary.setTotalInFlowSummary(null2Zero(totalInFlowSummary));
        flowSummary.setTotalOutFlowSummary(null2Zero(totalOutFlowSummary));
        return flowSummary;
    }

    @Override
    public List<FlowSummary> findByRecordMinuteTime(Long recordMinuteTime) {
        return flowSummaryRepository.findByRecordMinuteTime(recordMinuteTime);
    }

    @Override
    public Map<Long, FlowSummary> getTimeUserInfoMap(String userInfoId, List<Long> recordMinuteTimeList) {
        // FlowSummary也是降序排序
        List<FlowSummary> flowSummaryList = flowSummaryRepository.findByUserInfoIdAndRecordMinuteTimeInOrderByRecordMinuteTimeDesc(userInfoId, recordMinuteTimeList);
        Map<Long, FlowSummary> timeUserInfoMap = new HashMap<>();
        // recordMinuteTime降序排序
        recordMinuteTimeList.sort(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o2.compareTo(o1);
            }
        });
        // 如果数据库里有这个时间戳对应的FlowSummary，那就把它放到Map里面去
        for (int i = 0; i < flowSummaryList.size(); i++) {
            timeUserInfoMap.put(recordMinuteTimeList.get(i), flowSummaryList.get(i));
        }
        // 否则就放个空进去
        for (int i = flowSummaryList.size(); i < recordMinuteTimeList.size(); i++) {
            timeUserInfoMap.put(recordMinuteTimeList.get(i), new FlowSummary(recordMinuteTimeList.get(i), userInfoId));
        }
        return timeUserInfoMap;
    }

    @Override
    public void initFlowSummary() {

        // 取出所有用户，
        List<UserInfo> userInfoList = userInfoRepository.findAll();

        while (true) {
            // 获取当前的时间
            OffsetDateTime currentOffsetDateTime = Instant.now().atOffset(ZoneOffset.of(customConfig.getTimeZone()));

            // 取出这些用户最近一条FlowSummary
            for (UserInfo userInfo : userInfoList) {
                FlowSummary flowSummary = flowSummaryRepository.findTopByUserInfoIdOrderByRecordMinuteTimeDesc(userInfo.getId());
                if (flowSummary == null) {
                    // 从FlowStatistics表中取出最远的时间-2分钟
                    FlowStatisticsDetail flowStatisticsDetail = flowStatisticsDetailRepository.findTopByServerPortOrderByGmtCreateAsc(userInfo.getPort());
                    Long gmtCreate = flowStatisticsDetail.getGmtCreate();
                    OffsetDateTime offsetDateTime = Instant.ofEpochMilli(gmtCreate).atOffset(ZoneOffset.of(customConfig.getTimeZone()));
                    OffsetDateTime frontMinuteOffsetDateTime = offsetDateTime.truncatedTo(ChronoUnit.MINUTES).minusMinutes(2);
                    flowSummary = new FlowSummary(frontMinuteOffsetDateTime.toInstant().toEpochMilli(), userInfo.getId());
                    flowSummaryRepository.save(flowSummary);
                }
                // 补上从flowSummary开始（不统计）到最后一刻到现在为止所有的时间
                OffsetDateTime offsetDateTime = Instant.ofEpochMilli(flowSummary.getRecordMinuteTime()).atOffset(ZoneOffset.of(customConfig.getTimeZone()));
                for (offsetDateTime = offsetDateTime.plusMinutes(1); offsetDateTime.isBefore(currentOffsetDateTime); offsetDateTime = offsetDateTime.plusMinutes(1)) {
                    calcOneMinuteFlowAndSave(offsetDateTime.toInstant().toEpochMilli(), userInfo);
                }
            }

            OffsetDateTime newOffsetDateTime = Instant.now().atOffset(ZoneOffset.of(customConfig.getTimeZone()));
            if (currentOffsetDateTime.getMinute() == newOffsetDateTime.getMinute()) {
                // 说明FlowSummary表已经初始化完毕
                break;
            }
        }

        bInited = true;
    }

    @Override
    public boolean isFlowSummaryInited() {
        return bInited;
    }

    /**
     * 计算某个用户在timeMs的前一分钟的FlowSummary，并存入数据库
     * @param timeMs
     * @param needUserInfo 传null表示计算所有用户，否则只记录传入的用户
     */
    @Override
    public void calcOneMinuteFlowAndSave(Long timeMs, UserInfo needUserInfo) {
        // 获取所有的时间戳
        AllOffsetDateTimes allOffsetDateTimes = new AllOffsetDateTimes(timeMs, customConfig.getTimeZone(), customConfig.getDayOfWeek());
        // 获取上上分钟的FlowSummaryList
        List<FlowSummary> frontFrontMinuteFlowSummaryList = findByRecordMinuteTime(allOffsetDateTimes.getBeforeMin2MinuteStartTimeMs());
        // 获取所有用户，并初始化frontMinuteFlowSummaryList
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        // 统计所有用户上一分钟的进出流量
        List<FlowStatisticsDetail> frontMinuteFlowStatisticsDetailList = flowStatisticsDetailRepository.findByGmtCreateGtLtGroupByServerPortAndDirection(allOffsetDateTimes.getBeforeMin1MinuteStartTimeMs(), allOffsetDateTimes.getMinuteStartTimeMs());

        // 往数据库FlowSummary中插入上一分钟的数据
        OffsetDateTime frontMinuteStartTime = allOffsetDateTimes.getBeforeMin1MinuteStartTime();
        List<Long> frontTimeMsList = getFrontTimeMsList(allOffsetDateTimes);
        for (UserInfo userInfo : userInfoList) {
            if (needUserInfo != null && ! userInfo.equals(needUserInfo)) {
                // 如果只是求一个用户，那么就跳过其它用户
                continue;
            }
            FlowSummary fMinFlowSum = new FlowSummary(allOffsetDateTimes.getBeforeMin1MinuteStartTimeMs(), userInfo.getId());
            FlowSummary ffMinFlowSum = findFlowSummaryByUserInfo(frontFrontMinuteFlowSummaryList, userInfo, allOffsetDateTimes.getBeforeMin2MinuteStartTimeMs());
            FlowStatisticsDetail frontMinuteDirection1FlowStatisticDetail = findFlowStatisticsDetailByUserInfoAndDirection(frontMinuteFlowStatisticsDetailList, userInfo, InOutSiteEnum.CLIENT_TO_SERVER.getDirection());
            FlowStatisticsDetail frontMinuteDirection2FlowStatisticDetail = findFlowStatisticsDetailByUserInfoAndDirection(frontMinuteFlowStatisticsDetailList, userInfo, InOutSiteEnum.SERVER_TO_CLIENT.getDirection());
            FlowStatisticsDetail frontMinuteDirection3FlowStatisticDetail = findFlowStatisticsDetailByUserInfoAndDirection(frontMinuteFlowStatisticsDetailList, userInfo, InOutSiteEnum.SERVER_TO_REMOTE.getDirection());
            FlowStatisticsDetail frontMinuteDirection4FlowStatisticDetail = findFlowStatisticsDetailByUserInfoAndDirection(frontMinuteFlowStatisticsDetailList, userInfo, InOutSiteEnum.REMOTE_TO_SERVER.getDirection());
            Long frontMinuteInFlow = frontMinuteDirection1FlowStatisticDetail.getFlowSize() + frontMinuteDirection4FlowStatisticDetail.getFlowSize();
            Long frontMinuteOutFlow = frontMinuteDirection2FlowStatisticDetail.getFlowSize() + frontMinuteDirection3FlowStatisticDetail.getFlowSize();

            fMinFlowSum.setThisMinuteInFlowSummary(frontMinuteInFlow);
            fMinFlowSum.setThisMinuteOutFlowSummary(frontMinuteOutFlow);
            // 如果不是一个新的小时
            if (! frontMinuteStartTime.equals(allOffsetDateTimes.getHourStartTime())) {
                fMinFlowSum.setThisHourInFlowSummary(ffMinFlowSum.getThisHourInFlowSummary() + frontMinuteInFlow);
                fMinFlowSum.setThisHourOutFlowSummary(ffMinFlowSum.getThisHourOutFlowSummary() + frontMinuteOutFlow);
            }
            // 如果不是一个新的半天
            if (! frontMinuteStartTime.equals(allOffsetDateTimes.getHalfDayStartTime())) {
                fMinFlowSum.setThisHalfDayInFlowSummary(ffMinFlowSum.getThisHalfDayInFlowSummary() + frontMinuteInFlow);
                fMinFlowSum.setThisHalfDayOutFlowSummary(ffMinFlowSum.getThisHalfDayOutFlowSummary() + frontMinuteOutFlow);
            }
            // 如果不是一个新天
            if (! frontMinuteStartTime.equals(allOffsetDateTimes.getDayStartTime())) {
                fMinFlowSum.setThisDayInFlowSummary(ffMinFlowSum.getThisDayInFlowSummary() + frontMinuteInFlow);
                fMinFlowSum.setThisDayOutFlowSummary(ffMinFlowSum.getThisDayOutFlowSummary() + frontMinuteOutFlow);
            }
            // 如果不是一个新星期
            if (! frontMinuteStartTime.equals(allOffsetDateTimes.getWeekStartTime())) {
                fMinFlowSum.setThisWeekInFlowSummary(ffMinFlowSum.getThisWeekInFlowSummary() + frontMinuteInFlow);
                fMinFlowSum.setThisWeekOutFlowSummary(ffMinFlowSum.getThisWeekOutFlowSummary() + frontMinuteOutFlow);
            }
            // 如果不是一个新月
            if (! frontMinuteStartTime.equals(allOffsetDateTimes.getMonthStartTime())) {
                fMinFlowSum.setThisMonthInFlowSummary(ffMinFlowSum.getThisMonthInFlowSummary() + frontMinuteInFlow);
                fMinFlowSum.setThisMonthOutFlowSummary(ffMinFlowSum.getThisMonthOutFlowSummary() + frontMinuteOutFlow);
            }
            // 如果不是一个新季度
            if (! frontMinuteStartTime.equals(allOffsetDateTimes.getQuarterStartTime())) {
                fMinFlowSum.setThisQuarterInFlowSummary(ffMinFlowSum.getThisQuarterInFlowSummary() + frontMinuteInFlow);
                fMinFlowSum.setThisQuarterOutFlowSummary(ffMinFlowSum.getThisQuarterOutFlowSummary() + frontMinuteOutFlow);
            }
            // 如果不是一个新半年
            if (! frontMinuteStartTime.equals(allOffsetDateTimes.getHalfYearStartTime())) {
                fMinFlowSum.setThisHalfYearInFlowSummary(ffMinFlowSum.getThisHalfYearInFlowSummary() + frontMinuteInFlow);
                fMinFlowSum.setThisHalfYearOutFlowSummary(ffMinFlowSum.getThisHalfYearOutFlowSummary() + frontMinuteOutFlow);
            }
            // 如果不是一个新年
            if (! frontMinuteStartTime.equals(allOffsetDateTimes.getYearStartTime())) {
                fMinFlowSum.setThisYearInFlowSummary(ffMinFlowSum.getThisYearInFlowSummary() + frontMinuteInFlow);
                fMinFlowSum.setThisYearOutFlowSummary(ffMinFlowSum.getThisYearOutFlowSummary() + frontMinuteOutFlow);
            }
            fMinFlowSum.setTotalInFlowSummary(ffMinFlowSum.getTotalInFlowSummary() + frontMinuteInFlow);
            fMinFlowSum.setTotalOutFlowSummary(ffMinFlowSum.getTotalOutFlowSummary() + frontMinuteOutFlow);

            Map<Long, FlowSummary> timeFlowSummaryMap = getTimeUserInfoMap(userInfo.getId(), frontTimeMsList);
            // 近一小时流量
            fMinFlowSum.setH1InFlowSummary(ffMinFlowSum.getH1InFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeH1Min1MinuteStartTimeMs()).getThisMinuteInFlowSummary() +
                    frontMinuteInFlow);
            fMinFlowSum.setH1OutFlowSummary(ffMinFlowSum.getH1OutFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeH1Min1MinuteStartTimeMs()).getThisMinuteOutFlowSummary() +
                    frontMinuteOutFlow);
            // 近半天流量
            fMinFlowSum.setH12InFlowSummary(ffMinFlowSum.getH12InFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeH12Min1MinuteStartTimeMs()).getThisMinuteInFlowSummary() +
                    frontMinuteInFlow);
            fMinFlowSum.setH12OutFlowSummary(ffMinFlowSum.getH12OutFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeH12Min1MinuteStartTimeMs()).getThisMinuteOutFlowSummary() +
                    frontMinuteOutFlow);
            // 近一天流量
            fMinFlowSum.setD1InFlowSummary(ffMinFlowSum.getD1InFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeD1Min1MinuteStartTimeMs()).getThisMinuteInFlowSummary() +
                    frontMinuteInFlow);
            fMinFlowSum.setD1OutFlowSummary(ffMinFlowSum.getD1OutFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeD1Min1MinuteStartTimeMs()).getThisMinuteOutFlowSummary() +
                    frontMinuteOutFlow);
            // 近一周流量
            fMinFlowSum.setW1InFlowSummary(ffMinFlowSum.getW1InFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeW1Min1MinuteStartTimeMs()).getThisMinuteInFlowSummary() +
                    frontMinuteInFlow);
            fMinFlowSum.setW1OutFlowSummary(ffMinFlowSum.getW1OutFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeW1Min1MinuteStartTimeMs()).getThisMinuteOutFlowSummary() +
                    frontMinuteOutFlow);
            // 近一月流量
            fMinFlowSum.setM1InFlowSummary(ffMinFlowSum.getM1InFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeM1Min1MinuteStartTimeMs()).getThisMinuteInFlowSummary() +
                    frontMinuteInFlow);
            fMinFlowSum.setM1OutFlowSummary(ffMinFlowSum.getM1OutFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeM1Min1MinuteStartTimeMs()).getThisMinuteOutFlowSummary() +
                    frontMinuteOutFlow);
            // 近三月流量
            fMinFlowSum.setM3InFlowSummary(ffMinFlowSum.getM3InFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeM3Min1MinuteStartTimeMs()).getThisMinuteInFlowSummary() +
                    frontMinuteInFlow);
            fMinFlowSum.setM3OutFlowSummary(ffMinFlowSum.getM3OutFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeM3Min1MinuteStartTimeMs()).getThisMinuteOutFlowSummary() +
                    frontMinuteOutFlow);
            // 近半年流量
            fMinFlowSum.setM6InFlowSummary(ffMinFlowSum.getM6InFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeM6Min1MinuteStartTimeMs()).getThisMinuteInFlowSummary() +
                    frontMinuteInFlow);
            fMinFlowSum.setM6OutFlowSummary(ffMinFlowSum.getM6OutFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeM6Min1MinuteStartTimeMs()).getThisMinuteOutFlowSummary() +
                    frontMinuteOutFlow);
            // 近一年流量
            fMinFlowSum.setY1InFlowSummary(ffMinFlowSum.getY1InFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeY1Min1MinuteStartTimeMs()).getThisMinuteInFlowSummary() +
                    frontMinuteInFlow);
            fMinFlowSum.setY1OutFlowSummary(ffMinFlowSum.getY1OutFlowSummary() -
                    timeFlowSummaryMap.get(allOffsetDateTimes.getBeforeY1Min1MinuteStartTimeMs()).getThisMinuteOutFlowSummary() +
                    frontMinuteOutFlow);

            // 保存
            flowSummaryRepository.save(fMinFlowSum);
        }
    }

    @Override
    public FlowSummary findRecentUserInfoByUserInfoId(String userInfoId) {
        return flowSummaryRepository.findTopByUserInfoIdOrderByRecordMinuteTimeDesc(userInfoId);
    }

    @Override
    public FlowSummary findAllRecent() {
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        FlowSummary sumFlowSummary = null;
        for (UserInfo userInfo : userInfoList) {
            FlowSummary flowSummary = flowSummaryRepository.findTopByUserInfoIdOrderByRecordMinuteTimeDesc(userInfo.getId());
            if (sumFlowSummary == null) {
                sumFlowSummary = flowSummary;
            } else {
                sumFlowSummary = sumFlowSummary.add(flowSummary);
            }
        }
        if (sumFlowSummary != null) {
            sumFlowSummary.setUserInfoId(null);
        }
        return sumFlowSummary;
    }

    /**
     * 获取计算近1小时、12小时、1天、1周、1月、3月、6月、1年流量所需要的时间戳
     * @param allOffsetDateTimes
     * @return
     */
    private List<Long> getFrontTimeMsList(AllOffsetDateTimes allOffsetDateTimes) {
        List<Long> frontTimeMsList = new ArrayList<>();
        frontTimeMsList.add(allOffsetDateTimes.getBeforeH1Min1MinuteStartTimeMs());
        frontTimeMsList.add(allOffsetDateTimes.getBeforeH12Min1MinuteStartTimeMs());
        frontTimeMsList.add(allOffsetDateTimes.getBeforeD1Min1MinuteStartTimeMs());
        frontTimeMsList.add(allOffsetDateTimes.getBeforeW1Min1MinuteStartTimeMs());
        frontTimeMsList.add(allOffsetDateTimes.getBeforeM1Min1MinuteStartTimeMs());
        frontTimeMsList.add(allOffsetDateTimes.getBeforeM3Min1MinuteStartTimeMs());
        frontTimeMsList.add(allOffsetDateTimes.getBeforeM6Min1MinuteStartTimeMs());
        frontTimeMsList.add(allOffsetDateTimes.getBeforeY1Min1MinuteStartTimeMs());
        return frontTimeMsList;
    }

    /**
     * 从flowSummaryList中，根据用户ID获取对应的FlowSummary
     * @param flowSummaryList
     * @param userInfo
     * @param frontFrontMinuteStartTime
     * @return
     */
    private FlowSummary findFlowSummaryByUserInfo(List<FlowSummary> flowSummaryList, UserInfo userInfo, Long frontFrontMinuteStartTime) {
        for (FlowSummary flowSummary : flowSummaryList) {
            if (flowSummary.getUserInfoId().equals(userInfo.getId())) {
                return flowSummary;
            }
        }
        return new FlowSummary(frontFrontMinuteStartTime, userInfo.getId());
    }

    /**
     * 从FlowStatisticsDetailList中，根据用户的端口和方向获取对应的FlowStatisticsDetail
     * @param frontMinuteFlowStatisticsDetailList
     * @param userInfo
     * @param direction
     * @return
     */
    private FlowStatisticsDetail findFlowStatisticsDetailByUserInfoAndDirection(List<FlowStatisticsDetail> frontMinuteFlowStatisticsDetailList, UserInfo userInfo, Integer direction) {
        for (FlowStatisticsDetail flowSummary : frontMinuteFlowStatisticsDetailList) {
            if (flowSummary.getServerPort().equals(userInfo.getPort()) &&
                    flowSummary.getDirection().equals(direction)) {
                return flowSummary;
            }
        }
        return new FlowStatisticsDetail(userInfo.getPort(), direction, 0L);
    }

    private Long null2Zero(Long flowSize) {
        if (flowSize == null) {
            return 0L;
        } else {
            return flowSize;
        }
    }
}
