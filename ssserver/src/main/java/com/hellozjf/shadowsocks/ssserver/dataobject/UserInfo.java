package com.hellozjf.shadowsocks.ssserver.dataobject;

import lombok.Data;

import javax.persistence.Entity;

/**
 * @author Jingfeng Zhou
 */
@Data
@Entity
public class UserInfo extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户占用的SS端口号
     */
    private Integer port;

    /**
     * 超时时间
     */
    private Integer timeout;

    /**
     * 加密方式
     */
    private String method;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;
}
