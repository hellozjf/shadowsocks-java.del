package com.hellozjf.shadowsocks.ssserver.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jingfeng Zhou
 */
@Data
@ApiModel("客户端IP信息")
public class ClientIpInfoVO {

    @ApiModelProperty("详细地址")
    private String as;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("国家代码")
    private String countryCode;

    @ApiModelProperty("网络服务提供者")
    private String isp;

    @ApiModelProperty("维度")
    private Double lat;

    @ApiModelProperty("经度")
    private Double lon;

    @ApiModelProperty("组织机构")
    private String org;

    @ApiModelProperty("查询IP")
    private String query;

    @ApiModelProperty("地区")
    private String region;

    @ApiModelProperty("地区名称")
    private String regionName;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("时区")
    private String timezone;

    @ApiModelProperty("邮编")
    private String zip;
}
