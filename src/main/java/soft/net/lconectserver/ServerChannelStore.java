package soft.net.lconectserver;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import soft.net.ifs.INetChanel;

/**
 * 服务端网络连接仓库
 * 
 * @author fanpei
 * @date 2018-09-09 22:06
 *
 */
public class ServerChannelStore {
	private int port;// 当前仓库端口标识

	// ip port chanel
	private Map<String, INetChanel> channels;

	/**
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 获取链路仓库
	 * 
	 * @return
	 */
	public Map<String, INetChanel> getChannels() {
		return channels;
	}

	public ServerChannelStore(int port) {
		this.port = port;
		channels = new ConcurrentHashMap<>();
	}

	/**
	 * 获取存储标识
	 * 
	 * @return
	 */
	public String getStoreId() {
		return Integer.toString(port);
	}

	/**
	 * 添加链路
	 * 
	 * @param chanelId 链路标识
	 * @param ch       链路对象
	 */
	public void addChannel(String chanelId, INetChanel ch) {

		channels.put(chanelId, ch);
	}

	/**
	 * 移除链路
	 * 
	 * @param chanelId 链路标识
	 */
	public void removeChannel(String chanelId) {
		if (!channels.isEmpty()) {
			channels.remove(chanelId);
		}
	}

	/**
	 * 获取链路
	 * 
	 * @param channelId 链路标识
	 * @return
	 */
	public INetChanel getChannel(String channelId) {
		return channels.get(channelId);
	}

	public Collection<INetChanel> getAllChanels() {
		return channels.values();
	}

	/**
	 * 获取当前连接数
	 * 
	 * @return
	 */
	public int getCurrentConNum() {
		return channels.size();
	}

}
