package com.hellozjf.shadowsocks.ssserver.service.impl;

import com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum;
import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.exception.ShadowsocksException;
import com.hellozjf.shadowsocks.ssserver.repository.FlowStatisticsDetailRepository;
import com.hellozjf.shadowsocks.ssserver.repository.FlowSummaryRepository;
import com.hellozjf.shadowsocks.ssserver.repository.UserInfoRepository;
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

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public FlowSummary findByUserInfoId(Long userInfoId) {
        FlowSummary flowSummary = flowSummaryRepository.findByUserInfoId(userInfoId);
        if (flowSummary == null) {
            // 说明没有该用户的统计信息，那么需要往数据库中新增一条记录
            flowSummary = new FlowSummary();
            flowSummary.setUserInfoId(userInfoId);
            flowSummaryRepository.save(flowSummary);
        }
        UserInfo userInfo = userInfoRepository.findById(userInfoId).orElse(null);
        if (userInfo == null) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_FIND_THIS_ID_OBJECT);
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
        // TODO 如果用户没有产生过流量，下面h24InFlowSummary...都会变成null，这是个bug！
        // 查出日/周/月/季度/年的出/入流量
        Long h24InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirectionsAndServerPort(h24Calendar.getTime(), currentCalendar.getTime(), inList, userInfo.getPort());
        Long h24OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirectionsAndServerPort(h24Calendar.getTime(), currentCalendar.getTime(), outList, userInfo.getPort());
        Long d7InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirectionsAndServerPort(d7Calendar.getTime(), currentCalendar.getTime(), inList, userInfo.getPort());
        Long d7OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirectionsAndServerPort(d7Calendar.getTime(), currentCalendar.getTime(), outList, userInfo.getPort());
        Long m1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirectionsAndServerPort(m1Calendar.getTime(), currentCalendar.getTime(), inList, userInfo.getPort());
        Long m1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirectionsAndServerPort(m1Calendar.getTime(), currentCalendar.getTime(), outList, userInfo.getPort());
        Long m3InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirectionsAndServerPort(m3Calendar.getTime(), currentCalendar.getTime(), inList, userInfo.getPort());
        Long m3OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirectionsAndServerPort(m3Calendar.getTime(), currentCalendar.getTime(), outList, userInfo.getPort());
        Long y1InFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirectionsAndServerPort(y1Calendar.getTime(), currentCalendar.getTime(), inList, userInfo.getPort());
        Long y1OutFlowSummary = flowStatisticsDetailRepository.findSumFlowSizeByGmtCreateAndAndDirectionsAndServerPort(y1Calendar.getTime(), currentCalendar.getTime(), outList, userInfo.getPort());
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

    @Override
    public FlowSummary findAll() {
        // TODO 查找所有的流量
        return null;
    }
}
