package org.qee.cloud.remoting.api.transport;

import org.qee.cloud.common.extentions.ExtensionLoader;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.remoting.api.codec.Codec;
import org.qee.cloud.remoting.api.exchange.codec.ExchangeCodec;

import static org.qee.cloud.common.constants.CommonConstants.*;


/**
 * 抽象终端 主要处理 超时问题（包括调用超时，链接超时属性） 和编解码问题。客户端和服务端都是相同的编解码
 */
public abstract class AbstractEndPoint extends AbstractPeer {

    private Codec codec;

    private int timeout;

    private int connectTimeout;


    public AbstractEndPoint(URL url) {
        super(url);
        this.codec = getChannelCodec(url);
        this.timeout = url.getPositiveParameter(TIMEOUT_KEY, DEFAULT_TIMEOUT);
        this.connectTimeout = url.getPositiveParameter(CONNECT_TIMEOUT_KEY, DEFAULT_CONNECT_TIMEOUT);
    }

    protected Codec getChannelCodec(URL url) {
        return new ExchangeCodec();
    }

    //Getter 属性

    public Codec getCodec() {
        return codec;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }
}
