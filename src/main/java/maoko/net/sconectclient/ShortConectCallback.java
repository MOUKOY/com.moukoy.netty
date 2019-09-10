package maoko.net.sconectclient;

import maoko.net.protocol.IProtocol;

/**
 * @author maoko
 * @date 2019/9/10 11:43
 */
public interface ShortConectCallback<Protocol extends IProtocol> {

    /**
     * 接收数据处理
     *
     * @param iprotocol
     */
    void dataReciveEvent(Protocol iprotocol);
}
