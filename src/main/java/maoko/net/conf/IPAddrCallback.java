package maoko.net.conf;

import maoko.common.exception.DataIsNullException;
import maoko.net.ifs.IValidate;
import maoko.net.model.CusHostAndPort;
import maoko.net.model.NetEventListener;

/**
 * 服务端ip端口和回调listener
 *
 * @author maoko
 * @date 2019/9/6 15:49
 */
public class IPAddrCallback implements IValidate {
    private CusHostAndPort host;
    private NetEventListener listener;

    public IPAddrCallback(CusHostAndPort host, NetEventListener listener) {
        this.host = host;
        this.listener = listener;
    }

    public CusHostAndPort getHost() {
        return host;
    }

    public NetEventListener getListener() {
        return listener;
    }

    @Override
    public void validate() throws Exception {
        if (host == null || listener == null)
            throw new DataIsNullException("host and listener must both not null");
    }
}
