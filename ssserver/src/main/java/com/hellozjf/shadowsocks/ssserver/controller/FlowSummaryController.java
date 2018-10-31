package com.hellozjf.shadowsocks.ssserver.controller;

import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import com.hellozjf.shadowsocks.ssserver.dataobject.FlowSummary;
import com.hellozjf.shadowsocks.ssserver.exception.ShadowsocksException;
import com.hellozjf.shadowsocks.ssserver.service.IFlowSummerService;
import com.hellozjf.shadowsocks.ssserver.util.ResultUtils;
import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
@RestController
@RequestMapping("/flowSummary")
public class FlowSummaryController {

    @Autowired
    private IFlowSummerService flowSummerService;

    /**
     * 获取所有用户流量信息
     * @return
     */
    @GetMapping("/")
    public ResultVO get() {
        // TODO 获取所有用户流量信息
        return null;
    }

    @GetMapping("/{userInfoId}")
    public ResultVO get(@PathVariable("userInfoId") Long userInfoId) {
        FlowSummary flowSummary = flowSummerService.findByUserInfoId(userInfoId);
        if (flowSummary == null) {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_FIND_THIS_ID_OBJECT);
        } else {
            return ResultUtils.success(flowSummary);
        }
    }
}
