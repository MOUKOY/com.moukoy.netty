package maoko.net.lconectserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import maoko.common.exception.DataIsNullException;
import maoko.common.model.net.CusHostAndPort;
import maoko.net.conf.CongfigServer;
import maoko.net.exception.NoCurrentPortConnectException;
import maoko.net.ifs.INetChanel;

/**
 * 服务端连接映射总表
 * 
 * @author fanpei
 *
 */
public class ServerConMap {

	/**
	 * String:本地监听端口 CusChannelStore:连接仓库
	 */
	private final Map<String, ServerChannelStore> chanelsMap;

	public ServerConMap() throws DataIsNullException {
		if (CongfigServer.HOSTS == null || CongfigServer.HOSTS.size() == 0)
			throw new DataIsNullException("server conf hosts is null");

		chanelsMap = new HashMap<>(CongfigServer.HOSTS.size());
		for (CusHostAndPort ipp : CongfigServer.HOSTS) {
			ServerChannelStore ccs = new ServerChannelStore(ipp.getPort());
			chanelsMap.put(ccs.getStoreId(), ccs);
		}
	}

	/**
	 * 添加指定得连接
	 * 
	 * @param netSoure
	 * @throws NoCurrentPortConnectException
	 */
	public void addChannel(INetChanel netSoure) throws NoCurrentPortConnectException {
		ServerChannelStore store = getConnectMap(netSoure.getLPort());
		if (store != null) {
			store.addChannel(netSoure.getChanelId(), netSoure);
		} else
			throw new NoCurrentPortConnectException(netSoure.getLPort());
	}

	/**
	 * 移除指定的连接
	 * 
	 * @param netSoure
	 * @throws NoCurrentPortConnectException
	 */
	public void removeChannel(INetChanel netSoure) throws NoCurrentPortConnectException {
		ServerChannelStore store = getConnectMap(netSoure.getLPort());
		if (store != null) {
			store.removeChannel(netSoure.getChanelId());
		} else
			throw new NoCurrentPortConnectException(netSoure.getLPort());
	}

	/**
	 * 获取指定端口上得所有连接
	 * 
	 * @param localPort
	 * @return
	 */
	public Collection<INetChanel> getAllChanels(int localPort) {
		ServerChannelStore store = getConnectMap(localPort);
		if (store != null) {
			return store.getAllChanels();
		} else
			return null;
	}

	/**
	 * 获取所有链接
	 * 
	 * @return
	 */
	public Collection<INetChanel> getAllChanels() {
		Collection<INetChanel> channels = null;
		if (chanelsMap != null) {
			channels = new ArrayList<>();
			for (ServerChannelStore store : chanelsMap.values()) {
				Collection<INetChanel> chs = store.getAllChanels();
				if (chs != null && !chs.isEmpty())
					channels.addAll(chs);
			}
		}
		return channels;
	}

	/**
	 * 获取指定端口 连接池仓库
	 * 
	 * @param port 服务器端口
	 * @return
	 */
	private ServerChannelStore getConnectMap(int port) {
		String key = Integer.toString(port);
		return chanelsMap.get(key);
	}

	/**
	 * 获取指定连接当前连接数
	 * 
	 * @param port
	 * @return
	 */
	public int getConnectNum(int port) {
		int num = 0;
		ServerChannelStore store = getConnectMap(port);
		if (store != null)
			num = store.getCurrentConNum();
		return num;
	}

	/**
	 * 获取当前所有连接数
	 * 
	 * @return
	 */
	public int getAllConectNum() {
		int num = 0;
		for (ServerChannelStore store : chanelsMap.values()) {
			num += store.getCurrentConNum();
		}
		return num;
	}

	/**
	 * 关闭当前所有连接
	 */
	public void closeAllConnect() {
		Collection<INetChanel> channels = getAllChanels();
		for (INetChanel ch : channels) {
			ch.close();
		}
	}

}
