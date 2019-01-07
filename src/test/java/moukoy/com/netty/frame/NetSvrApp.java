package moukoy.com.netty.frame;

import org.junit.Before;
import org.junit.Test;

import soft.net.lconectserver.LConectServer;

/**
 * 网络客户端程序
 * 
 * @author fanpei
 *
 */
public class NetSvrApp {

	private LConectServer server;

	@Before
	public void before() {
		try {
			soft.access.NettyFrame.initSysArgs();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void start() {
		try {
			server = new LConectServer();
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
