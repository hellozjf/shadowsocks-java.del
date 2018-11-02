package com.hellozjf.shadowsocks.ssserver.util;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.hellozjf.shadowsocks.ssserver.config.CustomConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.*;

/**
 * @author Jingfeng Zhou
 */
@Component
@Slf4j
public class ProcessUtils {

    @Autowired
    private CustomConfig customConfig;

    /**
     * 杀死对应PID的进程
     *
     * @param pid
     * @throws Exception
     */
    public void killPID(String pid) throws Exception {
        if (StringUtils.isEmpty(pid)) {
            log.warn("正在杀死空的进程号");
            return;
        }
        if (OSInfoUtils.isLinux()) {
            killLinuxPID(pid);
        } else if (OSInfoUtils.isWindows()) {
            killWindowsPID(pid);
        }
    }

    private void killWindowsPID(String pid) throws Exception {
        Process process = Runtime.getRuntime().exec("taskkill /F /PID " + pid);
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        TimeLimiter timeLimiter = SimpleTimeLimiter.create(executorService);
        waitKillReturn(process, br, timeLimiter);
    }

    private void killLinuxPID(String pid) throws Exception {
        Process process = Runtime.getRuntime().exec("kill -9 " + pid);
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        TimeLimiter timeLimiter = SimpleTimeLimiter.create(executorService);
        waitKillReturn(process, br, timeLimiter);
    }

    private void waitKillReturn(Process process, BufferedReader br, TimeLimiter timeLimiter) throws InterruptedException, ExecutionException {
        String line;
        try {
            while ((line = timeLimiter.callWithTimeout(br::readLine, customConfig.getRuntimeCallTimeout(), TimeUnit.SECONDS)) != null) {
                // 读光所有的内容，然后process就会结束
                log.debug("{}", line);
            }
        } catch (TimeoutException e) {
            // 这是正常的，java的process压根就不会关闭流，所以我只能通过超时来关闭它
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * 查询端口被哪个进程占用了
     *
     * @param port
     * @return
     */
    public String getPID(String port) {
        if (OSInfoUtils.isLinux()) {
            return getLinuxPID(port);
        } else if (OSInfoUtils.isWindows()) {
            return getWindowsPID(port);
        }
        return null;
    }

    private String getWindowsPID(String port) {
        String pid = null;
        String cmd = "cmd /k netstat -ano | findstr " + port;
        Process process = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            TimeLimiter timeLimiter = SimpleTimeLimiter.create(executorService);
            String line = null;
            while ((line = timeLimiter.callWithTimeout(br::readLine, customConfig.getRuntimeCallTimeout(), TimeUnit.SECONDS)) != null) {
                if (line.indexOf("0.0.0.0:" + port) != -1) {
                    String[] lineArray = line.split("\\s+");
                    pid = lineArray[5].trim();
                    return pid;
                } else if (line.indexOf("[::]:" + port) != -1) {
                    String[] lineArray = line.split("\\s+");
                    pid = lineArray[5].trim();
                    return pid;
                }
            }
        } catch (TimeoutException e) {
            // 这是正常的，java的process压根就不会关闭流，所以我只能通过超时来关闭它
        } catch (Exception e) {
            log.error("getWindowsPID {}", e.getMessage());
        } finally {
            // 杀掉Runtime运行的进程
            if (process != null) {
                process.destroy();
            }
        }
        return pid;
    }

    private String getLinuxPID(String port) {
        String pid = null;
        String cmd = "/bin/sh -c netstat -anp | grep " + port;
        Process process = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            TimeLimiter timeLimiter = SimpleTimeLimiter.create(executorService);
            String line = null;
            while ((line = timeLimiter.callWithTimeout(br::readLine, customConfig.getRuntimeCallTimeout(), TimeUnit.SECONDS)) != null) {
                if (line.indexOf("0.0.0.0:" + port) != -1) {
                    String[] lineArray = line.split("\\s+");
                    pid = lineArray[6].trim().split("/")[0];
                    return pid;
                } else if (line.indexOf(":::" + port) != -1) {
                    String[] lineArray = line.split("\\s+");
                    pid = lineArray[6].trim().split("/")[0];
                    return pid;
                }
            }
        } catch (TimeoutException e) {
            // 这是正常的，java的process压根就不会关闭流，所以我只能通过超时来关闭它
        } catch (Exception e) {
            log.error("getLinuxPID {}", e.getMessage());
        } finally {
            // 杀掉Runtime运行的进程
            if (process != null) {
                process.destroy();
            }
        }
        return pid;
    }
}
