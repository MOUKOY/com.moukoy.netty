package soft.net.protocol;

import soft.net.exception.DecodeDataFailException;
import soft.net.ifs.IByteBuff;
import soft.net.ifs.IBytesBuild;
import soft.net.ifs.IParse;

/**
 * 协议接口
 * 
 * @author fanpei
 *
 */
public interface IProtocol extends IParse, IBytesBuild {

	/**
	 * 获取数据
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
		int tmphasReadLen = hasReadLen;
		int buffLen = in.readableBytes();
		if (buffLen > 0) {
			int remainLength = totalLen - tmphasReadLen;
			int needRead = cacRemainLen(buffLen, remainLength);
			in.readBytes(datas, tmphasReadLen, needRead);
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
