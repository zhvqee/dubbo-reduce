package com.individual.common.constants;

public interface CommonConstants {

    String TIMEOUT_KEY = "timeout";

    int DEFAULT_TIMEOUT = 1000;



    String CONNECT_TIMEOUT_KEY = "connect.timeout";

    int DEFAULT_CONNECT_TIMEOUT = 3000;



    String CODEC_KEY = "codec";

    String CODEC_DEFAULT="telnet";


    String BIND_IP_KEY = "bind.ip";

    String BIND_PORT_KEY = "bind.port";

    String ANYHOST_VALUE = "0.0.0.0";

    String LOCALHOST_KEY = "localhost";

    String LOCALHOST_VALUE = "127.0.0.1";

    /**
     * max size of channel. default value is zero that means unlimited.
     */
    String ACCEPTS_KEY = "accepts";

    int DEFAULT_ACCEPTS = 0;


    String IDLE_TIMEOUT_KEY = "idle.timeout";

    int DEFAULT_IDLE_TIMEOUT = 600 * 1000;


    String IO_THREADS_KEY = "iothreads";

    int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);


    String DEFAULT_KEY_PREFIX = "default.";

    String DEFAULT_KEY = "default";


}
