package com.individual.remoting.api.exchange;

import com.individual.common.exceptions.RemotingException;
import com.individual.common.extentions.ExtensionLoader;
import com.individual.common.model.URL;

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
