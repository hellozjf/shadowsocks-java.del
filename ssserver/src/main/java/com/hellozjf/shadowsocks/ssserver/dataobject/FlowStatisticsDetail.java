package com.hellozjf.shadowsocks.ssserver.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Jingfeng Zhou
 *
 * FlowStatisticsDetail用于记录用户每次入站和出站流量
 */
@Data
@Entity
public class FlowStatisticsDetail {

    /**
     * ID
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 一个端口对应一个用户
     */
    private Integer port;

    /**
     * 入/出站的流量
     */
    private Integer flow;

    /**
     * 方向，@See com.hellozjf.shadowsocks.ssserver.constant.InOutSiteEnums
     */
    private Integer direction;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;
}
