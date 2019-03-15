package maoko.net.ifs;

/**
 * 数据发送借口
 * 
 * @author fanpei
 * @date 2018-09-10 03:54
 *
 */
public interface ISendData {
	/**
	 * 发送数据是否等待对端缓冲区处理【等待】
	 * 
	 * @param netdata
	 * @param isWait
	 */
	boolean sendData(IBytesBuild netdata, boolean isWait) throws Exception;

	/**
	 * 不等待立即返回
	 * 
	 * @param data
	 */
	boolean sendData(IBytesBuild data) throws Exception;

	/**
	 * 发送数据是否等待对端缓冲区处理【等待】
	 * 
	 * @param netdatas
	 * @param isWait
	 */
	boolean sendData(byte[] netdatas, boolean isWait) throws Exception;

	/**
	 * 不等待立即返回
	 * 
	 * @param datas
	 */
	boolean sendData(byte[] datas) throws Exception;
}
