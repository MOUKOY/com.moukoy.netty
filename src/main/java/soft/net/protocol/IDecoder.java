package soft.net.protocol;

import soft.net.ifs.IByteBuff;

/**
 * netty解码器
 * 
 * @author fanpei
 *
 */
public interface IDecoder {

	/**
	 * 解析数据
	 * 
	 * @param in
	 *            数据流
	 * @param creator
	 *            网络协议创建器
	 * 
	 */
	void deCode(IByteBuff in);

	/**
	 * 是否有解析结果
	 * 
	 * @return
	 */
	boolean hasDecDatas();

	/**
	 * 获取解析数据
	 * 
	 * @return
	 */
	IProtocol popData();
}
