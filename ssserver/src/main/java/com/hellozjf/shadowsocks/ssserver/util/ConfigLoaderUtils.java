package com.hellozjf.shadowsocks.ssserver.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hellozjf.shadowsocks.ssserver.dataobject.Config;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author hellozjf
 *
 * 加载Config配置工具
 */
@Slf4j
public class ConfigLoaderUtils {

    /**
     * 从指定路径中读取config.json配置
     * @param file
     * @return
     * @throws Exception
     */
    public static Config load(String file) throws Exception {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            JsonReader reader;
            reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            Config config = new Gson().fromJson(reader, Config.class);
            reader.close();
            return config;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
