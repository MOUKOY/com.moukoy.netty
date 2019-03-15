package maoko.net.sconectclient;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import maoko.net.ifs.INetChanel;
import maoko.net.model.CusNetSource;

/**
 * 客户端网络仓库
 * 
 * @author fanpei
 * @date 2018-09-09 23:27
 *
 */
public class ClientChannelStore {

	private Map<String, CusNetSource> chanels;

	public Map<String, CusNetSource> getChanels() {
		return chanels;
	}

	public ClientChannelStore() {
		chanels = new ConcurrentHashMap<>();
	}

	public void addChanel(CusNetSource channel) {
		chanels.put(channel.getChanelId(), channel);
	}

	public void removeChannel(INetChanel chanel) {
		chanels.remove(chanel.getChanelId());
	}

	public boolean contain(String chanelId) {
		return chanels.containsKey(chanelId);
	}

	public CusNetSource get(String chanelId) {
		return chanels.get(chanelId);
	}

	/**
	 * 关闭当前所有连接
	 */
	public void closeAllConnect() {
		Iterator<Entry<String, CusNetSource>> iterator = chanels.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, CusNetSource> e = iterator.next();
			CusNetSource channel = e.getValue();
			channel.close();
			iterator.remove();
		}
	}
}
