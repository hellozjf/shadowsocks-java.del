package com.hellozjf.shadowsocks.ssserver.repository;

import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.util.CalendarUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Jingfeng Zhou
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FlowStatisticsDetailRepositoryTest {

    @Autowired
    private FlowStatisticsDetailRepository flowStatisticsDetailRepository;

    @Autowired
    private CalendarUtils calendarUtils;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    public void findSumFlowSizeByGmtCreateAndDirectionsAndServerPort() {
        Calendar hourStartTime = calendarUtils.getHourStartTime();
        Calendar todayStartTime = calendarUtils.getTodayStartTime();
        List<Integer> inDirections = Arrays.asList(InOutSiteEnum.CLIENT_TO_SERVER.getDirection(), InOutSiteEnum.REMOTE_TO_SERVER.getDirection());
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        log.debug("todayStartTime = {}", todayStartTime.getTime());
        log.debug("hourStartTime = {}", hourStartTime.getTime());
        log.debug("inDirections = {}", inDirections);
        log.debug("port = {}", userInfoList.get(0).getPort());
        Long flowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndDirectionsAndServerPort(todayStartTime.getTime(), hourStartTime.getTime(), inDirections, userInfoList.get(0).getPort());
        log.debug("flowSummary = {}", flowSummary);
    }
}