package com.individual.common.utils;

import java.net.InetSocketAddress;

import static com.individual.common.constants.CommonConstants.ANYHOST_VALUE;
import static com.individual.common.constants.CommonConstants.LOCALHOST_KEY;

public class NetUtils {

    public static boolean isInvalidLocalHost(String host) {
        return host == null
                || host.length() == 0
                || host.equalsIgnoreCase(LOCALHOST_KEY)
                || host.equals(ANYHOST_VALUE)
                || host.startsWith("127.");
    }


    public static String toAddressString(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }
}
