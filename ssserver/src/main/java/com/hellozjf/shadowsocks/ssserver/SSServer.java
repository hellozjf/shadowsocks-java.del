package com.hellozjf.shadowsocks.ssserver;

import com.hellozjf.shadowsocks.ssserver.dataobject.Config;
import com.hellozjf.shadowsocks.ssserver.handler.TcpChannelInitializer;
import com.hellozjf.shadowsocks.ssserver.handler.UdpChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class SSServer {

    @Autowired
    private TcpChannelInitializer tcpChannelInitializer;

    @Autowired
    private UdpChannelInitializer udpChannelInitializer;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void start(Config config) throws Exception {
        if (config.getPortPassword() != null) {
            // 如果当前配置中有多用户，那么以多用户为准
            for (Map.Entry<Integer, String> portPassword : config.getPortPassword().entrySet()) {
                startSingle(config.getServer(), portPassword.getKey(), portPassword.getValue(), config.getMethod());
            }
        } else {
            // 否则以单用户为准
            startSingle(config.getServer(), config.getServerPort(), config.getPassword(), config.getMethod());
        }
    }

    private void startSingle(String server, Integer port, String password, String method) throws Exception {
        startSingle(server, port, password, method, "", "");
    }

    private void startSingle(String server, Integer port, String password, String method, String obfs, String obfsparam) throws Exception {
        startSingleTcp(server, port, password, method, obfs, obfsparam);
        startSingleUdp(server, port, password, method, obfs, obfsparam);
    }

    private void startSingleTcp(String server, Integer port, String password, String method, String obfs, String obfsparam) throws Exception {
        ServerBootstrap tcpBootstrap = new ServerBootstrap();
        tcpBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 5120)
                // 读缓冲区为32k
                .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                //关闭时等待1s发送关闭
                .childOption(ChannelOption.SO_LINGER, 1)
                .childHandler(tcpChannelInitializer.init(method, password, obfs, obfsparam));

        log.debug("TCP Start At Port {}", port);
        tcpBootstrap.bind(server, port).sync();
    }

    private void startSingleUdp(String server, Integer port, String password, String method, String obfs, String obfsparam) throws Exception {

        //udp server
        Bootstrap udpBootstrap = new Bootstrap();
        udpBootstrap.group(bossGroup).channel(NioDatagramChannel.class)
                // 支持广播
                .option(ChannelOption.SO_BROADCAST, true)
                // 设置UDP读缓冲区为64k
                .option(ChannelOption.SO_RCVBUF, 64 * 1024)
                // 设置UDP写缓冲区为64k
                .option(ChannelOption.SO_SNDBUF, 64 * 1024)
                .handler(udpChannelInitializer.init(method, password, obfs, obfsparam))
        ;
        udpBootstrap.bind(server, port).sync();
        log.debug("udp listen at {}:{}", server, port);
    }

    public void stop() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        log.info("Stop Server!");
    }

}
