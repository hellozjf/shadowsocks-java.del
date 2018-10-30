package com.hellozjf.shadowsocks.ssserver.dataobject;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

/**
 * @author hellozjf
 *
 * 对应配置文件config.json
 */
@Data
public class Config {

	@SerializedName("server")
	private String server;

	@SerializedName("server_port")
    private Integer serverPort;

	@SerializedName("local_address")
    private String localAddress;

	@SerializedName("local_port")
    private Integer localPort;

	@SerializedName("password")
    private String password;

    @SerializedName("timeout")
    private Integer timeout;

	@SerializedName("method")
	private String method;

	@SerializedName("fast_open")
    private String fastOpen;

    @SerializedName("port_password")
    private Map<Integer,String> portPassword;
}
