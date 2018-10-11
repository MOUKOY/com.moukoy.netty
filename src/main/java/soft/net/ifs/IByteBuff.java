package soft.net.ifs;

import java.nio.ByteOrder;

/**
 * 读取byte数据索引移动基类
 * 
 * @type 默认ByteOrder.LITTLE_ENDIAN
 * 
 * @author fanpei
 *
 */
public interface IByteBuff {
	static ByteOrder ORDER = ByteOrder.LITTLE_ENDIAN;
	static byte ORDERVALUE = ORDER == ByteOrder.LITTLE_ENDIAN ? ((byte) 0) : ((byte) 1);

	/**
	 * 是否读完
	 * 
	 * @return
	 */
	boolean hasData();

	/**
	 * 剩下可读取数据长度
	 * 
	 * @return
	 */
	int readableBytes();

	byte readByte();

	short readShort();

	int readInt();

	long readLong();

	/**
	 * 向指定的缓冲区填充数据
	 * 
	 * @param tmpBuff 目标缓冲区
	 */
	void readBytes(byte[] tmpBuff);

	/**
	 * 向指定的缓冲区填充数据
	 * 
	 * @param destBuff    目标缓冲区
	 * @param destPos     目标缓冲区位置
	 * @param destReadLen 欲读取数据长度
	 */
	void readBytes(byte[] destBuff, int destPos, int destReadLen);

	/**
	 * 读取剩余所有数据
	 * 
	 * @return
	 */
	byte[] readAllBytes();

	/**
	 * 释放资源
	 */
	void release();

	void printHex();

}
