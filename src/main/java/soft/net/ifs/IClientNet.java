package soft.net.ifs;

import soft.net.model.ClientChanel;
import soft.net.model.NetEventListener;

public interface IClientNet {

	/**
	 * 启动网络监听服务
	 * 
	 * @return
	 */
	// void start();

	/**
	 * 关闭网络服务和连接
	 * 
	 * @return
	 */
	void close() /* throws CusException */;

	/**
	 * 向服务端发起长连接 成功返回连接,关闭程序时，需要自行关闭连接和释放
	 * 
	 * @param ip
	 * @param port
	 * @param timeout 超时时间
	 * @return
	 */
	ClientChanel connectServer(String ip, int port, int timeout);

	/**
	 * 向服务端发送数据
	 * 
	 * @param ip
	 * @param port
	 * @param data
	 * @param timeout 超时时间，单位毫秒
	 * @throws Exception
	 */
	boolean sendDataToSvr(String ip, int port, IBytesBuild data, int timeout) throws Exception;

	/**
	 * 指定的长连接发送数据
	 * 
	 * @param listener 长连接监视器
	 * @param data
	 * @throws Exception
	 */
	boolean sendDataToSvr(NetEventListener listener, IBytesBuild data) throws Exception;
}
