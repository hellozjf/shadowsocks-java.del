package com.hellozjf.shadowsocks.ssserver;

import com.hellozjf.shadowsocks.ssserver.config.Config;
import com.hellozjf.shadowsocks.ssserver.dataobject.UserInfo;
import com.hellozjf.shadowsocks.ssserver.handler.TcpChannelInitializer;
import com.hellozjf.shadowsocks.ssserver.handler.UdpChannelInitializer;
import com.hellozjf.shadowsocks.ssserver.repository.UserInfoRepository;
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

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SSServer {

    @Autowired
    private Config config;

    @Autowired
    private UserInfoRepository userInfoRepository;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 通过配置文件初始化数据库中的用户信息
     */
    private void addUser(Integer port, String username, String password, Integer timeout, String method) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPort(port);
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfo.setTimeout(timeout);
        userInfo.setMethod(method);
        userInfoRepository.save(userInfo);
    }

    /**
     * 通过配置文件启动程序，同时初始化数据库
     */
    private void startWithConfigAndInitUserInfo() {
        if (config.getPortPassword() != null) {
            // 如果当前配置中有多用户，那么以多用户为准
            for (Map.Entry<Integer, String> portPassword : config.getPortPassword().entrySet()) {
                try {
                    // 启动对应的TCP和UDP端口
                    startSingle(config.getServer(),
                            portPassword.getKey(),
                            portPassword.getValue(),
                            config.getMethod());

                    // 添加user+port用户
                    addUser(portPassword.getKey(),
                            "user" + portPassword.getKey(),
                            portPassword.getValue(),
                            config.getTimeout(),
                            config.getMethod());
                } catch (Exception e) {
                    // TODO 如果端口开启成功，但是数据库写入失败，怎么办？
                    log.error("startSingle failed, e = {}", e);
                }
            }
        } else {
            // 否则以单用户为准
            try {
                // 启动对应的TCP和UDP端口
                startSingle(config.getServer(),
                        config.getServerPort(),
                        config.getPassword(),
                        config.getMethod());

                // 添加admin用户
                addUser(config.getServerPort(),
                        "admin",
                        config.getPassword(),
                        config.getTimeout(),
                        config.getMethod());
            } catch (Exception e) {
                // TODO 如果端口开启成功，但是数据库写入失败，怎么办？
                log.error("startSingle failed, e = {}", e);
            }
        }
    }

    /**
     * 通过数据库的内容启动程序
     */
    private void startWithDatabase(List<UserInfo> userInfoList) {
        for (UserInfo userInfo : userInfoList) {
            String server = config.getServer();
            Integer port = userInfo.getPort();
            String password = userInfo.getPassword();
            String method = userInfo.getMethod();
            try {
                startSingle(server, port, password, method);
            } catch (Exception e) {
                // TODO 这是个严重的错误，端口启动失败，用户就会无法使用这个服务，那么应该要将占用该端口的进程杀掉，再次尝试启动端口
                log.error("启动端口{}失败", port);
            }
        }
    }

    public void start() throws Exception {

        // 首先从数据库中获取用户信息
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        if (userInfoList.size() == 0) {
            // 说明数据库中没有用户信息，那么就从classpath:config.json中初始化用户
            startWithConfigAndInitUserInfo();
        } else {
            // 说明数据库中有用户信息，那么就从数据库中初始化端口
            startWithDatabase(userInfoList);
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
                .childHandler(new TcpChannelInitializer(method, password, obfs, obfsparam));

        log.info("TCP Start At {}:{}", server, port);
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
                .handler(new UdpChannelInitializer(method, password, obfs, obfsparam))
        ;
        udpBootstrap.bind(server, port).sync();
        log.info("UDP Start At {}:{}", server, port);
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
