package com.hellozjf.shadowsocks.ssserver.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * @author Jingfeng Zhou
 *
 * FlowStatisticsDetail用于记录用户每次入站和出站流量
 */
@Data
@Entity
public class FlowStatisticsDetail extends BaseEntity {

    /**
     * 方向，@See com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnum
     */
    private Integer direction;

    /**
     * 入/出站的流量
     */
    private Long flowSize;

    /**
     * Client所访问的SS服务端地址
     */
    private String serverAddress;

    /**
     * 每个Client都会占用SS服务端上面的一个端口
     */
    private Integer serverPort;

    /**
     * Client使用的地址
     */
    private String clientHost;

    /**
     * Client使用的端口
     */
    private Integer clientPort;

    /**
     * 要访问的远程地址
     */
    private String remoteAddress;

    /**
     * 要访问的远程端口
     */
    private Integer remotePort;

    /**
     * 数据字节数组，这里必须要用byte[]，否则字符串转Byte[]会很麻烦
     */
    @Lob
    private byte[] content;
}
