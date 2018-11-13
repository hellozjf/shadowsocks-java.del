package com.hellozjf.shadowsocks.ssserver.controller;

import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.exception.ShadowsocksException;
import com.hellozjf.shadowsocks.ssserver.service.IFlowSummaryService;
import com.hellozjf.shadowsocks.ssserver.service.IUserInfoService;
import com.hellozjf.shadowsocks.ssserver.util.ResultUtils;
import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
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
@RequestMapping("/flowSummary")
public class FlowSummaryController {

    @Autowired
    private IFlowSummaryService flowSummerService;

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * TODO 这个接口有问题！返回的全是null
     * 获取所有流量汇总信息
     * @return
     */
    @GetMapping("/summary")
    public ResultVO getAllSummary() {
        FlowSummary flowSummary = flowSummerService.findAllRecent();
        return ResultUtils.success(flowSummary);
    }

    /**
     * 获取所有用户流量信息
     * @return
     */
    @GetMapping("/")
    public ResultVO getAllUsers() {
        List<UserInfo> userInfoList = userInfoService.findAll();
        List<FlowSummary> flowSummaryList = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            FlowSummary flowSummary = flowSummerService.findRecentUserInfoByUserInfoId(userInfo.getId());
            flowSummaryList.add(flowSummary);
        }
        return ResultUtils.success(flowSummaryList);
    }

    /**
     * 获取单个用户的流量信息
     * @param userInfoId
     * @return
     */
    @GetMapping("/{userInfoId}")
    public ResultVO get(@PathVariable("userInfoId") String userInfoId) {
        FlowSummary flowSummary = flowSummerService.findRecentUserInfoByUserInfoId(userInfoId);
        if (flowSummary == null) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_FIND_THIS_ID_OBJECT);
        } else {
            return ResultUtils.success(flowSummary);
        }
    }

    /**
     * 删除FlowSummary表所有记录，这样定时器一分钟到了之后会重新生成所有记录
     * @return
     */
    @DeleteMapping("/")
    public ResultVO deleteAll() {
        flowSummerService.clearAll();
        return ResultUtils.success();
    }
}
