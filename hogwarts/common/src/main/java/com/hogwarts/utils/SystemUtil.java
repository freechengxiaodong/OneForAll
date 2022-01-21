package com.hogwarts.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

public class SystemUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemUtil.class);

    public static final DecimalFormat DECIMAL_FORMAT_0_00 = new DecimalFormat("0.00");
    public static final DecimalFormat DECIMAL_FORMAT_000 = new DecimalFormat("000");
    public static final DecimalFormat DECIMAL_FORMAT_00 = new DecimalFormat("00");

    /**
     * 本机IP缓存 - 系统启动时获取
     */
    public static final String LOCAL_IP = getLocalIP();

    /**
     * 本机IP后两段缓存 - 系统启动时获取
     */
    public static final String IP_SEGMENT = getIPSegment();

    public static final String IP_3_SEGMENT = getIP3Segment();

    public static final String IP_2_BIT = getIPLast2Bit();


    /**
     * 获取本机IP
     *
     * @return
     */
    public static String getLocalIP() {
        String ip = null;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ip = inetAddress.getHostAddress();
            if (StringUtils.isBlank(ip)) {
                throw new IllegalArgumentException("local host ip is blank.");
            }
        } catch (UnknownHostException e) {
            LOGGER.error("Failed to get localhost ip.", e);
        }
        return ip;
    }

    /**
     * 本机IP后两位代码段
     *
     * @return
     */
    public static String getIPSegment() {
        String ip = getLocalIP();
        String[] ipa = ip.split("\\.");
        String ip3 = ipa[2];
        String ip4 = ipa[3];
        LOGGER.info("local IP = {}", ip);
        String ipSegment = DECIMAL_FORMAT_000.format(Integer.parseInt(ip3)) + DECIMAL_FORMAT_000.format(Integer.parseInt(ip4));
        LOGGER.info("sequence's ip segment = {}", ipSegment);
        return ipSegment;
    }

    /**
     * 本机IP最后一段3位
     *
     * @return
     */
    public static String getIP3Segment() {
        String ip = getLocalIP();
        String[] ipa = ip.split("\\.");
        String ip4 = ipa[3];
        LOGGER.info("local IP = {}", ip);
        String ipSegment = DECIMAL_FORMAT_000.format(Integer.parseInt(ip4));
        LOGGER.info("sequence's ip 3bit segment = {}", ipSegment);
        return ipSegment;
    }

    /**
     * 本机IP最后一段2位
     *
     * @return
     */
    public static String getIPLast2Bit() {
        String ip = getLocalIP();
        String[] ipa = ip.split("\\.");
        String ip4 = ipa[3];
        LOGGER.info("local IP = {}", ip);
        String ipSegment = DECIMAL_FORMAT_00.format(Integer.parseInt(ip4));
        LOGGER.info("sequence's ip 2bit segment = {}", ipSegment);
        return ipSegment;
    }
}
