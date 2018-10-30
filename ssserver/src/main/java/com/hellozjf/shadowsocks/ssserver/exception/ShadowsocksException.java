package com.hellozjf.shadowsocks.ssserver.exception;

import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import lombok.Getter;

/**
 * @author Jingfeng Zhou
 */
@Getter
public class ShadowsocksException extends RuntimeException {

    private Integer code;

    public ShadowsocksException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ShadowsocksException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }
}
