package soft.access;

import soft.net.conf.ConfReader;

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
		System.out.println("sdkcommon is initing....");

		// sdkcommon
		moukoy.sdkcommon.App.init();

		// netty
		ConfReader.init();

		System.out.println("sdkcommon is inited success....");
	}

}
