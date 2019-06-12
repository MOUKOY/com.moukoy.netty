package maoko.net;

import maoko.net.conf.ConfReader;
import maoko.sdk.SDKCommon;

/**
 * 网络框架启动入口
 *
 */
public class NettyFrame {
	// private static String RunPath;

	/**
	 * 初始化netty 框架参数
	 * 
	 * @param level 监控级别
	 * @throws Exception
	 */
	public static void initSysArgs() throws Exception {
		System.out.println("netty is start initing....");
		// sdkcommon
		SDKCommon.init();

		// netty
		ConfReader.init();

		System.out.println("netty is inited success....");
	}

}
