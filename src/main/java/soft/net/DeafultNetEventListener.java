package soft.net;

import io.netty.channel.socket.nio.NioSocketChannel;
import soft.common.log.IWriteLog;
import soft.common.log.Log4j2Writer;
import soft.net.ifs.IByteBuff;
import soft.net.model.NetEventListener;
import soft.net.protocol.IPListener;
import soft.net.protocol.IProtocol;

/**
 * gtmc监听器实现
 * 
 * @author fanpei
 *
 */
@IPListener
public class DeafultNetEventListener extends NetEventListener {
	private static final IWriteLog log = new Log4j2Writer(DeafultNetEventListener.class);

	public DeafultNetEventListener(NioSocketChannel ch) {
		super(ch);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void chanelConect() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dataReciveEvent(IByteBuff buff) {
		try {
			log.debug("deafult listener recv data:{}", buff.printHex());
			int len = buff.readableBytes();
			byte[] datas = new byte[len];
			buff.readBytes(datas);
		} catch (Exception e) {
			log.error("打印获取数据失败", e);
		}
	}

	@Override
	public void closeEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public IProtocol getProtocol() {
		return null;
	}

	@Override
	public String getListenerTypeStr() {
		return "DeafultNetEventListener";
	}

}
