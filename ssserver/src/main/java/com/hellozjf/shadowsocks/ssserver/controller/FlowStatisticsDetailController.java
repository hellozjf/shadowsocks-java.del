package com.hellozjf.shadowsocks.ssserver.controller;

import com.hellozjf.shadowsocks.ssserver.service.IFlowStatisticsDetailService;
import com.hellozjf.shadowsocks.ssserver.util.ResultUtils;
import com.hellozjf.shadowsocks.ssserver.vo.ClientIpInfo;
import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Jingfeng Zhou
 */
@RestController
@RequestMapping("/flowStatisticsDetail")
public class FlowStatisticsDetailController {

    @Autowired
    private IFlowStatisticsDetailService flowStatisticsDetailService;

    @GetMapping("/getAllClientIpInfo/")
    public ResultVO getAllClientIpInfo() {
        List<ClientIpInfo> clientIpInfoList = flowStatisticsDetailService.getAllClientIpInfoList();
        return ResultUtils.success(clientIpInfoList);
    }

    @GetMapping("/getAllClientIpInfo/{serverPort}")
    public ResultVO getAllClientIpInfo(@PathVariable("serverPort") Integer serverPort) {
        List<ClientIpInfo> clientIpInfoList = flowStatisticsDetailService.getAllClientIpInfoListByServerPort(serverPort);
        return ResultUtils.success(clientIpInfoList);
    }
}
