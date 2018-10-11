package soft.net.protocol;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import soft.common.log.IWriteLog;
import soft.common.log.LogWriter;
import soft.net.ifs.IByteBuff;
import soft.net.ifs.IDecoder;
import soft.net.model.NetEventListener;

/**
 * 协议解码器
 * 
 * @author fanpei
 *
 */
public class MyDecoder implements IDecoder {
	private static final IWriteLog log = new LogWriter(MyDecoder.class);

	private Queue<IProtocol> resluts = null;// 解析结果
	private IProtocol protocol = null;// 当前数据

	public MyDecoder(NetEventListener listener) {
		this.protocol = listener.getProtocol();
		this.resluts = new ConcurrentLinkedDeque<>();
	}

	@Override
	public IProtocol popData() {
		return resluts.poll();
	}

	@Override
	public void deCode(IByteBuff in) {
		try {
			while (in.hasData()) {

				if (!protocol.headerReadEnough())
					protocol.readHeader(in);

				if (protocol.headerReadEnough()) {// 头读完

					if (!protocol.dataReadEnough())
						protocol.readData(in);

					if (protocol.dataReadEnough()) {// 数据读完

						if (!protocol.enderReadEnough())
							protocol.readEnd(in);

						if (protocol.enderReadEnough()) {// 尾部读取完
							protocol.validate();
							protocol.parse();
							IProtocol newprotocol = protocol.copyProtocol();
							resluts.add(newprotocol);
							protocol.clear();
						}
					}
				}
			}
		} catch (Exception e) {
			protocol.clear();
			log.error("解析数据报文错误", e);
		} finally {
			in.release();// 释放资源
		}

	}

	@Override
	public boolean hasDecDatas() {
		return !resluts.isEmpty();
	}
}
