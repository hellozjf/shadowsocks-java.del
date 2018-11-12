package com.hellozjf.shadowsocks.ssserver.dataobject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @author Jingfeng Zhou
 */
@Data
@Entity
@ApiModel("客户端IP信息")
public class UserInfo extends BaseEntity {

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    private String password;

    /**
     * 用户占用的SS端口号
     */
    @ApiModelProperty("用户占用的SS端口号")
    private Integer port;

    /**
     * 超时时间
     */
    @ApiModelProperty("超时时间")
    private Integer timeout;

    /**
     * 加密方式
     */
    @ApiModelProperty("加密方式")
    private String method;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phone;
}
