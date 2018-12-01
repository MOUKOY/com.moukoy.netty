package soft.net.lconectserver;

import java.util.HashMap;
import java.util.Map;

import soft.net.model.NetEventListener;

/**
 * 监听器仓库
 * 
 * @author fanpei
 *
 */
public class ListenerStore {
	private Map<String, Class<NetEventListener>> listers = new HashMap<>();

	public void add(String ipKey, Class<NetEventListener> clazz) {
		listers.put(ipKey, clazz);
	}

	public Class<NetEventListener> getListenerClass(String ipKey) {
		return listers.get(ipKey);
	}

}
