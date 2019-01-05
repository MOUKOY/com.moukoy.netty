package soft.access;

import java.io.File;

import io.netty.util.ResourceLeakDetector;
import soft.common.StringUtil;
import soft.net.conf.ConfReader;

/**
 * 网络框架启动入口
 *
 */
public class NettyFrame {
	private static String RunPath;

	/**
	 * 初始化netty 框架参数
	 * 
	 * @param runPath 运行路径 默认PARANOID监控级别
	 * @throws Exception
	 */
	public static void initSysArgs(String runPath) throws Exception {
		initSysArgs(runPath, ResourceLeakDetector.Level.PARANOID);

	}

	/**
	 * 初始化netty 框架参数
	 * 
	 * @param runPath 运行路径
	 * @param level   监控级别
	 * @throws Exception
	 */
	public static void initSysArgs(String runPath, ResourceLeakDetector.Level level) throws Exception {

		RunPath = runPath;
		System.out.println("soft is initing....");
		if (runPath == null) {
			System.out.println("path get fail,init runPath...");
			if (!isWinOS()) {// linux
				RunPath = "/tmp/softNet";
				System.out.println("init linux runPath sucessful");
			} else {
				RunPath = "c:/tmp/softNet";
			}
		}
		if (!StringUtil.isStringNull(RunPath)) {
			File runFile = new File(RunPath);
			if (!runFile.exists()) {
				runFile.mkdirs();
			}
			System.setProperty("softRun.path", RunPath);
		}

		// sdkcommon
		moukoy.sdkcommon.App.init(RunPath);

		// netty
		nettySetting(level);
		ConfReader.init(RunPath);

	}

	/**
	 * OS版本判断
	 * 
	 * @return true windows; false:linux
	 */
	private static boolean isWinOS() {
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0) {
			return true;
		} else
			return false;
	}

	/**
	 * netty 配置 DISABLED>SIMPLE>ADVANCED>PARANOID 性能排序,监控级别越来越高，
	 * 
	 * @param level 内存泄露监控级别
	 */
	private static void nettySetting(ResourceLeakDetector.Level level) {

		System.setProperty("io.netty.noUnsafe", "false");// 屏蔽 jdk.internal.misc.Unsafe.allocateUninitializedArray(int):
		// unavailable java.lang.ClassNotFoundException:
		// jdk.internal.misc.Unsafe

		switch (level) {
		case PARANOID:// 最高级测试监控
		case ADVANCED:// 高级检测
			System.setProperty("io.netty.leakDetection.maxRecords", "100");
			System.setProperty("io.netty.leakDetection.acquireAndReleaseOnly", "true");
			ResourceLeakDetector.setLevel(level);
			break;
		default:// 默认级别
			ResourceLeakDetector.setLevel(level);
			break;
		}
	}

	public static void main(String[] args) {
		try {
			initSysArgs("D:\\MyPerson\\netPlat\\src");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
