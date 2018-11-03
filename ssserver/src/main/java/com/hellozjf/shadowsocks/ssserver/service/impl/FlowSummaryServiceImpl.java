package com.hellozjf.shadowsocks.ssserver.service.impl;

import com.hellozjf.shadowsocks.ssserver.config.CustomConfig;
import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.exception.ShadowsocksException;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.repository.FlowSummaryRepository;
import com.hellozjf.shadowsocks.ssserver.repository.UserInfoRepository;
import com.hellozjf.shadowsocks.ssserver.service.IFlowSummerService;
import com.hellozjf.shadowsocks.ssserver.util.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.*;
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

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CalendarUtils calendarUtils;

    @Autowired
    private CustomConfig customConfig;

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
        OffsetDateTime currentTime = Instant.now().atOffset(ZoneOffset.of(timeZone));
        OffsetDateTime hourStartTime = currentTime.truncatedTo(ChronoUnit.HOURS);
        OffsetDateTime halfDayStartTime = currentTime.truncatedTo(ChronoUnit.HALF_DAYS);
        OffsetDateTime dayStartTime = currentTime.truncatedTo(ChronoUnit.DAYS);
        OffsetDateTime weekStartTime = dayStartTime.with(TemporalAdjusters.dayOfWeekInMonth(0, DayOfWeek.of(dayOfWeek)));
        OffsetDateTime monthStartTime = dayStartTime.withDayOfMonth(1);
        OffsetDateTime quarterStartTime;
        OffsetDateTime halfYearStartTime;
        Month month = monthStartTime.getMonth();
        if (month.equals(Month.JANUARY) || month.equals(Month.FEBRUARY) || month.equals(Month.MARCH)) {
            quarterStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JANUARY.getValue());
            halfYearStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JANUARY.getValue());
        } else if (month.equals(Month.APRIL) || month.equals(Month.MAY) || month.equals(Month.JUNE)) {
            quarterStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.APRIL.getValue());
            halfYearStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JANUARY.getValue());
        } else if (month.equals(Month.JULY) || month.equals(Month.AUGUST) || month.equals(Month.SEPTEMBER)) {
            quarterStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JULY.getValue());
            halfYearStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JULY.getValue());
        } else {
            quarterStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.OCTOBER.getValue());
            halfYearStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JULY.getValue());
        }
        OffsetDateTime yearStartTime = dayStartTime.withDayOfYear(1);
        OffsetDateTime beforeH1StartTime = currentTime.minusHours(1);
        OffsetDateTime beforeH12StartTime = currentTime.minusHours(12);
        OffsetDateTime beforeD1StartTime = currentTime.minusDays(1);
        OffsetDateTime beforeW1StartTime = currentTime.minusWeeks(1);
        OffsetDateTime beforeM1StartTime = currentTime.minusMonths(1);
        OffsetDateTime beforeM3StartTime = currentTime.minusMonths(3);
        OffsetDateTime beforeM6StartTime = currentTime.minusMonths(6);
        OffsetDateTime beforeY1StartTime = currentTime.minusYears(1);

        // 构造方向数组
        List<Integer> inList = Arrays.asList(InOutSiteEnum.CLIENT_TO_SERVER.getDirection(), InOutSiteEnum.REMOTE_TO_SERVER.getDirection());
        List<Integer> outList = Arrays.asList(InOutSiteEnum.SERVER_TO_CLIENT.getDirection(), InOutSiteEnum.SERVER_TO_REMOTE.getDirection());

        // 计算流量
        Long thisHourInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(hourStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long thisHourOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(hourStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long thisHalfDayInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(halfDayStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long thisHalfDayOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(halfDayStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long thisDayInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(dayStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long thisDayOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(dayStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long thisWeekInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(weekStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long thisWeekOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(weekStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long thisMonthInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(monthStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long thisMonthOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(monthStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long thisQuarterInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(quarterStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long thisQuarterOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(quarterStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long thisHalfYearInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(halfYearStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long thisHalfYearOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(halfYearStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long thisYearInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(yearStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long thisYearOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(yearStartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long h1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeH1StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long h1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeH1StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long h12InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeH12StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long h12OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeH12StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long d1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeD1StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long d1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeD1StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long w1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeW1StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long w1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeW1StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long m1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeM1StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long m1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeM1StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long m3InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeM3StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long m3OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeM3StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long m6InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeM6StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long m6OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeM6StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long y1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeY1StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long y1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(beforeY1StartTime.toInstant().toEpochMilli(), currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());
        Long totalInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(0L, currentTime.toInstant().toEpochMilli(), inList, userInfo.getPort());
        Long totalOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(0L, currentTime.toInstant().toEpochMilli(), outList, userInfo.getPort());

        // 填充流量信息
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
    public FlowSummary findAll() {
        // 取出所有的时间点，全部计算一遍，然后返回
        Calendar currentTime = calendarUtils.getCurrentTime();
        Calendar hourStartTime = calendarUtils.getHourStartTime();
        Calendar todayStartTime = calendarUtils.getTodayStartTime();
        Calendar weekStartTime = calendarUtils.getWeekStartTime();
        Calendar monthStartTime = calendarUtils.getMonthStartTime();
        Calendar quarterStartTime = calendarUtils.getQuarterStartTime();
        Calendar yearStartTime = calendarUtils.getYearStartTime();
        Calendar h24StartTime = calendarUtils.getH24StartTime();
        Calendar d7StartTime = calendarUtils.getD7StartTime();
        Calendar m1StartTime = calendarUtils.getM1StartTime();
        Calendar m3StartTime = calendarUtils.getM3StartTime();
        Calendar y1StartTime = calendarUtils.getY1StartTime();

        // 构造方向数组
        List<Integer> inList = Arrays.asList(InOutSiteEnum.CLIENT_TO_SERVER.getDirection(), InOutSiteEnum.REMOTE_TO_SERVER.getDirection());
        List<Integer> outList = Arrays.asList(InOutSiteEnum.SERVER_TO_CLIENT.getDirection(), InOutSiteEnum.SERVER_TO_REMOTE.getDirection());

        // 计算各个时间的流量
        Long todayInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(todayStartTime.getTime(), currentTime.getTime(), inList);
        Long todayOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(todayStartTime.getTime(), currentTime.getTime(), outList);
        Long weekInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(weekStartTime.getTime(), currentTime.getTime(), inList);
        Long weekOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(weekStartTime.getTime(), currentTime.getTime(), outList);
        Long monthInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(monthStartTime.getTime(), currentTime.getTime(), inList);
        Long monthOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(monthStartTime.getTime(), currentTime.getTime(), outList);
        Long quarterInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(quarterStartTime.getTime(), currentTime.getTime(), inList);
        Long quarterOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(quarterStartTime.getTime(), currentTime.getTime(), outList);
        Long yearInFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(yearStartTime.getTime(), currentTime.getTime(), inList);
        Long yearOutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(yearStartTime.getTime(), currentTime.getTime(), outList);
        Long h24InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(h24StartTime.getTime(), currentTime.getTime(), inList);
        Long h24OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(h24StartTime.getTime(), currentTime.getTime(), outList);
        Long d7InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(d7StartTime.getTime(), currentTime.getTime(), inList);
        Long d7OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(d7StartTime.getTime(), currentTime.getTime(), outList);
        Long m1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(m1StartTime.getTime(), currentTime.getTime(), inList);
        Long m1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(m1StartTime.getTime(), currentTime.getTime(), outList);
        Long m3InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(m3StartTime.getTime(), currentTime.getTime(), inList);
        Long m3OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(m3StartTime.getTime(), currentTime.getTime(), outList);
        Long y1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(y1StartTime.getTime(), currentTime.getTime(), inList);
        Long y1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirections(y1StartTime.getTime(), currentTime.getTime(), outList);

        // 返回flowSummary
        FlowSummary flowSummary = new FlowSummary();
        flowSummary.setUserInfoId(null);
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

    @Override
    public FlowSummary findAll(String timeZone, Integer dayOfWeek) {

    }

    private Long null2Zero(Long flowSize) {
        if (flowSize == null) {
            return 0L;
        } else {
            return flowSize;
        }
    }
}
