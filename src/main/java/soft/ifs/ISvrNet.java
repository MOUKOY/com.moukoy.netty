package soft.ifs;

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
	 * 向指定客户端发送数据
	 * 
	 * @param chanelId 链路标识
	 * @param data     数据内容
	 * 
	 * @return
	 */
	@Deprecated
	boolean sendDataToClient(INetChanel chanel, IBytesBuild data) throws Exception;

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
	 * @param data
	 * @throws Exception
	 */
	void sendDataToAllClient(int localPort, IBytesBuild data) throws Exception;

	/**
	 * 获取本地指定端口上所有连接的个数
	 * 
	 * @param localPort
	 * @return
	 */
	int getAllConnectNum(int localPort);
}
