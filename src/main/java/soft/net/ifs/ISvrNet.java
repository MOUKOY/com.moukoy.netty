package soft.net.ifs;

import java.util.Collection;

/**
 * 网络传输接口 server
 * 
 * @author FanPei
 *
 */
public interface ISvrNet {

	/**
	 * 启动网络监听服务
	 * 
	 * @return
	 */
	void start() throws Exception;

	/**
	 * 关闭网络服务和连接
	 * 
	 * @return
	 */
	void close() /* throws CusException */;

	/**
	 * 向当前已连接所有客户端广播数据
	 * 
	 * @param data 广播数据内容
	 * @return
	 */
	void sendDataToAllClient(IBytesBuild data) throws Exception;

	/**
	 * 向指定端口上广播数据
	 * 
	 * @param localPort
	 * @param data      广播数据内容
	 * @throws Exception
	 */
	void sendDataToAllClient(int localPort, IBytesBuild data) throws Exception;

	/**
	 * 定向发送数据
	 * 
	 * @param channels 指定的连接集合
	 * @param data    数据内容
	 * @throws Exception
	 */
	void sendDataToAllClient(Collection<INetChanel> channels, IBytesBuild data) throws Exception;

	/**
	 * 获取本地指定端口上所有连接的个数
	 * 
	 * @param localPort 指定端口
	 * @return
	 */
	int getAllConnectNum(int localPort);

	/**
	 * 获取本地所有连接的个数
	 * 
	 * @return
	 */
	int getAllConnectNum();
}
