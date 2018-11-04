package com.hellozjf.shadowsocks.ssserver;

import com.hellozjf.shadowsocks.ssserver.service.IFlowSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hellozjf
 *
 * ssserver启动类
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@Slf4j
public class SSServerStarter {

    public static void main(String[] args) {
        SpringApplication.run(SSServerStarter.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(IFlowSummaryService flowSummaryService, SSServer ssServer) {
        return args -> {
            // 首先通过flowStatistics表初始化flowSummary表
            flowSummaryService.initFlowSummary();
            // 然后才能启动服务器
            ssServer.start();
        };
    }
}
