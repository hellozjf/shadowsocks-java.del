package com.hellozjf.shadowsocks.ssserver.vo;

import lombok.Data;

/**
 * @author hellozjf
 *
 * 存储某个IP浏览的站点，浏览的次数，浏览的上传和下载流量
 */
@Data
public class ClientHostBrowseContentVO {

    /**
     * 访问者的IP地址
     */
    private String clientHost;

    /**
     * 被访问者的IP地址
     */
    private String remoteAddress;

    /**
     * 被访问的次数，这个次数可能不太准，可能需要除以4
     */
    private Long browseCount;

    /**
     * 上传流量大小，暂不实现
     */
    private Long uploadSize;

    /**
     * 下载流量大小，暂不实现
     */
    private Long downloadSize;

    /**
     * 总流量大小
     */
    private Long flowSize;

    public ClientHostBrowseContentVO(String remoteAddress, Long browseCount, Long flowSize) {
        this.remoteAddress = remoteAddress;
        this.browseCount = browseCount;
        this.flowSize = flowSize;
    }
}
