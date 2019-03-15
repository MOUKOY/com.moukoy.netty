package maoko.net.ifs;

import io.netty.channel.socket.nio.NioSocketChannel;
import maoko.net.model.NetEventListener;

/**
 * 网络资源创建器接口
 * 
 * @author fanpei
 */
public interface IListenerCreator {

	/**
	 * 获取一个监听者
	 * 
	 * @return
	 */
	NetEventListener getListener(NioSocketChannel ch);

}
