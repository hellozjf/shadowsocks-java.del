package com.hellozjf.shadowsocks.ssserver.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Jingfeng Zhou
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProcessUtilsTest {

    @Autowired
    private ProcessUtils processUtils;

    @Test
    public void getPID() {
        assertEquals("840", processUtils.getPID("135"));
    }

    @Test
    public void killPID() throws Exception {
        String pid = processUtils.getPID("9999");
        log.debug("即将kill PID为{}的进程", pid);
        processUtils.killPID(pid);
        log.debug("已成功kill PID为{}的进程", pid);
        pid = processUtils.getPID("9999");
        assertEquals(null, pid);
    }
}