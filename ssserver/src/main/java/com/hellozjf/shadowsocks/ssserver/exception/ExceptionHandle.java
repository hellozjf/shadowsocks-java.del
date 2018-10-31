package com.hellozjf.shadowsocks.ssserver.exception;

import com.hellozjf.shadowsocks.ssserver.util.ResultUtils;
import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 廖师兄
 * 2017-01-21 13:59
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO handle(Exception e) {
        if (e instanceof ShadowsocksException) {
            ShadowsocksException shadowsocksException = (ShadowsocksException) e;
            return ResultUtils.error(shadowsocksException.getCode(), shadowsocksException.getMessage());
        } else {
            log.error("【系统异常】{}", e);
            return ResultUtils.error(-1, "未知错误");
        }
    }
}
