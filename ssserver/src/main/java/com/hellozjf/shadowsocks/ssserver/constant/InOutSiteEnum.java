package com.hellozjf.shadowsocks.ssserver.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Jingfeng Zhou
 */
@Getter
@AllArgsConstructor
public enum InOutSiteEnum {

    CLIENT_TO_SERVER(1, "入站，ss客户端 -> ss服务端"),
    SERVER_TO_CLIENT(2, "出站，ss服务端 -> ss客户端"),
    SERVER_TO_REMOTE(3, "出站，ss服务端 -> 远程服务器"),
    REMOTE_TO_SERVER(4, "入站，远程服务器 -> ss服务端"),
    ;

    private Integer direction;
    private String descption;
}
