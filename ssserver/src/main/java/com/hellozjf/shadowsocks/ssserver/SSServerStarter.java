package com.hellozjf.shadowsocks.ssserver;

import com.hellozjf.shadowsocks.ssserver.dataobject.Config;
import com.hellozjf.shadowsocks.ssserver.util.ConfigLoaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author hellozjf
 *
 * ssserver启动类
 */
@SpringBootApplication
@Slf4j
public class SSServerStarter {

    public static void main(String[] args) {
        SpringApplication.run(SSServerStarter.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(SSServer ssServer) {
        return args -> {
            // 打印配置信息
            Config config = ConfigLoaderUtils.load("config.json");
            log.debug("config={}", config);

            // 启动SSServer
            ssServer.start(config);
        };
    }
}
