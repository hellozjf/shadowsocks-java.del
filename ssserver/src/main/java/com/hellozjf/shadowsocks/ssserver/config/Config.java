package com.hellozjf.shadowsocks.ssserver.config;


import com.hellozjf.shadowsocks.ssserver.util.JsonPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author hellozjf
 * <p>
 * 对应配置文件config.json
 */
@Component
@PropertySource(
        value = "classpath:config.json",
        factory = JsonPropertySourceFactory.class
)
@ConfigurationProperties
@Data
public class Config {

    private String server;
    private Integer serverPort;
    private String localAddress;
    private Integer localPort;
    private String password;
    private Integer timeout;
    private String method;
    private String fastOpen;
    private Map<Integer, String> portPassword;
    private String obfs;
    private String obfsparam;
}
