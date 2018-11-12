package com.hellozjf.shadowsocks.ssserver.controller;

import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.exception.ShadowsocksException;
import com.hellozjf.shadowsocks.ssserver.service.IFlowSummaryService;
import com.hellozjf.shadowsocks.ssserver.service.IUserInfoService;
import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
@RestController
@RequestMapping("/slowFlowSummary")
@Api(tags = "精确流量汇总")
public class AccurateFlowSummaryController {

    @Autowired
    private IFlowSummaryService flowSummerService;

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 获取所有流量汇总信息
     * @param timeZone
     * @param dayOfWeek
     * @return
     */
    @GetMapping("/")
    public ResultVO<FlowSummary> getAllSummary(@RequestParam(name = "timeZone", defaultValue = "${custom.time-zone}") String timeZone,
                                  @RequestParam(name = "dayOfWeek", defaultValue = "${custom.day-of-week}") Integer dayOfWeek) {
        FlowSummary flowSummary = flowSummerService.findAll(timeZone, dayOfWeek);
        return ResultVO.success(flowSummary);
    }

    /**
     * 获取所有用户流量信息
     * @return
     */
    @GetMapping("/allUsers")
    public ResultVO<List<FlowSummary>> getAllUsers(@RequestParam(name = "timeZone", defaultValue = "${custom.time-zone}") String timeZone,
                           @RequestParam(name = "dayOfWeek", defaultValue = "${custom.day-of-week}") Integer dayOfWeek) {
        List<UserInfo> userInfoList = userInfoService.findAll();
        List<FlowSummary> flowSummaryList = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            FlowSummary flowSummary = flowSummerService.findByUserInfoId(timeZone, dayOfWeek, userInfo.getId());
            flowSummaryList.add(flowSummary);
        }
        return ResultVO.success(flowSummaryList);
    }

    /**
     * 获取单个用户的流量信息
     * @param timeZone
     * @param dayOfWeek
     * @param userInfoId
     * @return
     */
    @GetMapping("/{userInfoId}")
    public ResultVO<FlowSummary> get(@RequestParam(name = "timeZone", defaultValue = "${custom.time-zone}") String timeZone,
                        @RequestParam(name = "dayOfWeek", defaultValue = "${custom.day-of-week}") Integer dayOfWeek,
                        @PathVariable("userInfoId") String userInfoId) {
        FlowSummary flowSummary = flowSummerService.findByUserInfoId(timeZone, dayOfWeek, userInfoId);
        if (flowSummary == null) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_FIND_THIS_ID_OBJECT);
        } else {
            return ResultVO.success(flowSummary);
        }
    }
}
