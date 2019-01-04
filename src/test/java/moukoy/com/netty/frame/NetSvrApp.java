package moukoy.com.netty.frame;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

import org.junit.Before;
import org.junit.Test;

import soft.common.file.PathUtil;
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
			URL url = NetSvrApp.class.getProtectionDomain().getCodeSource().getLocation();
			String runPath = new File(URLDecoder.decode(url.getPath(), "utf-8")).getAbsolutePath();
			runPath = PathUtil.getParentDir(runPath);// 特别注意：打jar包时启用【调试时注释掉此行】
			soft.access.NettyFrame.initSysArgs(runPath);
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
