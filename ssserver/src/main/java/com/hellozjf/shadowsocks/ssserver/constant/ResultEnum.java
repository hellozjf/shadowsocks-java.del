package com.hellozjf.shadowsocks.ssserver.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Jingfeng Zhou
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
    CAN_NOT_GET_BEAN(1, "无法获取bean"),
    ;

    Integer code;
    String message;
}
