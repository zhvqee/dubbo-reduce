package org.qee.cloud.remoting.api.transport;

import org.qee.cloud.common.model.URL;

/**
 * 抽象对等端，主要关注 端是否关闭 和存储该端的描述信息Url
 * //dubbo 把对等端继承了Channel 能力，抽象太多次，没必要，ø
 */
public abstract class AbstractPeer implements EndPoint {

    private volatile URL url;

    /**
     * 对等端是否关闭
     */
    private volatile boolean closed;

    public AbstractPeer(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("illegal argument ");
        }
        this.url = url;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        this.closed = true;
    }

    // Getter

    @Override
    public URL getUrl() {
        return url;
    }
}
