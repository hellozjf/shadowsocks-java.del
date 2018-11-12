package com.hellozjf.shadowsocks.ssserver.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hellozjf
 * http请求返回的最外层对象
 */
@Data
@ApiModel(description = "返回结果")
public class ResultVO<T> {

    /**
     * 错误码.
     */
    @ApiModelProperty(value = "错误码")
    private Integer code;

    /**
     * 提示信息.
     */
    @ApiModelProperty(value = "提示信息")
    private String msg;

    /**
     * 花费的时间，毫秒为单位
     */
    @ApiModelProperty("花费的时间，毫秒为单位")
    private Long costTime;

    /**
     * 业务数据
     */
    @ApiModelProperty("业务数据")
    private T data;

    /**
     * 构造函数
     * @param code
     * @param msg
     * @param data
     */
    private ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static<T> ResultVO<T> success() {
        return success(null);
    }

    public static<T> ResultVO<T> success(T data) {
        return new ResultVO(0, "成功", data);
    }

    public static ResultVO error(Integer code, String msg) {
        return new ResultVO(code, msg, null);
    }
}
