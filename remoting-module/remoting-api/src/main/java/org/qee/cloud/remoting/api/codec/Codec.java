package org.qee.cloud.remoting.api.codec;

import org.qee.cloud.remoting.api.buffer.ChannelBuffer;

import java.io.IOException;

/**
 * 编解码 ,这里精简为只实现为hession2 序列化，所以不需要URL 描述
 */
public interface Codec {

    /**
     * 把 消息 编码到buffer
     * <p>
     * //dubbo 中通过 Channel.getUrl 的描述来选择 选择哪种对象序列化（比如hession2）则通过url描述指定
     *
     * @param buffer
     * @param message
     * @throws IOException
     */
    void encode(ChannelBuffer buffer, Object message) throws IOException;

    /**
     * 把 buffer 内容解码为object
     * //dubbo 中通过 Channel.getUrl 的描述来选择 选择哪种对象反序列化（比如hession2）则通过url描述指定
     *
     * @param buffer
     * @return
     * @throws IOException
     */
    Object decode(ChannelBuffer buffer) throws IOException, ClassNotFoundException;


    enum DecodeResult {
        NEED_MORE_INPUT, SKIP_SOME_INPUT
    }
}
