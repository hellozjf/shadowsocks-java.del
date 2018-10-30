package com.hellozjf.shadowsocks.ssserver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author Jingfeng Zhou
 *
 * 这个代码是参考这个网站写出来的，https://www.baeldung.com/spring-boot-json-properties
 */
public class JsonPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(
            String name, EncodedResource resource)
            throws IOException {
        Map readValue = new ObjectMapper()
                .readValue(resource.getInputStream(), Map.class);
        return new MapPropertySource("json-property", readValue);
    }
}
