package com.hellozjf.shadowsocks.ssserver.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Jingfeng Zhou
 */
@Getter
@AllArgsConstructor
public enum InOutSiteEnum {

    CLIENT_TO_SERVER(1, "入站", "client -> server"),
    SERVER_TO_CLIENT(2, "出站", "server -> client"),
    SERVER_TO_REMOTE(3, "出站", "server -> remote"),
    REMOTE_TO_SERVER(4, "入站", "remote -> server"),
    ;

    private Integer direction;
    private String inOutDesc;
    private String detail;

    public static InOutSiteEnum getByDirection(Integer direction) {
        InOutSiteEnum[] inOutSiteEnums = InOutSiteEnum.values();
        for (InOutSiteEnum inOutSiteEnum : inOutSiteEnums) {
            if (inOutSiteEnum.getDirection().equals(direction)) {
                return inOutSiteEnum;
            }
        }
        return null;
    }
}
