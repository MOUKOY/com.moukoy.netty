package maoko.net.lconectserver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import maoko.net.conf.IPAddrCallback;
import maoko.net.exception.IPCallbackNullException;
import maoko.net.model.NetEventListener;

/**
 * 监听器仓库
 *
 * @author fanpei
 */
@Deprecated
public class ListenerStore {
    private IPAddrCallback[] callbacks;
    private Map<String, NetEventListener> listers;

    public ListenerStore(IPAddrCallback[] callbacks) throws Exception {
        this.callbacks = callbacks;
        listers = new HashMap<>();
        for (int i = 0; i < callbacks.length; i++) {
            IPAddrCallback IPAddrCallback = callbacks[i];
            IPAddrCallback.validate();
            listers.put(IPAddrCallback.getHost().getAddrStr(), IPAddrCallback.getListener());
        }
    }

    @Deprecated
    public void add(String ipKey, NetEventListener listener) {
        listers.put(ipKey, listener);
    }

    public int size() {
        return callbacks.length;
    }

    public IPAddrCallback getCallbackIndex(int index) {
        return callbacks[index];
    }

    public NetEventListener getListener(String ipKey) {
        return listers.get(ipKey);
    }

    public Collection<NetEventListener> listeners() {
        return listers.values();
    }

    /**
     * 初始化
     *
     * @param callbacks
     * @return
     * @throws IPCallbackNullException
     */
    public static ListenerStore initListeners(IPAddrCallback[] callbacks) throws Exception {
        if (callbacks == null || callbacks.length == 0)
            throw new IPCallbackNullException();
        return new ListenerStore(callbacks);
    }

}
