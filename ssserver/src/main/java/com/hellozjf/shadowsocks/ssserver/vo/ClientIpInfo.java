package com.hellozjf.shadowsocks.ssserver.vo;

import lombok.Data;

/**
 * @author Jingfeng Zhou
 */
@Data
public class ClientIpInfo {
    private String as;
    private String city;
    private String country;
    private String countryCode;
    private String isp;
    private Double lat;
    private Double lon;
    private String org;
    private String query;
    private String region;
    private String regionName;
    private String status;
    private String timezone;
    private String zip;
}
