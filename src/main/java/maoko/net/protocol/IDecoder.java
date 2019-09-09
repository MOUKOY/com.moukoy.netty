package maoko.net.protocol;

import maoko.net.ifs.IByteBuff;

/**
 * netty解码器
 * 
 * @author fanpei
 *
 */
public interface IDecoder<Protocol extends IProtocol> {

	/**
	 * 解析数据
	 * 
	 * @param in
	 *            数据流
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
	Protocol popData();
}
