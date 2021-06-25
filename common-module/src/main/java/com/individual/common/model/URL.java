package com.individual.common.model;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static com.individual.common.constants.CommonConstants.DEFAULT_KEY_PREFIX;

public class URL {

    private String protocal;

    private String path;

    // by default, host to registry
    private String host;

    // by default, port to registry
    private int port;

    private Map<String, String> parameters;

    public URL(String protocal, String host, int port, String path, Map<String, String> parameters) {
        this.protocal = protocal;
        this.path = path;
        this.host = host;
        this.port = port;
        this.parameters = parameters;
    }

    public int getPositiveParameter(String key, int defaultValue) {
        return getParameter(key, defaultValue);
    }

    public String getParameter(String key, String defaultValue) {
        String ret = null;
        return (ret = parameters.get(key)) != null ? ret : defaultValue;
    }

    public int getParameter(String key, int defaultValue) {
        String ret = null;
        return (ret = parameters.get(key)) != null ? Integer.parseInt(ret) : defaultValue;
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }


    public static URL valueOf(String url) {
        if (url == null || (url = url.trim()).length() == 0) {
            throw new IllegalArgumentException("url == null");
        }
        String protocol = null;
        String username = null;
        String password = null;
        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = null;
        int i = url.indexOf('?'); // separator between body and parameters
        if (i >= 0) {
            String[] parts = url.substring(i + 1).split("&");
            parameters = new HashMap<>();
            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf('=');
                    if (j >= 0) {
                        String key = part.substring(0, j);
                        String value = part.substring(j + 1);
                        parameters.put(key, value);
                        // compatible with lower versions registering "default." keys
                        if (key.startsWith(DEFAULT_KEY_PREFIX)) {
                            parameters.putIfAbsent(key.substring(DEFAULT_KEY_PREFIX.length()), value);
                        }
                    } else {
                        parameters.put(part, part);
                    }
                }
            }
            url = url.substring(0, i);
        }
        i = url.indexOf("://");
        if (i >= 0) {
            if (i == 0) {
                throw new IllegalStateException("url missing protocol: \"" + url + "\"");
            }
            protocol = url.substring(0, i);
            url = url.substring(i + 3);
        } else {
            // case: file:/path/to/file.txt
            i = url.indexOf(":/");
            if (i >= 0) {
                if (i == 0) {
                    throw new IllegalStateException("url missing protocol: \"" + url + "\"");
                }
                protocol = url.substring(0, i);
                url = url.substring(i + 1);
            }
        }

        i = url.indexOf('/');
        if (i >= 0) {
            path = url.substring(i + 1);
            url = url.substring(0, i);
        }
        i = url.lastIndexOf('@');
        if (i >= 0) {
            username = url.substring(0, i);
            int j = username.indexOf(':');
            if (j >= 0) {
                password = username.substring(j + 1);
                username = username.substring(0, j);
            }
            url = url.substring(i + 1);
        }
        i = url.lastIndexOf(':');
        if (i >= 0 && i < url.length() - 1) {
            if (url.lastIndexOf('%') > i) {
                // ipv6 address with scope id
                // e.g. fe80:0:0:0:894:aeec:f37d:23e1%en0
                // see https://howdoesinternetwork.com/2013/ipv6-zone-id
                // ignore
            } else {
                port = Integer.parseInt(url.substring(i + 1));
                url = url.substring(0, i);
            }
        }
        if (url.length() > 0) {
            host = url;
        }

        return new URL(protocol, host, port, path, parameters);
    }
}
