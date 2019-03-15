package maoko.net.ifs;

import java.util.Map;

import io.netty.channel.Channel;
import maoko.net.model.CusHostAndPort;

/**
 * 网络链路接口
 * 
 * @author fanpei
 *
 */
public interface INetChanel extends ISendData {

	/**
	 * 获取连接配置用户自定义信息
	 * 
	 * @return
	 */
	public Map<String, Object> getSettings();

	/**
	 * 关闭连接
	 */
	void close();

	/**
	 * 获取netty网络链路
	 * 
	 * @return
	 */
	Channel getChannel();

	/**
	 * 获取数据源地址信息
	 * 
	 * @return
	 */
	CusHostAndPort getRAddress();

	/**
	 * 获取远端源地址[IP：Port]
	 * 
	 * @return
	 */
	String getRIpPort();

	/**
	 * 获取远端的源地址[IP]
	 * 
	 * @return
	 */
	String getRIP();

	/**
	 * 获取远端端口
	 * 
	 * @return
	 */
	int getRPort();

	/**
	 * 获取接受当前数据的本地端口
	 * 
	 * @return
	 */
	int getLPort();

	/**
	 * 获取当前源指定的唯一标识
	 * 
	 * @return
	 */
	String getChanelId();

}
