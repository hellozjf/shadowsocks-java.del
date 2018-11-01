package com.hellozjf.shadowsocks.ssserver.util;

import com.hellozjf.shadowsocks.ssserver.constant.PlatformEnum;

/**
 * 操作系统类：
 * 获取System.getProperty("os.name")对应的操作系统
 *
 * @author isea533
 */
public class OSInfoUtils {

    private static String OS = System.getProperty("os.name").toLowerCase();

    private static OSInfoUtils _instance = new OSInfoUtils();

    private PlatformEnum platform;

    private OSInfoUtils() {
    }

    public static boolean isLinux() {
        return OS.indexOf("linux") >= 0;
    }

    public static boolean isMacOS() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") < 0;
    }

    public static boolean isMacOSX() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
    }

    public static boolean isWindows() {
        return OS.indexOf("windows") >= 0;
    }

    public static boolean isOS2() {
        return OS.indexOf("os/2") >= 0;
    }

    public static boolean isSolaris() {
        return OS.indexOf("solaris") >= 0;
    }

    public static boolean isSunOS() {
        return OS.indexOf("sunos") >= 0;
    }

    public static boolean isMPEiX() {
        return OS.indexOf("mpe/ix") >= 0;
    }

    public static boolean isHPUX() {
        return OS.indexOf("hp-ux") >= 0;
    }

    public static boolean isAix() {
        return OS.indexOf("aix") >= 0;
    }

    public static boolean isOS390() {
        return OS.indexOf("os/390") >= 0;
    }

    public static boolean isFreeBSD() {
        return OS.indexOf("freebsd") >= 0;
    }

    public static boolean isIrix() {
        return OS.indexOf("irix") >= 0;
    }

    public static boolean isDigitalUnix() {
        return OS.indexOf("digital") >= 0 && OS.indexOf("unix") > 0;
    }

    public static boolean isNetWare() {
        return OS.indexOf("netware") >= 0;
    }

    public static boolean isOSF1() {
        return OS.indexOf("osf1") >= 0;
    }

    public static boolean isOpenVMS() {
        return OS.indexOf("openvms") >= 0;
    }

    /**
     * 获取操作系统名字
     *
     * @return 操作系统名
     */
    public static PlatformEnum getOSname() {
        if (isAix()) {
            _instance.platform = PlatformEnum.AIX;
        } else if (isDigitalUnix()) {
            _instance.platform = PlatformEnum.Digital_Unix;
        } else if (isFreeBSD()) {
            _instance.platform = PlatformEnum.FreeBSD;
        } else if (isHPUX()) {
            _instance.platform = PlatformEnum.HP_UX;
        } else if (isIrix()) {
            _instance.platform = PlatformEnum.Irix;
        } else if (isLinux()) {
            _instance.platform = PlatformEnum.Linux;
        } else if (isMacOS()) {
            _instance.platform = PlatformEnum.Mac_OS;
        } else if (isMacOSX()) {
            _instance.platform = PlatformEnum.Mac_OS_X;
        } else if (isMPEiX()) {
            _instance.platform = PlatformEnum.MPEiX;
        } else if (isNetWare()) {
            _instance.platform = PlatformEnum.NetWare_411;
        } else if (isOpenVMS()) {
            _instance.platform = PlatformEnum.OpenVMS;
        } else if (isOS2()) {
            _instance.platform = PlatformEnum.OS2;
        } else if (isOS390()) {
            _instance.platform = PlatformEnum.OS390;
        } else if (isOSF1()) {
            _instance.platform = PlatformEnum.OSF1;
        } else if (isSolaris()) {
            _instance.platform = PlatformEnum.Solaris;
        } else if (isSunOS()) {
            _instance.platform = PlatformEnum.SunOS;
        } else if (isWindows()) {
            _instance.platform = PlatformEnum.Windows;
        } else {
            _instance.platform = PlatformEnum.Others;
        }
        return _instance.platform;
    }

}