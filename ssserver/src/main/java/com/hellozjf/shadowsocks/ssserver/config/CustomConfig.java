package com.hellozjf.shadowsocks.ssserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Jingfeng Zhou
 */
@Data
@Component
@ConfigurationProperties("custom")
public class CustomConfig {
    private Integer minPort;
    private Integer maxPort;
    private Integer runtimeCallTimeout;
}
