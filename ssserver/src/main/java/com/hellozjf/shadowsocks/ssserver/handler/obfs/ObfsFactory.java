package com.hellozjf.shadowsocks.ssserver.handler.obfs;

import com.hellozjf.shadowsocks.ssserver.handler.obfs.impl.HttpSimpleHandler;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
public class ObfsFactory {

    public static List<ChannelHandler> getObfsHandler(String obfs) {
        switch (obfs) {
            case HttpSimpleHandler.OBFS_NAME:
                return HttpSimpleHandler.getHandlers();
        }
        return null;
    }
}
