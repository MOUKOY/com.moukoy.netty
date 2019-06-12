package maoko.net.protocol;

import io.netty.channel.socket.nio.NioSocketChannel;
import maoko.common.log.IWriteLog;
import maoko.common.log.Log4j2Writer;
import maoko.net.ifs.IByteBuff;
import maoko.net.model.NetEventListener;

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
