package org.qee.cloud.common.utils;

import org.qee.cloud.common.constants.CommonConstants;

import java.net.InetSocketAddress;

public class NetUtils {

    public static boolean isInvalidLocalHost(String host) {
        return host == null
                || host.length() == 0
                || host.equalsIgnoreCase(CommonConstants.LOCALHOST_KEY)
                || host.equals(CommonConstants.ANYHOST_VALUE)
                || host.startsWith("127.");
    }


    public static String toAddressString(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }
}
