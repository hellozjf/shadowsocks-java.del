package com.hellozjf.shadowsocks.ssserver.controller;

import com.hellozjf.shadowsocks.ssserver.service.IFlowStatisticsDetailService;
import com.hellozjf.shadowsocks.ssserver.util.ResultUtils;
import com.hellozjf.shadowsocks.ssserver.vo.ClientHostBrowseContentVO;
import com.hellozjf.shadowsocks.ssserver.vo.ClientIpInfoVO;
import com.hellozjf.shadowsocks.ssserver.vo.IpInfoAndBrowseContent;
import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Jingfeng Zhou
 */
@RestController
@RequestMapping("/flowStatisticsDetail")
public class FlowStatisticsDetailController {

    @Autowired
    private IFlowStatisticsDetailService flowStatisticsDetailService;

    /**
     * 获取所有端口下所有服务HOST的IP信息
     *
     * @return
     */
    @GetMapping("/getAllClientIpInfo/")
    public ResultVO getAllClientIpInfo() {
        List<ClientIpInfoVO> clientIpInfoList = flowStatisticsDetailService.getAllClientIpInfoList();
        return ResultUtils.success(clientIpInfoList);
    }

    /**
     * 获取特定端口下所有服务HOST的IP信息
     *
     * @param serverPort
     * @return
     */
    @GetMapping("/getAllClientIpInfo/{serverPort}")
    public ResultVO getAllClientIpInfo(@PathVariable("serverPort") Integer serverPort) {
        List<ClientIpInfoVO> clientIpInfoList = flowStatisticsDetailService.getAllClientIpInfoListByServerPort(serverPort);
        return ResultUtils.success(clientIpInfoList);
    }

    /**
     * 查看某个IP访问过哪些网站，统计访问次数和访问流量大小
     *
     * @param clientHost
     * @return
     */
    @GetMapping("/getBrowseContent/{clientHost}")
    public ResultVO getBrowseContentByClientHost(@PathVariable("clientHost") String clientHost) {
        return ResultUtils.success(flowStatisticsDetailService.getClientHostBrowseContentList(clientHost));
    }

    /**
     * 查看所有IP访问过哪些网站，统计访问次数和访问流量大小
     *
     * @return
     */
    @GetMapping("/getBrowseContent/")
    public ResultVO getBrowseContentByClientHost() {
        List<ClientIpInfoVO> clientIpInfoVOList = flowStatisticsDetailService.getAllClientIpInfoList();
        List<IpInfoAndBrowseContent> ipInfoAndBrowseContentList = new ArrayList<>();
        for (ClientIpInfoVO clientIpInfoVO : clientIpInfoVOList) {
            IpInfoAndBrowseContent ipInfoAndBrowseContent = new IpInfoAndBrowseContent();
            ipInfoAndBrowseContent.setClientIpInfoVO(clientIpInfoVO);
            List<ClientHostBrowseContentVO> clientHostBrowseContentVOList = flowStatisticsDetailService.getClientHostBrowseContentList(clientIpInfoVO.getQuery());
            ipInfoAndBrowseContent.setClientHostBrowseContentVOList(clientHostBrowseContentVOList);
            ipInfoAndBrowseContentList.add(ipInfoAndBrowseContent);
        }
        ipInfoAndBrowseContentList.sort(new Comparator<IpInfoAndBrowseContent>() {
            @Override
            public int compare(IpInfoAndBrowseContent o1, IpInfoAndBrowseContent o2) {
                return o1.getClientHostBrowseContentVOList().size() < o2.getClientHostBrowseContentVOList().size() ? -1 : 1;
            }
        });
        return ResultUtils.success(ipInfoAndBrowseContentList);
    }
}
