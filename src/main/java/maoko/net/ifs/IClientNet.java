package maoko.net.ifs;

import maoko.net.model.ClientChanel;
import maoko.net.model.NetEventListener;

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
	 * @return
	 */
	ClientChanel connectServer(String ip, int port);

	/**
	 * 向服务端发送数据
	 * 
	 * @param ip
	 * @param port
	 * @param data
	 * @throws Exception
	 */
	boolean sendDataToSvr(String ip, int port, IBytesBuild data) throws Exception;

	/**
	 * 指定的长连接发送数据
	 * 
	 * @param listener 长连接监视器
	 * @param data
	 * @throws Exception
	 */
	boolean sendDataToSvr(NetEventListener listener, IBytesBuild data) throws Exception;
}
