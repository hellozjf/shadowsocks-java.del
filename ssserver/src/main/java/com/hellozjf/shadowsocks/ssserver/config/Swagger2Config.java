package com.hellozjf.shadowsocks.ssserver.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Jingfeng Zhou
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${swagger2.enable}")
    private boolean enable;

    @Bean("FlowStatisticsDetailApis")
    public Docket flowStatisticsDetailApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("流量统计详情模块")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex("/flowStatisticsDetail.*"))
                .build()
                .apiInfo(apiInfo())
                .enable(enable);
    }

    @Bean("UserInfoApis")
    public Docket userInfoApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户信息模块")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex("/userInfo.*"))
                .build()
                .apiInfo(apiInfo())
                .enable(enable);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("shadowsocks-java接口文档")
                .description("更多详情请关注https://github.com/hellozjf/shadowsocks-java")
                .termsOfServiceUrl("https://github.com/hellozjf/shadowsocks-java")
                .version("1.0")
                .build();
    }

}
