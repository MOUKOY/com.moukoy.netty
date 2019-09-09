package maoko.net.protocol;

import maoko.common.exception.NotContainException;
import maoko.net.exception.DecodeDataFailException;
import maoko.net.ifs.IByteBuff;
import maoko.net.ifs.IBytesBuild;
import maoko.net.ifs.IParse;

/**
 * 协议接口
 * 
 * @author fanpei
 *
 */
public interface IProtocol extends IParse, IBytesBuild {

	/**
	 * 获取协议设备ID
	 * 
	 * @return
	 * @throws NotContainException 设备ID未初始化,协议不包含设备ID
	 */
	public int getDeviceID() throws NotContainException;

	/**
	 * 获取协议数据总长度【byte[]协议流总长度，不代表Datas和Childdatas数据之和】
	 * 
	 * @return
	 */
	public int getTotalLen();

	/**
	 * 获取数据部分数据
	 * 
	 * @return
	 */
	public byte[] getDatas();

	/**
	 * 获取子功能数据
	 * 
	 * @return
	 */
	public byte[] getChilddatas();

	/**
	 * 校验数据
	 * 
	 * 
	 * @throws Exception
	 */
	void validate() throws Exception;

	/**
	 * 读取头
	 * 
	 * @param in
	 */
	void readHeader(IByteBuff in) throws Exception;

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
	void parseHeaders()throws Exception;

	/**
	 * 获取解析完整对象的原始数据16进制形式[注意：只返回一次，第二次调用返回为空，数据被清除]
	 */
	String srcHex();

	/**
	 * 克隆副本
	 * 
	 * @return
	 * @throws Exception
	 */
	IProtocol copyProtocol() throws Exception;

	/**
	 * 读取数据并返回已经读取数据大小
	 * 
	 * @param in
	 * @param totalLen   该部分总数据长度
	 * @param hasReadLen 已读取多少数据
	 * @param datas      需要填充数据的目标源
	 * @return
	 */
	static int readStreamToArray(IByteBuff in, int totalLen, int hasReadLen, byte[] datas) {
		return readStreamToArray(in, totalLen, hasReadLen, datas, null);
	}

	/**
	 * 读取数据并返回已经读取数据大小
	 * 
	 * @param in
	 * @param totalLen   该部分总数据长度
	 * @param hasReadLen 已读取多少数据
	 * @param datas      需要填充数据的目标源
	 * @param srcBH      将读取的字节数据填充至srcBH
	 * @return
	 */
	public static int readStreamToArray(IByteBuff in, int totalLen, int hasReadLen, byte[] datas, SrcBinaryHex srcBH) {
		int tmphasReadLen = hasReadLen;
		int buffLen = in.readableBytes();
		if (buffLen > 0) {
			int remainLength = totalLen - tmphasReadLen;
			int needRead = cacRemainLen(buffLen, remainLength);
			in.readBytes(datas, tmphasReadLen, needRead, srcBH);
			tmphasReadLen += needRead;// 移动已读datas取数据长度
		}
		return tmphasReadLen;
	}

	/**
	 * 计算剩余读取长度
	 * 
	 * @param buffLen      流总长度
	 * @param remainLength 剩余要读取长度
	 * @return
	 */
	static int cacRemainLen(int buffLen, int remainLength) {
		return remainLength - buffLen > 0 ? buffLen : remainLength;// 剩余长度还大于当前剩余流长度,读完
	}
}
