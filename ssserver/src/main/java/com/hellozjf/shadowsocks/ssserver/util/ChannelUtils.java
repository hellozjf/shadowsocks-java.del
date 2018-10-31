package com.hellozjf.shadowsocks.ssserver.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Jingfeng Zhou
 */
public class ChannelUtils {

    /**
     * 关闭多个通道
     * @param ctxs
     */
    public static void closeChannels(ChannelHandlerContext... ctxs) {
        for (ChannelHandlerContext ctx : ctxs) {
            closeChannel(ctx);
        }
    }

    /**
     * 关闭多个通道
     * @param chs
     */
    public static void closeChannels(Channel... chs) {
        for (Channel ch : chs) {
            closeChannel(ch);
        }
    }

    /**
     * 关闭单个通道
     * @param ctx
     */
    public static void closeChannel(ChannelHandlerContext ctx) {
        closeChannel(ctx.channel());
    }

    /**
     * 关闭单个通道
     * @param ch
     */
    public static void closeChannel(Channel ch) {
        if (ch != null && ch.isOpen()) {
            ch.close();
        }
    }
}
