package soft.net.protocol;

import soft.net.exception.DecodeDataFailException;
import soft.net.ifs.IByteBuff;
import soft.net.ifs.IParse;

/**
 * 协议接口
 * 
 * @author fanpei
 *
 */
public interface IProtocol extends IParse {

	/**
	 * 起始头
	 */
	byte STARTFLAG = (byte) 0xFF;
	/**
	 * 结束头
	 */
	byte ENDFLAG = (byte) 0xFF;

	/**
	 * 校验数据
	 * 
	 * @throws CRC8CheckNotSame
	 * 
	 * @throws Exception
	 */
	void validate() throws Exception;

	/**
	 * 读取头
	 * 
	 * @param in
	 */
	void readHeader(IByteBuff in) throws DecodeDataFailException;

	/**
	 * 头是否读取完成
	 * 
	 * @return
	 */
	boolean headerReadEnough();

	/**
	 * 数据是否读取完成
	 * 
	 * @return
	 */
	boolean dataReadEnough();

	/**
	 * 读取数据
	 * 
	 * @param in
	 */
	void readData(IByteBuff in);

	/**
	 * 尾部协议包是否读取完成
	 * 
	 * @return
	 */
	boolean enderReadEnough();

	/**
	 * 读取尾部
	 * 
	 * @param in
	 */
	void readEnd(IByteBuff in);

	/**
	 * 解析头（实例化数据）
	 * 
	 */
	void parseHeaders();

	/**
	 * 克隆副本
	 * 
	 * @return
	 */
	IProtocol copyProtocol();

	/**
	 * 组装发送二进制协议
	 * 
	 * @return
	 */
	byte[] buildSendBytes();
}
