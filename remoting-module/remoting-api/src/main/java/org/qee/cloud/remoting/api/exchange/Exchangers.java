package org.qee.cloud.remoting.api.exchange;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.extentions.ExtensionLoader;
import org.qee.cloud.common.model.URL;

/**
 * 协议层 交互入口
 */
public class Exchangers {

    public static ExchangeServer bind(URL url, ExchangeHandler exchangeHandler) throws RemotingException {
        return getExchanger().bind(url, exchangeHandler);
    }


    public static ExchangeClient connect(URL url, ExchangeHandler exchangeHandler) throws RemotingException {
        return getExchanger().connect(url, exchangeHandler);
    }


    public static Exchanger getExchanger() {
        return ExtensionLoader.getExtensionLoader(Exchanger.class).getDefaultExtension();
    }


}
