package com.hellozjf.shadowsocks.ssserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
        return args -> ssServer.start();
    }
}
