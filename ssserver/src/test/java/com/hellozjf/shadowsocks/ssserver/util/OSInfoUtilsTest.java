package com.hellozjf.shadowsocks.ssserver.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import static org.junit.Assert.*;

/**
 * @author Jingfeng Zhou
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OSInfoUtilsTest {

    @Test
    public void isWindows() {
        assertTrue(OSInfoUtils.isWindows());
    }
}