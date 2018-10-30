package com.hellozjf.shadowsocks.ssserver.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Jingfeng Zhou
 */
@Getter
@AllArgsConstructor
public enum InOutSiteEnums {

    IN_SITE(1, "入站，ss客户端 -> ss服务端"),
    OUT_SITE(2, "出站，ss服务端 -> ss客户端")
    ;

    private Integer direction;
    private String descption;
}
