package com.hellozjf.shadowsocks.ssserver.controller;

import com.hellozjf.shadowsocks.ssserver.service.IFlowStatisticsDetailService;
import com.hellozjf.shadowsocks.ssserver.vo.ClientIpInfoVO;
import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "流量统计详情")
public class FlowStatisticsDetailController {

    @Autowired
    private IFlowStatisticsDetailService flowStatisticsDetailService;

    /**
     * @return
     */
    @GetMapping("/getAllClientIpInfo/")
    @ApiOperation("查询所有端口服务的所有客户IP信息")
    public ResultVO<List<ClientIpInfoVO>> getAllClientIpInfo() {
        List<ClientIpInfoVO> clientIpInfoList = flowStatisticsDetailService.getAllClientIpInfoList();
        return ResultVO.success(clientIpInfoList);
    }

    @GetMapping("/getAllClientIpInfo/{serverPort}")
    @ApiOperation("查询特定端口服务的所有客户IP信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "serverPort", value = "服务端口", required = true, dataType = "Integer", paramType = "path")
    )
    public ResultVO<List<ClientIpInfoVO>> getAllClientIpInfo(@PathVariable("serverPort") Integer serverPort) {
        List<ClientIpInfoVO> clientIpInfoList = flowStatisticsDetailService.getAllClientIpInfoListByServerPort(serverPort);
        return ResultVO.success(clientIpInfoList);
    }
}
