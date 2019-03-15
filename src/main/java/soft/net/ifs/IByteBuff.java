package soft.net.ifs;

import java.nio.ByteOrder;

import soft.net.protocol.SrcBinaryHex;

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
	
//	/**
//	 * 设置是否需要缓存原始数据成十六进制字符串
//	 * @param cache 是否需要缓存
//	 */
//	void setCacheSrcByte(boolean cache);
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

	/**
	 * 读取一个byte
	 * @return
	 */
	byte readByte();
	
	/**
	 * 读取一个byte,并将读出字节转换成16进制字符串存入srcBH
	 * @param srcBH
	 * @return
	 */
	byte readByte(SrcBinaryHex srcBH);
	
	/**
	 * 读取一个short
	 * @return
	 */
	short readShort();
	/**
	 * 读取一个short,并将读出字节转换成16进制字符串存入srcBH
	 * @param srcBH
	 * @return
	 */
	short readShort(SrcBinaryHex srcBH);

	/**
	 * 读取一个int
	 * @return
	 */
	int readInt();
	
	/**
	 * 读取一个int,并将读出字节转换成16进制字符串存入srcBH
	 * @param srcBH
	 * @return
	 */
	int readInt(SrcBinaryHex srcBH);

	/**
	 * 读取一个long
	 * @return
	 */
	long readLong();
	/**
	 * 读取一个long,并将读出字节转换成16进制字符串存入srcBH
	 * @param srcBH
	 * @return
	 */
	long readLong(SrcBinaryHex srcBH);

	/**
	 * 向指定的缓冲区填充数据
	 * 
	 * @param tmpBuff 目标缓冲区
	 */
	void readBytes(byte[] tmpBuff);
	/**
	 * 向指定的缓冲区填充数据,并将读出字节转换成16进制字符串存入srcBH
	 * @param srcBH
	 * @return
	 */
	void readBytes(byte[] tmpBuff,SrcBinaryHex srcBH);

	/**
	 * 向指定的缓冲区填充数据
	 * 
	 * @param destBuff    目标缓冲区
	 * @param destPos     目标缓冲区位置
	 * @param destReadLen 欲读取数据长度
	 */
	void readBytes(byte[] destBuff, int destPos, int destReadLen);
	/**
	 * 向指定的缓冲区填充数据,并将读出字节转换成16进制字符串存入srcBH
	 * @param srcBH
	 * @return
	 */
	void readBytes(byte[] destBuff, int destPos, int destReadLen,SrcBinaryHex srcBH);

	/**
	 * 读取剩余所有数据
	 * 
	 * @return
	 */
	byte[] readAllBytes();
	
	/**
	 * 读取剩余所有数据,并将读出字节转换成16进制字符串存入srcBH
	 * @param srcBH
	 * @return
	 */
	byte[] readAllBytes(SrcBinaryHex srcBH);
	

	/**
	 * 释放资源
	 */
	void release();

	/**
	 * 获取数据16进制形式
	 */
	String printHex();

}
