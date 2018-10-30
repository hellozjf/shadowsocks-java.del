package com.hellozjf.shadowsocks.ssserver.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jingfeng Zhou
 */
@Slf4j
public class ByteBufUtils {
    public static ByteBuf getByteBufFromMsg(Object msg) {
        ByteBuf byteBuf = null;
        if (msg instanceof DatagramPacket) {
            // 说明是UDP的流量，要将里面的ByteBuf取出来
            byteBuf = ((DatagramPacket) msg).content();
        } else if (msg instanceof ByteBuf) {
            // 说明是TCP的流量，直接转化为ByteBuf
            byteBuf = (ByteBuf) msg;
        } else {
            // 不知道这是什么数据，打印个警告
            log.warn("unknown msg type: {}", msg.getClass().toString());
        }
        return byteBuf;
    }
}
