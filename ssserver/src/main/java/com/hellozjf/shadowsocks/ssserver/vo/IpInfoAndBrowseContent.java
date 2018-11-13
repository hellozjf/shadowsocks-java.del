package com.hellozjf.shadowsocks.ssserver.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Jingfeng Zhou
 *
 * 存储IP信息以及其对应的浏览内容
 */
@Data
public class IpInfoAndBrowseContent {
    private ClientIpInfoVO clientIpInfoVO;
    private List<ClientHostBrowseContentVO> clientHostBrowseContentVOList;
}
