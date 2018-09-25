package soft.net.model;

import soft.ifs.IByteBuff;

/**
 * 读取网络流
 * 
 * @author fanpei
 *
 */
public abstract class AReadNetStream {
	/**
	 * 读取数据并返回已经读取数据大小
	 * 
	 * @param in
	 * @param totalLen
	 *            该部分总数据长度
	 * @param hasReadLen
	 *            已读取多少数据
	 * @param datas
	 *            需要填充数据的目标源
	 * @return
	 */
	protected int readStreamToArray(IByteBuff in, int totalLen, int hasReadLen, byte[] datas) {
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
	 * @param buffLen
	 *            流总长度
	 * @param remainLength
	 *            剩余要读取长度
	 * @return
	 */
	protected int cacRemainLen(int buffLen, int remainLength) {
		return remainLength - buffLen > 0 ? buffLen : remainLength;// 剩余长度还大于当前剩余流长度,读完
	}
}
